package com.ikags.ikalib.androidview;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.view.SurfaceView;
/**
 * SurfaceView相关的设置方法
 * @author airzhangfish
 *
 */
public class SurfaceUtil {



	/**
	 * 背景透明的控件,初始化需要调用
	 * @param sfv
	 */
	public static void initSurfaceTransparent(SurfaceView sfv){
		sfv.setZOrderOnTop(true);
		sfv.getHolder().setFormat(PixelFormat.TRANSLUCENT);
	}

	/**
	 * 背景透明的控件,需要绘画
	 * @param mCanvas
	 */
	public static void drawTransparent(Canvas mCanvas){
		if(mCanvas!=null){
			mCanvas.drawColor(Color.TRANSPARENT,Mode.CLEAR);
		}
	}

}
