package com.ikags.ikalib.gameutil.opengl;

import com.ikags.ikalib.gameutil.opengl.lib.Matrix4f;
/**
 * openGL界面设置和配置相关
 * @author zhangxiasheng
 *
 */
public class IKA3DConfig {
	/**
	 * 投影矩阵(系统矩阵,一般不变)
	 */
	public static Matrix4f gMatProject = new Matrix4f();
	/**
	 * 视图矩阵(镜头矩阵,设置镜头的时候需要改变)
	 */
	public static Matrix4f gMatView = new Matrix4f();

//	/**
//	 * 模型矩阵(变更模型后需要更新的矩阵)
//	 */
	//public static Matrix4f gMatModel = new Matrix4f();

	/**
	 * 视口参数
	 */
	public static int[] gpViewport = new int[4];
	/**
	 * 当前系统的投影矩阵，列序填充
	 */
	public static float[] gpMatrixProjectArray = new float[16];
	/**
	 * 当前系统的视图矩阵，列序填充
	 */
	public static float[] gpMatrixViewArray = new float[16];
	/**
	 * 当前帧绘画消耗的毫秒时间
	 */
	public static long gDrawFrameTime=0;
	public static float mRatio = 0;
	public static float gScreenX, gScreenY;

	public static void setTouchPosition(float x, float y) {
		gScreenX = x;
		gScreenY = y;
	}

}
