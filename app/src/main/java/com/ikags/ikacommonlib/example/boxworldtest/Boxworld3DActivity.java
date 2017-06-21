package com.ikags.ikacommonlib.example.boxworldtest;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.ikags.ikalib.gameutil.opengl.IKA3DGLView;
import com.ikags.ikacommonlib.R;

public class Boxworld3DActivity  extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    	requestWindowFeature(Window.FEATURE_NO_TITLE); // 去掉标题
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.acti_opengl3dtest);
        initLayout();
    }
    BoxScene boxScene=null;
    IKA3DGLView mGLSurfaceview=null;
    public void initLayout(){
    	
		mGLSurfaceview = (IKA3DGLView) this.findViewById(R.id.iKADGLView1);
		
		
		boxScene = new BoxScene(this);
		mGLSurfaceview.setIKA3DGLRender(boxScene);
		
		mGLSurfaceview.requestFocus();
		mGLSurfaceview.setFocusableInTouchMode(true);
    }
    

}
