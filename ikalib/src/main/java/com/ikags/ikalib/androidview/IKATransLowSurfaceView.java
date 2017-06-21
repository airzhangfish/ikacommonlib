package com.ikags.ikalib.androidview;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
/**
 * 背景透明的ILowSurfaceView,和ILowSurfaceView使用方法一样,注意的是这个view绘制的东西都会显示到所有view的最前面
 * @author airzhangfish
 *
 */
public class IKATransLowSurfaceView extends IKALowSurfaceView  {

	public IKATransLowSurfaceView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public IKATransLowSurfaceView(Context context) {
		super(context);
	}

	public void initView(Context context) {
		mSurfaceHolder = getHolder();
		mSurfaceHolder.addCallback(this);
		mcontext = context;
		//初始化背景透明
		try {
			SurfaceUtil.initSurfaceTransparent(this);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void draw(Canvas mCanvas) {
		try {
			mWidth=mCanvas.getWidth();
			mHeight=mCanvas.getHeight();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		try {
			//绘制透明背景
			SurfaceUtil.drawTransparent(mCanvas);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		try {
			onUpdateDraw(mCanvas);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
