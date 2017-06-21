package com.ikags.ikacommonlib.example.lowviews;

import com.ikags.ikalib.gameutil.opengl.IKA3DConfig;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;

public class BufLowView extends IKA2DGameScreenView{
	
	public BufLowView(Context context) {
		super(context);
	}
	public BufLowView(Context context, AttributeSet attrs) {
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
	Bitmap bitmap=null;
	int posX=0;
	@Override
	public void onUpdateDraw(Canvas mCanvas) {
		
		if(bitmap==null){
			try{
			bitmap = BitmapFactory.decodeStream(this.getContext().getAssets().open("opengl_waterfall32.png"));	
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}
		posX++;
		posX=posX%500;
		for(int i=0;i<200;i++){
			mCanvas.drawBitmap(bitmap, posX+i, posX+i, mpaint);
		}
		Log.v("EnvScene_canvas", "fps="+(1000/Math.max(spendtime,1)));
	}
	@Override
	public void onSurfaceDestroyed(SurfaceHolder holder) {
	
		
	}



}
