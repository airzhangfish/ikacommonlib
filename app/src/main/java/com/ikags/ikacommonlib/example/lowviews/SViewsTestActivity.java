package com.ikags.ikacommonlib.example.lowviews;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

import com.ikags.ikacommonlib.R;

public class SViewsTestActivity extends Activity {
	
	BufLowView iKABufLowSurfaceView1 =null;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v("SViewsTestActivity", "initviewacti_3lowview");
        setContentView(R.layout.acti_3lowview);
        iKABufLowSurfaceView1=(BufLowView)this.findViewById(R.id.iKABufLowSurfaceView1);
        iKABufLowSurfaceView1.setEnableBuffer(320, 240); // 570 ,320
       // iKABufLowSurfaceView1.setOnTouchListener(otl);
    }
    
    
    OnTouchListener otl=new OnTouchListener(){

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			int count=event.getPointerCount();
			StringBuffer sb=new StringBuffer();
			sb.append("count=="+count);
			for(int i=0;i<count;i++){
				sb.append("("+event.getX(i)+","+event.getY(i)+")=");	
			}
			Log.v("onTouchEvent", "sb="+sb.toString());
			return true;
		}
    	
    	
    };
	
}