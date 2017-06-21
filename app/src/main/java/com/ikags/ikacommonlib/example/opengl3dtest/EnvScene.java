package com.ikags.ikacommonlib.example.opengl3dtest;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLU;
import android.opengl.GLUtils;
import android.util.Log;
import android.view.MotionEvent;

import com.ikags.ikalib.gameutil.opengl.IKA3DConfig;
import com.ikags.ikalib.gameutil.opengl.IKA3DScene;
import com.ikags.ikalib.gameutil.opengl.IKA3DUtil;
import com.ikags.ikalib.gameutil.opengl.lib.Matrix4f;
import com.ikags.ikalib.gameutil.opengl.lib.Vector3f;
import com.ikags.ikalib.gameutil.opengl.ms3d.IMS3DModel;
import com.ikags.ikacommonlib.R;


public class EnvScene extends IKA3DScene {


	Activity acti = null;
	
	public EnvScene(Activity act) {
		acti = act;
	}

    
	@Override
	public void onSceneCreated(GL10 gl, EGLConfig config) {

	}
    private Vector3f mvEye = new Vector3f(), mvCenter = new Vector3f(), mvUp = new Vector3f(0, 1, 0);
	Matrix4f matRotX = new Matrix4f();
	public static Matrix4f gMatModel = new Matrix4f();
	IMS3DModel m3dmodel=null;
	float count=0;
	int  posX=0;
	@Override
	public void onSceneDrawFrame(GL10 gl) {
		//onDraw3D(gl);
		posX++;
		posX=posX%500;
		for(int i=0;i<200;i++){
			onDraw2D(gl,posX+i);	
		}

	Log.v("EnvScene_opengl", "fps="+(1000/Math.max(IKA3DConfig.gDrawFrameTime,1)));
	}
	
	
	public void onDraw3D(GL10 gl){
		count=count+1f;
		if(count>100f){
			count=-100f;
		}
		if(m3dmodel==null){
			m3dmodel = IKA3DUtil.loadModel(acti, gl, R.raw.env, new int[]{R.raw.dimian, R.raw.hai, R.raw.shanmai, R.raw.tree});
		}
		
		if(m3dmodel!=null){
            float far=m3dmodel.getSphereRadius()*Math.abs(mLookY/100);
			double dis_angle=0/(31.4*3);
			//TODO
			mvEye.x=(float)(far*Math.sin(dis_angle));
			mvEye.y=far;
			mvEye.z=(float)(far*Math.cos(dis_angle));
    		mvCenter.x = 0;
    	    mvCenter.z = 0;
    		mvCenter.y = 0;
    		IKA3DUtil.setCamera(gl, mvEye, mvCenter, mvUp);
			}
		
		
		//��ģ��
		gl.glPushMatrix();
		matRotX.setIdentity();
		gMatModel.set(matRotX);
		gl.glMultMatrixf(gMatModel.asFloatBuffer());
	   IKA3DUtil.drawModel(gl,m3dmodel, 3f);	

		gl.glPopMatrix();

	}
	
	
    FloatBuffer vertices;  
    FloatBuffer texture;  
    ShortBuffer indices;  
	int textureId=-1;
    public void onDraw2D(GL10 gl,int offset) {  
    	if(textureId==-1){
    		init2D();
        textureId = loadTexture("opengl_waterfall32.png",gl);  
    	}
    	
    	float mmx=80f;
    	float mmy=80f;
    	float offx=offset;
    	float offy=offset;
    	vertices.clear();
        vertices.put( new float[] {  0+offx,   0+offy,  
        		mmx+offx,  0+offy,   
                0+offx, mmy+offy,  
                mmx+offx,  mmy+offy},0,8);  
        vertices.position(0);  

        //������ʾ����Ļ�ϵ�ʲôλ��(opengl �Զ�ת��)  
       // gl.glViewport(0, 0, 400, 240);  
       // gl.glClear(GL10.GL_COLOR_BUFFER_BIT);  
        gl.glMatrixMode(GL10.GL_PROJECTION);  
        gl.glLoadIdentity();  
       // gl.glOrthof(-80f, 80f, -60f, 60f, 1, -1);  
        gl.glOrthof(0.0f,320f,0.0f,240f,1,-1);  

        gl.glEnable(GL10.GL_TEXTURE_2D);  
    	//������ID  
                    gl.glBindTexture(GL10.GL_TEXTURE_2D, textureId);  

        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);  
        gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);  

        gl.glVertexPointer(2, GL10.GL_FLOAT, 0, vertices);  

        gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, texture);  
        // gl.glRotatef(1, 0, 1, 0);  
        
        gl.glDrawElements(GL10.GL_TRIANGLE_STRIP, 6,   GL10.GL_UNSIGNED_SHORT, indices);  
    }  
    
    
    
    public void init2D(){
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4 * 2 * 4);  
        byteBuffer.order(ByteOrder.nativeOrder());              
        vertices = byteBuffer.asFloatBuffer();  
//        vertices.put( new float[] {  -80f,   -120f,0,1f,  
//                                     80f,  -120f, 1f,1f,  
//                                     -80f, 120f, 0f,0f,  
//                                     80f,120f,   1f,0f});  
        vertices.put( new float[] {  -80f,   -120f,  
                                     80f,  -120f,   
                                     -80f, 120f,  
                                     80f,  120f});  
 
        ByteBuffer indicesBuffer = ByteBuffer.allocateDirect(6 * 2);   
        indicesBuffer.order(ByteOrder.nativeOrder());   
        indices = indicesBuffer.asShortBuffer();  
        indices.put(new short[] { 0, 1, 2,1,2,3});  
          
        ByteBuffer textureBuffer = ByteBuffer.allocateDirect(4 * 2 * 4);  
        textureBuffer.order(ByteOrder.nativeOrder());              
        texture = textureBuffer.asFloatBuffer();  
        texture.put( new float[] { 0,1f,  
                                    1f,1f,  
                                    0f,0f,  
                                    1f,0f});  
          
        indices.position(0);  
        vertices.position(0);  
        texture.position(0);  
    }
	
	
	public int loadTexture(String fileName, GL10 gl) {
		try {
			Bitmap bitmap = BitmapFactory.decodeStream(acti.getAssets().open(fileName));
			int textureIds[] = new int[1];
			gl.glGenTextures(1, textureIds, 0);
			int textureId = textureIds[0];
			gl.glBindTexture(GL10.GL_TEXTURE_2D, textureId);
			GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
			gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
			gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_NEAREST);
			gl.glBindTexture(GL10.GL_TEXTURE_2D, 0);
			bitmap.recycle();
			return textureId;
		} catch (Exception e) {
			throw new RuntimeException("couldn't load asset '" + fileName + "'");
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
