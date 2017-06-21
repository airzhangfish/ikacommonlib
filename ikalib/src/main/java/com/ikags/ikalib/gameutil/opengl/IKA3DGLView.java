package com.ikags.ikalib.gameutil.opengl;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.MotionEvent;
/**
 * 自定义的openGL 3D View控件
 * 用法:
 * 	IKA3DGLView mGLSurfaceview = (IKA3DGLView) this.findViewById(R.id.iKADGLView1);
 * 	City3DScene mScene = new City3DScene(this);
 * 	mGLSurfaceview.setIKA3DGLRender(mScene);
 * @author zhangxiasheng
 *
 */
public class IKA3DGLView extends GLSurfaceView
{

  public static String TAG = "IKA3DGLView";
  public IKA3DGLRender mRender = null;
  public IKA3DScene mScene = null;
  
  public IKA3DGLView(Context context, AttributeSet attrs)
  {
    super(context, attrs);
  }

  public IKA3DGLView(Context context)
  {
    super(context);
  }


  public void setIKA3DGLRender(IKA3DScene scene){
	  mScene=scene;
	   mRender = new IKA3DGLRender(this.getContext(),mScene);
	    setRenderer(mRender);
    // 设置渲染模式为主动渲染
	    setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);	  
  }
  
  

  public void onPause()
  {
    super.onPause();
  }

  public void onResume()
  {
    super.onResume();
  }


  /**
   * ��Ӧ�����¼�
   */
  @Override
  public boolean onTouchEvent(MotionEvent e)
  {
   if(mScene!=null){
	   return mScene.onSceneTouchEvent(e);
   }
    return false;
  }

  public IKA3DGLRender get3DGLRender()
  {
    return mRender;
  }
}
