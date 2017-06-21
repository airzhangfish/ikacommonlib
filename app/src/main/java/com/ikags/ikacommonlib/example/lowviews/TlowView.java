package com.ikags.ikacommonlib.example.lowviews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;

import com.ikags.ikalib.androidview.IKATransLowSurfaceView;

public class TlowView extends IKATransLowSurfaceView{
	
	public TlowView(Context context) {
		super(context);
	}
	public TlowView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	@Override
	public void onInitView(Context context) {
//		Log.v("TlowView", "onInitView runing");
		testpad=new Paint();
		
	}
	
	
	Paint testpad=null;
	
	Paint mpaint=new Paint();
	Rect mrect=new Rect();
	int count=0;
	@Override
	public void onUpdateDraw(Canvas mCanvas) {
		Log.v("TLowView", mWidth+","+mHeight);
//		if(testpad!=null){
//			Log.v("TlowView", "testpad is full");
//		}else{
//			Log.v("TlowView", "testpad is null");
//		}
		int left=count%mWidth;
		int top=count%mHeight;
		mpaint.setColor(0xff00ff00);
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