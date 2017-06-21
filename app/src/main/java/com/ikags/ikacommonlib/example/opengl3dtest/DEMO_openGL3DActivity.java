package com.ikags.ikacommonlib.example.opengl3dtest;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.ikags.ikalib.gameutil.opengl.IKA3DGLView;
import com.ikags.ikacommonlib.R;

public class DEMO_openGL3DActivity  extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    	requestWindowFeature(Window.FEATURE_NO_TITLE); // ȥ������
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.acti_opengl3dtest);
        initLayout();
    }
    ModeltestScene mmodelScene=null;
    EnvScene mEnvScene=null;
    AnimeScene aScene=null;
    IKA3DGLView mGLSurfaceview=null;
    public void initLayout(){
    	
		mGLSurfaceview = (IKA3DGLView) this.findViewById(R.id.iKADGLView1);
		

//		mmodelScene = new ModeltestScene(this);
//		mGLSurfaceview.setIKA3DGLRender(mmodelScene);
		
		aScene = new AnimeScene(this);
		mGLSurfaceview.setIKA3DGLRender(aScene);
		
		mGLSurfaceview.requestFocus();
		mGLSurfaceview.setFocusableInTouchMode(true);
		//mGLSurfaceview.setOnTouchListener(otl);
    }
    

}
