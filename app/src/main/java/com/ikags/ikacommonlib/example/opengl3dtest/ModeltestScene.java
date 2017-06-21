package com.ikags.ikacommonlib.example.opengl3dtest;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.app.Activity;
import android.util.Log;
import android.view.MotionEvent;

import com.ikags.ikalib.gameutil.opengl.IKA3DScene;
import com.ikags.ikalib.gameutil.opengl.IKA3DUtil;
import com.ikags.ikalib.gameutil.opengl.lib.Matrix4f;
import com.ikags.ikalib.gameutil.opengl.lib.Vector3f;
import com.ikags.ikalib.gameutil.opengl.ms3d.IMS3DModel;
import com.ikags.ikacommonlib.R;


public class ModeltestScene extends IKA3DScene {


	public int SELECTTYPE = 0;
	public static final int SELECTTYPE_MAP = 0;
	public static final int SELECTTYPE_CASLE = 1;
	Activity acti = null;
	public static float defalutCasleEyeX=-165;
	public static float defalutCasleEyeY=-65;
	
	public ModeltestScene(Activity act) {
		acti = act;
	}

    public static  int[] m3d_buildingsID={R.raw.bachang,R.raw.yuan,R.raw.shui,R.raw.shui1};
    public static  int[] m3d_buildingsResID={R.raw.uv5_bachang,R.raw.uv_mukuang,R.raw.uv_shui,R.raw.uv_shui};
    
	@Override
	public void onSceneCreated(GL10 gl, EGLConfig config) {
	}
    private Vector3f mvEye = new Vector3f(), mvCenter = new Vector3f(), mvUp = new Vector3f(0, 1, 0);
	Matrix4f matRotX = new Matrix4f();
	public static Matrix4f gMatModel = new Matrix4f();
	public boolean isRotion=true;
	IMS3DModel m3dmodel=null;
	float count=0;
	@Override
	public void onSceneDrawFrame(GL10 gl) {

		count=count+0.1f;
		if(m3dmodel==null){
			m3dmodel = IKA3DUtil.loadModel(acti, gl, m3d_buildingsID[0], new int[]{m3d_buildingsResID[0]});	
		}
		
		if(m3dmodel!=null){
            float far=m3dmodel.getSphereRadius()*Math.abs(mLookY/100);
			double dis_angle=mLookX/(31.4*3);
			//TODO
			mvEye.x=(float)(far*Math.sin(dis_angle));
			mvEye.y=far;
			mvEye.z=(float)(far*Math.cos(dis_angle));

    		mvCenter.x = 0;
    	    mvCenter.z = 0;
    		mvCenter.y = 0;
    		IKA3DUtil.setCamera(gl, mvEye, mvCenter, mvUp);
    		Log.v("mvEye", mvEye.x+","+mvEye.y);
			}
	
		
		
		//��ģ��
		gl.glPushMatrix();
		matRotX.setIdentity();
		gMatModel.set(matRotX);
		gl.glMultMatrixf(gMatModel.asFloatBuffer());
	   IKA3DUtil.drawModel(gl,m3dmodel, 1f);	

		gl.glPopMatrix();


	}
	

	
	


	public float mLookX = 0;
	public float mLookY = 100;
	public float mLookZ = 0;

	public float mMapX = 0;
	public float mMapY = 0;
	public float mMapZ = 0;
	public float mMapRotX = 0;
	public int mMapRotTypeX = 0;
	public static final int ROTTYPE_NORMAL = 0;
	public static final int ROTTYPE_LEFT = 1;
	public static final int ROTTYPE_RIGHT = 2;

	public int mLookType = 0;

	float mX_down = 0;
	float mY_down = 0;
	float mX_move = 0;
	float mY_move = 0;
	float mX_up = 0;
	float mY_up = 0;
	float buf_mX = 0;
	float buf_mY = 0;
	@Override
	public boolean onSceneTouchEvent(MotionEvent e) {
			Log.v("City3DScene", "MOVE_SCREEN=" + mLookX + "," + mLookY + "," + mLookZ + ",action=" + e.getAction() + "(" + e.getX() + "," + e.getY() + ")");
			switch (e.getAction()) {
				case MotionEvent.ACTION_DOWN :
					mX_down = e.getX();
					mY_down = e.getY();
					buf_mX = mLookX;
					buf_mY = mLookY;
				break;
				case MotionEvent.ACTION_MOVE :
					mX_move = e.getX();
					mY_move = e.getY();
					mLookX = buf_mX + mX_move - mX_down;
					mLookY = buf_mY + mY_move - mY_down;
				break;
				case MotionEvent.ACTION_UP :
					mX_up = e.getX();
					mY_up = e.getY();
				break;
				case MotionEvent.ACTION_CANCEL :
				break;
			}
		return true;
	}
	

	
}
