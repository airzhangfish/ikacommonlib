package com.ikags.ikalib.gameutil.opengl;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.view.MotionEvent;
/**
 * IKA3D场景,需要初始化
 * @author zhangxiasheng
 *
 */
public abstract class IKA3DScene {
	/**
	 *  初始化场景,读取模型等可以在此操作
	 * @param gl
	 * @param config
	 */
	abstract public  void onSceneCreated(GL10 gl, EGLConfig config);
	/**
	 * 绘制场景,建议的绘制顺序:
	 * IKA3DUtil.setCamera(gl, mvEye, mvCenter, mvUp);//先设置摄像头
	 *  IKA3DUtil.drawModel(gl,m3dmodel, 1f);	//再绘制模型
	 * @param gl
	 */
	abstract public void onSceneDrawFrame(GL10 gl);

	/**
	 * 触控反馈
	 * @param e
	 * @return
	 */
	abstract public boolean onSceneTouchEvent(MotionEvent e);
}
