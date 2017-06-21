package com.ikags.ikalib.gameutil.opengl;

import java.io.InputStream;

import javax.microedition.khronos.opengles.GL10;

import android.app.Activity;

import com.ikags.ikalib.gameutil.opengl.lib.Matrix4f;
import com.ikags.ikalib.gameutil.opengl.lib.Ray;
import com.ikags.ikalib.gameutil.opengl.lib.TextureFactory;
import com.ikags.ikalib.gameutil.opengl.lib.TextureInfo;
import com.ikags.ikalib.gameutil.opengl.lib.Vector3f;
import com.ikags.ikalib.gameutil.opengl.ms3d.IMS3DModel;
/**
 * 绘制3D的工具
 * @author zhangxiasheng
 *
 */
public class IKA3DUtil {

	private static int mMsPerFrame = 1;
	/**
	 * 绘制模型
	 * @param gl
	 * @param ms3d
	 * @param scale
	 */
	public static void drawModel(GL10 gl, IMS3DModel ms3d, float scale) {
		gl.glPushMatrix();
		{
			// 首先对模型进行旋转
			gl.glScalef(scale, scale, scale);
			if (ms3d.containsAnimation()) {
				// 如果模型有动画，那么按时间就更新动画
				if (mMsPerFrame > 0) {
					ms3d.animate(mMsPerFrame * 0.1f);// 将毫秒数转化为秒, /1000
				}
				ms3d.fillRenderBuffer();// 更新顶点缓存
			}
			ms3d.render(gl);// 渲染模型
			// mModel.renderJoints(gl);// 渲染关节，骨骼
		}
		gl.glPopMatrix();
	}

	/**
	 * 载入模型--多个纹理
	 *
	 * @param gl
	 * @param idxModel
	 *            - 模型资源索引
	 * @param pIdxTex
	 *            - 纹理数组
	 */
	public static IMS3DModel loadModel(Activity acti, GL10 gl, int idxModel, int[] pIdxTex) {
		IMS3DModel mModel = new IMS3DModel();
		try {
			TextureInfo[] pTexInfos = new TextureInfo[pIdxTex.length];
			// 打开模型二进制流
			InputStream is = acti.getResources().openRawResource(idxModel);
			if (mModel.loadModel(is)) {
				// 载入模型成功，开始载入纹理
				for (int i = 0; i < pTexInfos.length; i++) {
					pTexInfos[i] = new TextureInfo();
					// 得到创建成功的纹理对象名称
					pTexInfos[i].mTexID = TextureFactory.getTexture(acti, gl, pIdxTex[i]);
				}
				// 赋予纹理
				mModel.setTexture(pTexInfos);
			} else {
				System.out.println("Load Model Failed. IdxModel:" + idxModel);
			}
			is.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return mModel;
	}

	/**
	 * 设置场景雾(详看源码)
	 *
	 * @param gl
	 */
	public static void setFog(GL10 gl) {
		// 雾气模式
		int fogMode[] = {GL10.GL_EXP, GL10.GL_EXP2, GL10.GL_LINEAR};
		// 雾气的颜色
		float fogColor[] = {0.5f, 0.5f, 0.5f, 1.0f};
		// 设置雾气的模式
		gl.glFogx(GL10.GL_FOG_MODE, fogMode[2]);
		// 设置雾气的颜色
		gl.glFogfv(GL10.GL_FOG_COLOR, fogColor, 0);
		// 设置雾气的密度
		gl.glFogf(GL10.GL_FOG_DENSITY, 0.35f);
		// 设置雾气的系统透视方案
		gl.glHint(GL10.GL_FOG_HINT, GL10.GL_DONT_CARE);
		// 雾气开始地方
		gl.glFogf(GL10.GL_FOG_START, 400.0f);
		// 雾气结束地方
		gl.glFogf(GL10.GL_FOG_END, 800.0f);
		// 开启雾气
		gl.glEnable(GL10.GL_FOG);

	}

	// 光照颜色属性
	private static float lightAmbient[] = {0f, 0f, 0f, 1f};// 环境光
	private static float lightDiffuse[] = {0.5f, 0.5f, 0.5f, 0.5f};// 散射
	private static float lightSpecular[] = {1.0f, 1.0f, 1.0f, 1f};// 高光
	// 材质颜色属性
	private static float matAmbient[] = {1.0f, 1.0f, 1.0f, 1.0f};
	private static float matDiffuse[] = {1.0f, 1.0f, 1.0f, 1.0f};
	private static float matSpecular[] = {1.0f, 1.0f, 1.0f, 1.0f};
	// 光照位置和朝向
	private static float lightPosition[] = {0f, 0f, 0f, 0.0f};
	private static float lightDirection[] = {0f, 0f, 0f, 0f};
	private static boolean reduce = false;

	/**
	 * 设置光照(相见源码)
	 *
	 * @param gl
	 */
	public static void setLight(GL10 gl) {
		// 启用光照
		gl.glShadeModel(GL10.GL_SMOOTH);
		gl.glEnable(GL10.GL_LIGHTING);
		gl.glEnable(GL10.GL_LIGHT0);
		// //设置材质参数
		gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_AMBIENT, matAmbient, 0);
		gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_DIFFUSE, matDiffuse, 0);
		gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_SPECULAR, matSpecular, 0);
		gl.glMaterialf(GL10.GL_FRONT_AND_BACK, GL10.GL_SHININESS, 5.0f);
		// //设置光源0光照颜色属性
		gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_AMBIENT, lightAmbient, 0);
		gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_DIFFUSE, lightDiffuse, 0);
		gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_SPECULAR, lightSpecular, 0);
		// 设置光源0位置及聚光灯属性
		if (!reduce) {
			lightPosition[0] = lightPosition[0] + 1f;
			lightPosition[1] = lightPosition[1] + 1f;
			lightPosition[2] = lightPosition[2] + 1f;
		} else {
			lightPosition[0] = lightPosition[0] - 1f;
			lightPosition[1] = lightPosition[1] - 1f;
			lightPosition[2] = lightPosition[2] - 1f;
		}
		if (lightPosition[1] > 50f) {
			reduce = true;
		} else if (lightPosition[1] < -50f) {
			reduce = false;
		}
		gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_POSITION, lightPosition, 0);
		gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_SPOT_DIRECTION, lightDirection, 0);
		gl.glLightf(GL10.GL_LIGHT0, GL10.GL_SPOT_CUTOFF, 30.0f);
		gl.glLightf(GL10.GL_LIGHT0, GL10.GL_SPOT_EXPONENT, 100.0f);
		gl.glShadeModel(GL10.GL_SMOOTH);
	}

	private static Vector3f transformedSphereCenter = new Vector3f();
	private static Ray transformedRay = new Ray();
	private static Matrix4f matInvertModel = new Matrix4f();
	private static Vector3f[] mpTriangle = {new Vector3f(), new Vector3f(), new Vector3f()};
	// private FloatBuffer mBufPickedTriangle = IBufferFactory.newFloatBuffer(3 * 3);

	/**
	 * 抓取的射线
	 */
	public static Ray mPickRay = null;

	/**
	 * 更新拾取事件,拾取需要的模型
	 *
	 * @param screenX
	 *            屏幕触点坐标X
	 * @param screenY
	 *            屏幕触点坐标Y
	 * @param mModel
	 *            传入的模型
	 * @param offset
	 *            坐标的位置偏移
	 * @param isAccurate
	 *            是否精确拾取,一般用false即可
	 * @return 是否拾取到
	 */
	public static boolean updatePick(float screenX, float screenY, IMS3DModel mModel, Matrix4f gMatModel, boolean isAccurate) {
		// 更新最新的拾取射线
		PickFactory.update(screenX, screenY);
		// 获得最新的拾取射线
		mPickRay = PickFactory.getPickRay();

		// 首先把模型的绑定球通过模型矩阵，由模型局部空间变换到世界空间
		gMatModel.transform(mModel.getSphereCenter(), transformedSphereCenter);
		// 首先检测拾取射线是否与模型绑定球发生相交
		// 这个检测很快，可以快速排除不必要的精确相交检测
		if (mPickRay.intersectSphere(transformedSphereCenter, mModel.getSphereRadius())) {
			// 如果射线与绑定球发生相交，那么就需要进行精确的三角面级别的相交检测
			// 由于我们的模型渲染数据，均是在模型局部坐标系中
			// 而拾取射线是在世界坐标系中
			// 因此需要把射线转换到模型坐标系中
			// 这里首先计算模型矩阵的逆矩阵

			if (isAccurate == false) {
				// 不精确相交判断的话,射线与绑定球发生相交返回true
				return true;
			}

			matInvertModel.set(gMatModel);
			matInvertModel.invert();
			// 把射线变换到模型坐标系中，把结果存储到transformedRay中
			mPickRay.transform(matInvertModel, transformedRay);
			// 将变换后的射线与模型做精确相交检测
			if (mModel.intersect(transformedRay, mpTriangle)) {
				// 精确相交
				return true;

				// 如果找到了相交的最近的三角形
				// 填充数据到被选取三角形的渲染缓存中
				// mBufPickedTriangle.position(0);
				// for(int i = 0; i < 3; i++) {
				// IBufferFactory.fillBuffer(mBufPickedTriangle, mpTriangle[i]);
				// }
				// mBufPickedTriangle.position(0);
			}
		}
		return false;
	}



	/**
	 * 设置相机
	 * @param gl
	 */
	public static void setCamera(GL10 gl,Vector3f mvEye,Vector3f mvCenter,Vector3f mvUp) {
		//设置模型视图矩阵
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();
		//GLU.gluLookAt(gl, mfEyeX, mfEyeY, mfEyeZ, mfCenterX, mfCenterY, mfCenterZ, 0, 1, 0);//系统提供
		Matrix4f.gluLookAt(mvEye, mvCenter, mvUp, IKA3DConfig.gMatView);
		gl.glLoadMatrixf(IKA3DConfig.gMatView.asFloatBuffer());
	}

	/**
	 * 背面剪裁(开启后,3D图形背后的贴图也会显示,关闭则不显示背后的贴图)
	 */
	public static void setBackFace(GL10 gl,boolean isEnable){
		if(isEnable){
			gl.glEnable(GL10.GL_CULL_FACE);
			gl.glCullFace(GL10.GL_BACK);
		}else{
			gl.glDisable(GL10.GL_CULL_FACE);
		}
	}


}