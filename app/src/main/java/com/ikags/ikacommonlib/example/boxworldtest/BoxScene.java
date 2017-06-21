package com.ikags.ikacommonlib.example.boxworldtest;

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


public class BoxScene extends IKA3DScene {


	Activity acti = null;
	
	public BoxScene(Activity act) {
		acti = act;
	}


	//0是不绘制,1草地,2沙子,3海,4树干,5树叶,6房子
	public int[][][] myboxworld={
		   {	{2,2,2,2,2,2,2,2,2,2},
				{2,2,2,2,2,2,2,2,2,2},
				{2,2,2,2,2,2,2,2,2,3},
				{2,2,2,2,2,2,2,2,3,3},
				{2,2,2,2,2,2,2,3,3,2},
				{2,2,2,2,2,2,3,3,2,2},
				{2,2,2,2,2,3,3,2,2,2},
				{2,2,2,2,3,3,2,2,2,2},
				{2,2,2,3,3,2,2,2,2,2},
				{2,2,3,3,2,2,2,2,2,2}},
			
			   {{1,1,1,1,1,1,1,1,1,1},
				{1,1,1,1,1,1,1,1,1,0},
				{1,1,1,1,1,1,1,1,2,0},
				{1,1,1,1,1,1,1,2,0,0},
				{1,1,1,1,1,1,0,0,0,0},
				{1,1,1,1,1,0,0,0,0,1},
				{1,1,1,1,0,0,0,2,1,1},
				{1,1,1,2,0,0,3,1,1,1},
				{1,1,2,0,0,2,1,1,1,1},
				{1,2,0,0,0,1,1,1,1,1}},
				
				   {{0,0,0,0,0,0,0,0,0,0},
					{0,0,0,0,0,0,6,0,0,0},
					{0,0,0,0,0,0,0,0,0,0},
					{0,0,0,4,0,0,0,0,0,0},
					{0,0,0,0,0,0,0,0,0,0},
					{0,0,0,0,0,0,0,0,0,0},
					{0,0,0,0,0,0,1,1,1,0},
					{0,0,0,0,0,0,3,1,1,0},
					{0,0,0,0,0,0,1,1,1,0},
					{0,0,0,0,0,0,0,0,0,0}},
					
					   {{0,0,0,0,0,0,0,0,0,0},
						{0,0,0,0,0,0,0,0,0,0},
						{0,0,0,0,0,0,0,0,0,0},
						{0,0,0,4,0,0,0,0,0,0},
						{0,0,0,0,0,0,0,0,0,0},
						{0,0,0,0,0,0,0,0,0,0},
						{0,0,0,0,0,0,0,1,0,0},
						{0,0,0,0,0,0,3,1,1,0},
						{0,0,0,0,0,0,0,1,0,0},
						{0,0,0,0,0,0,0,0,0,0}},

							   {{0,0,0,0,0,0,0,0,0,0},
								{0,0,0,0,0,0,0,0,0,0},
								{0,0,0,5,0,0,0,0,0,0},
								{0,0,5,5,5,0,0,0,0,0},
								{0,0,0,5,0,0,0,0,0,0},
								{0,0,0,0,0,0,0,0,0,0},
								{0,0,0,0,0,0,0,0,0,0},
								{0,0,0,0,0,0,0,3,0,0},
								{0,0,0,0,0,0,0,0,0,0},
								{0,0,0,0,0,0,0,0,0,0}},
								
								   {{0,0,0,0,0,0,0,0,0,0},
									{0,0,0,0,0,0,0,0,0,0},
									{0,0,5,5,5,0,0,0,0,0},
									{0,0,5,5,5,0,0,0,0,0},
									{0,0,5,5,5,0,0,0,0,0},
									{0,0,0,0,0,0,0,0,0,0},
									{0,0,0,0,0,0,0,0,0,0},
									{0,0,0,0,0,0,0,0,0,0},
									{0,0,0,0,0,0,0,0,0,0},
									{0,0,0,0,0,0,0,0,0,0}},
									
									   {{0,0,0,0,0,0,0,0,0,0},
										{0,0,0,0,0,0,0,0,0,0},
										{0,0,0,5,0,0,0,0,0,0},
										{0,0,5,5,5,0,0,0,0,0},
										{0,0,0,5,0,0,0,0,0,0},
										{0,0,0,0,0,0,0,0,0,0},
										{0,0,0,0,0,0,0,0,0,0},
										{0,0,0,0,0,0,0,0,0,0},
										{0,0,0,0,0,0,0,0,0,0},
										{0,0,0,0,0,0,0,0,0,0}},
										
										   {{0,0,0,0,0,0,0,0,0,0},
											{0,0,0,0,0,0,0,0,0,0},
											{0,0,0,0,0,0,0,0,0,0},
											{0,0,0,5,0,0,0,0,0,0},
											{0,0,0,0,0,0,0,0,0,0},
											{0,0,0,0,0,0,0,0,0,0},
											{0,0,0,0,0,0,0,0,0,0},
											{0,0,0,0,0,0,0,0,0,0},
											{0,0,0,0,0,0,0,0,0,0},
											{0,0,0,0,0,0,0,0,0,0}},
	};
	
	
	@Override
	public void onSceneCreated(GL10 gl, EGLConfig config) {

	}
    private Vector3f mvEye = new Vector3f(), mvCenter = new Vector3f(), mvUp = new Vector3f(0, 1, 0);
	Matrix4f matRotX = new Matrix4f();
	public static Matrix4f gMatModel = new Matrix4f();
	IMS3DModel m3dmodel=null;
	IMS3DModel[] objtypes=null;  //
	float count=0;
	@Override
	public void onSceneDrawFrame(GL10 gl) {

		count=count+1f;
		if(count>360f){
			count=0f;
		}
		if(m3dmodel==null){
			m3dmodel = IKA3DUtil.loadModel(acti, gl, R.raw.basebox_test, new int[]{R.raw.basebox_uv});
			objtypes=new IMS3DModel[10]; //0是不绘制,1草地,2沙子,3海,4树干,5树叶
			objtypes[0] = IKA3DUtil.loadModel(acti, gl, R.raw.basebox_test, new int[]{R.raw.basebox_uv});
			objtypes[1] = IKA3DUtil.loadModel(acti, gl, R.raw.basebox_test, new int[]{R.raw.basebox_land});
			objtypes[2] = IKA3DUtil.loadModel(acti, gl, R.raw.basebox_test, new int[]{R.raw.basebox_sand});
			objtypes[3] = IKA3DUtil.loadModel(acti, gl, R.raw.basebox_test, new int[]{R.raw.basebox_sea});
			objtypes[4] = IKA3DUtil.loadModel(acti, gl, R.raw.basebox_test, new int[]{R.raw.basebox_wood});
			objtypes[5] = IKA3DUtil.loadModel(acti, gl, R.raw.basebox_test, new int[]{R.raw.basebox_leaf});
			objtypes[6] = IKA3DUtil.loadModel(acti, gl, R.raw.m3d0_chengbao, new int[]{R.raw.uv0_chengbao});
		}
		
		
		if(m3dmodel!=null){
            float far=m3dmodel.getSphereRadius()*Math.abs(mLookY/100);
			double dis_angle=0/(31.4*3);
			//TODO
//			mvEye.x=(float)(far*Math.sin(dis_angle));
//			mvEye.y=far;
//			mvEye.z=(float)(far*Math.cos(dis_angle));
			double pix=count*0.1f/(2*3.1415926f);
			mvEye.x=(float)(200*Math.sin(pix))+100;       //平移
			mvEye.z=(float)(200*Math.cos(pix))+100;      //前后
			
			
			mvEye.y=150;           //高度
			
    		mvCenter.x = 100;
    	    mvCenter.z = 100;

    		mvCenter.y = 0;
    		IKA3DUtil.setCamera(gl, mvEye, mvCenter, mvUp);
			}
	
		
		float scalesize=2f;
		float baseboxwidth=10f;
		float offwitdth=baseboxwidth*scalesize;
		//新模型
//		gl.glPushMatrix();
//		matRotX.setIdentity();
//		gMatModel.set(matRotX);
//		gl.glMultMatrixf(gMatModel.asFloatBuffer());
//	   IKA3DUtil.drawModel(gl,m3dmodel, scalesize);	
//
//		gl.glPopMatrix();


		for(int py=0;py<myboxworld.length;py++){
			for(int pz=0;pz<myboxworld[py].length;pz++){
				for(int px=0;px<myboxworld[py][pz].length;px++){
					int posid=myboxworld[py][pz][px];
					if(posid>0){
						gl.glPushMatrix();
						matRotX.setIdentity();
						matRotX.setTranslation(offwitdth*px, offwitdth*py, offwitdth*pz);
						gMatModel.set(matRotX);
						gl.glMultMatrixf(gMatModel.asFloatBuffer());
						if(posid==6){
							  IKA3DUtil.drawModel(gl,objtypes[posid], 1);	
						}else{
							   IKA3DUtil.drawModel(gl,objtypes[posid], scalesize);		
						}
						gl.glPopMatrix();	
					}
				}
			}
		}
		
		
	}
	


	public float mLookX = 0;
	public float mLookY = 100;
	public float mLookZ = 0;

	

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
