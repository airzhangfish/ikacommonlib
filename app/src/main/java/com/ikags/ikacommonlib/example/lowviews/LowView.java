package com.ikags.ikacommonlib.example.lowviews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;

import com.ikags.ikalib.androidview.IKALowSurfaceView;

public class LowView extends IKALowSurfaceView{
	
	public LowView(Context context) {
		super(context);
	}
	public LowView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	@Override
	public void onInitView(Context context) {
//		Log.v("LowView", "onInitView runing");
//		testpad=new Paint();
		
	}
	Paint testpad=null;
	Paint mpaint=new Paint();
	Rect mrect=new Rect();
	int count=0;
	@Override
	public void onUpdateDraw(Canvas mCanvas) {
//		if(testpad!=null){
			Log.v("LowView", mWidth+","+mHeight);
//		}else{
//			Log.v("LowView", "testpad is null");
//		}
		int left=count%mWidth;
		int top=count%mHeight;
		mpaint.setColor(0xffff0000);
		mrect.set(left, top, left+10, top+10);
		mCanvas.drawRect(mrect, mpaint);
		count++;
		count++;
		count++;
	}
	@Override
	public void onSurfaceDestroyed(SurfaceHolder holder) {
	
		
	}



}
