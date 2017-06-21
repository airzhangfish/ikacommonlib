package com.ikags.ikalib.gameutil.opengl;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLSurfaceView;

import com.ikags.ikalib.gameutil.opengl.lib.Matrix4f;

/**
 * IKA3D渲染器,用法参考IKA3DGLView
 * @author zhangxiasheng
 *
 */
public class IKA3DGLRender implements GLSurfaceView.Renderer {
	public static final int FRAME_DELAY=20;
	Context acti = null;


	IKA3DScene mScene = null;


	public IKA3DGLRender(Context acti, IKA3DScene scene) {
		this.acti = acti;
		mScene = scene;
	}

	private long startTime = 0;
	private long endTime = 0;

	@Override
	public void onDrawFrame(GL10 gl) {
		startTime = System.currentTimeMillis();
		// 一般的opengl程序，首先要做的就是清屏
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		gl.glClearColor(0.5f, 0.5f, 0.5f, 1.0f);
		//gl.glClearColor(0f, 0f, 0f, 1.0f);
		gl.glLoadIdentity();

		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);

		gl.glEnable(GL10.GL_DEPTH_TEST);
		gl.glEnable(GL10.GL_ALPHA_TEST); // Enable Alpha Testing (To Make BlackTansparent)
		gl.glAlphaFunc(GL10.GL_GREATER, 0.1f); // Set Alpha Testing (To Make Black Transparent)


		//TODO 测试代码
//		  GLU.gluOrtho2D(gl, 0.0f,320f,0.0f,240f);
//		  gl.glEnable(GL10.GL_TEXTURE_2D);
//		  gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);

		try{
			if (mScene != null) {
				mScene.onSceneDrawFrame(gl);
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		endTime = System.currentTimeMillis();
		try {
			IKA3DConfig.gDrawFrameTime = endTime - startTime;
			if (IKA3DConfig.gDrawFrameTime <FRAME_DELAY) { // 帧数大于24帧
				Thread.sleep(FRAME_DELAY - IKA3DConfig.gDrawFrameTime);
			} else {
				Thread.sleep(5);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	/**
	 * 当绘图表面尺寸发生改变时调用
	 */
	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		// 设置视口

		gl.glViewport(0, 0, width, height);
		IKA3DConfig.gpViewport[0] = 0;
		IKA3DConfig.gpViewport[1] = 0;
		IKA3DConfig.gpViewport[2] = width;
		IKA3DConfig.gpViewport[3] = height;

		//设置投影矩阵
		IKA3DConfig.mRatio = (float) width / height;//屏幕宽高比
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();
		//GLU.gluPerspective(gl, 45.0f, ratio, 1, 5000);//系统提供
		Matrix4f.gluPersective(45.0f, IKA3DConfig.mRatio, 1f, 5000f, IKA3DConfig.gMatProject);
		gl.glLoadMatrixf(IKA3DConfig.gMatProject.asFloatBuffer());
		IKA3DConfig.gMatProject.fillFloatArray(IKA3DConfig.gpMatrixProjectArray);
		//每次修改完GL_PROJECTION后，最好将当前矩阵模型设置回GL_MODELVIEW
		gl.glMatrixMode(GL10.GL_MODELVIEW);
	}




	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		onSurfaceCreatedConfig(gl, config);

		if (mScene != null) {
			mScene.onSceneCreated(gl, config);
		}
	}


	private void onSurfaceCreatedConfig(GL10 gl, EGLConfig config){
		//全局性设置
		gl.glEnable(GL10.GL_DITHER);

		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);
		//设置清屏背景颜色
		gl.glClearColor(0.5f, 0.5f, 0.5f, 1);
		//设置着色模型为平滑着色
		gl.glShadeModel(GL10.GL_SMOOTH);

		//启用背面剪裁
//		gl.glEnable(GL10.GL_CULL_FACE);
//		gl.glCullFace(GL10.GL_BACK);
		//启用深度测试
		gl.glEnable(GL10.GL_DEPTH_TEST);
		//禁用光照和混合
		gl.glDisable(GL10.GL_LIGHTING);
		gl.glEnable(GL10.GL_BLEND);

	}


}

