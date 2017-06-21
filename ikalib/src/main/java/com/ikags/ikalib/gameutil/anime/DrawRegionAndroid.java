package com.ikags.ikalib.gameutil.anime;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
/**
 * 绘制旋转图片以及相关功能,主要用于动画绘制
 * @author zhangxiasheng
 *
 */
public class DrawRegionAndroid {

	private static Matrix mMatrix = new Matrix();
	/**
	 * 全角度旋转显示
	 * @param image_src
	 * @param mX
	 * @param mY
	 * @param x_dest
	 * @param y_dest
	 * @param rot
	 * @param scale
	 * @param framescale
	 * @param isleft
	 * @param canvas
	 * @param mPaint
	 */
	public static void drawRegion(Bitmap image_src, float mX, float mY, float x_dest, float y_dest, float rot, float scale, float framescale, boolean isleft, Canvas canvas, Paint mPaint) {

		// 旋转处理
		// Log.v("drawRegion", "rot="+rot+",scale="+scale+",isleft="+isleft);
		double ma = image_src.getWidth() * image_src.getWidth() + image_src.getHeight() * image_src.getHeight();
		float imgMaxwidth = (float) (Math.sqrt(ma) * scale);
		mMatrix.reset();
		mMatrix.setTranslate(mX + x_dest, mY + y_dest);
		mMatrix.postRotate(rot, mX + x_dest + imgMaxwidth / 2, mY + y_dest + imgMaxwidth / 2);
		mMatrix.postScale(scale, scale, mX + x_dest - imgMaxwidth / 2, mY + y_dest + imgMaxwidth / 2);
		if (framescale != 1f) {
			mMatrix.postScale(framescale, framescale, mX, mY);
		}
		if (isleft == false) {
			mMatrix.postScale(-1f, 1f, mX, mY);
		}

		canvas.drawBitmap(image_src, mMatrix, mPaint);
	}

	/**
	 * 原来的 90度+镜像的方式显示(原始J2ME用法)
	 * @param image_src
	 * @param x_src
	 * @param y_src
	 * @param width
	 * @param height
	 * @param transform
	 * @param x_dest
	 * @param y_dest
	 * @param anchor
	 * @param canvas
	 * @param mPaint
	 */
	public static void drawRegion(Bitmap image_src, int x_src, int y_src, int width, int height, int transform, int x_dest, int y_dest, int anchor, Canvas canvas, Paint mPaint) {

		if ((anchor & 2) != 0) {
			y_dest -= height / 2;
		} else if ((anchor & 32) != 0) {
			y_dest -= height;
		}
		if ((anchor & 8) != 0) {
			x_dest -= width;
		} else if ((anchor & 1) != 0) {
			x_dest -= width / 2;
		}
		Bitmap newMap = Bitmap.createBitmap(image_src, x_src, y_src, width, height);
		Matrix mMatrix = new Matrix();
		Matrix temp = new Matrix();
		float[] mirrorY = {-1, 0, 0, 0, 1, 0, 0, 0, 1};
		temp.setValues(mirrorY);
		switch (transform) {
			case 0 :
				break;
			case 5 :
				mMatrix.setRotate(90, width / 2, height / 2);
				break;
			case 3 :
				mMatrix.setRotate(180, width / 2, height / 2);
				break;
			case 6 :
				mMatrix.setRotate(270, width / 2, height / 2);
				break;
			case 2 :
				mMatrix.postConcat(temp);
				break;
			case 7 :
				mMatrix.postConcat(temp);
				mMatrix.setRotate(90, width / 2, height / 2);
				break;
			case 1 :
				mMatrix.postConcat(temp);
				mMatrix.setRotate(180, width / 2, height / 2);
				break;
			case 4 :
				mMatrix.postConcat(temp);
				mMatrix.setRotate(270, width / 2, height / 2);
				break;
		}
		mMatrix.setTranslate(x_dest, y_dest);
		canvas.drawBitmap(newMap, mMatrix, mPaint);
	}
	/**
	 * 绘制tilemap地图
	 * @param image_src
	 * @param x_dest
	 * @param y_dest
	 * @param transform
	 * @param anchor
	 * @param canvas
	 * @param mPaint
	 */
	public static void drawRegion_map(Bitmap image_src, int x_dest, int y_dest, int transform, int anchor, Canvas canvas, Paint mPaint) {
		// public static final int BASELINE 64
		// public static final int BOTTOM 32
		// public static final int DOTTED 1
		// public static final int HCENTER 1
		// public static final int LEFT 4
		// public static final int RIGHT 8
		// public static final int SOLID 0
		// public static final int TOP 16
		// public static final int VCENTER 2

		int height = image_src.getHeight();
		int width = image_src.getWidth();
		if ((anchor & 2) != 0) {
			y_dest -= height / 2;
		} else if ((anchor & 32) != 0) {
			y_dest -= height;
		}
		if ((anchor & 8) != 0) {
			x_dest -= width;
		} else if ((anchor & 1) != 0) {
			x_dest -= width / 2;
		}
		Matrix mMatrix = new Matrix();
		Matrix temp = new Matrix();
		float[] mirrorY = {-1, 0, 0, 0, 1, 0, 0, 0, 1};
		temp.setValues(mirrorY);
		switch (transform) {
			case 0 :
				break;
			case 5 :
				mMatrix.setRotate(90, width / 2, height / 2);
				break;
			case 3 :
				mMatrix.setRotate(180, width / 2, height / 2);
				break;
			case 6 :
				mMatrix.setRotate(270, width / 2, height / 2);
				break;
			case 2 :
				mMatrix.postConcat(temp);
				break;
			case 7 :
				mMatrix.postConcat(temp);
				mMatrix.setRotate(90, width / 2, height / 2);
				break;
			case 1 :
				mMatrix.postConcat(temp);
				mMatrix.setRotate(180, width / 2, height / 2);
				break;
			case 4 :
				mMatrix.postConcat(temp);
				mMatrix.setRotate(270, width / 2, height / 2);
				break;
		}
		mMatrix.setTranslate(x_dest, y_dest);
		canvas.drawBitmap(image_src, mMatrix, mPaint);
	}
}
