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
import android.opengl.GLUtils;
import android.util.Log;
import android.view.MotionEvent;

import com.ikags.ikalib.gameutil.opengl.IKA3DConfig;
import com.ikags.ikalib.gameutil.opengl.IKA3DScene;

public class AnimeScene extends IKA3DScene {

	Activity acti = null;

	public AnimeScene(Activity act) {
		acti = act;
	}

	@Override
	public void onSceneCreated(GL10 gl, EGLConfig config) {

	}

	int posX = 0;
	@Override
	public void onSceneDrawFrame(GL10 gl) {
		posX++;
		posX=posX%200;
		onDraw2D(gl, posX);
		
//		posX++;
//		posX = posX % 500;
//		for (int i = 0; i < 200; i++) {
//			onDraw2D(gl, posX + i);
//		}

		//Log.v("EnvScene_opengl", "fps=" + (1000 / Math.max(IKA3DConfig.gDrawFrameTime, 1)));
	}

	FloatBuffer vertices;
	FloatBuffer texture;
	ShortBuffer indices;
	int textureId = -1;
	public void onDraw2D(GL10 gl, int offset) {
		if (textureId == -1) {
			init2D();
			textureId = loadTexture("opengl_waterfall32.png", gl);
		}

		float mmx = 512f;
		float mmy = 256f;
		float offx = 0;
		float offy = 0;
		vertices.clear();
		vertices.put(new float[]{0 + offx, 0 + offy, mmx + offx, 0 + offy, 0 + offx, mmy + offy, mmx+ offx, mmy + offy}, 0, 8);
		vertices.position(0);

		// ������ʾ����Ļ�ϵ�ʲôλ��(opengl �Զ�ת��)
		// gl.glViewport(0, 0, 400, 240);
		// gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();
		// gl.glOrthof(-80f, 80f, -60f, 60f, 1, -1);
		gl.glOrthof(0.0f, 320f, 0.0f, 240f, 1, -1);

		gl.glEnable(GL10.GL_TEXTURE_2D);
		// ������ID
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textureId);

		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);

		gl.glVertexPointer(2, GL10.GL_FLOAT, 0, vertices);

		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, texture);
		// gl.glRotatef(1, 0, 1, 0);

		gl.glDrawElements(GL10.GL_TRIANGLE_STRIP, 6, GL10.GL_UNSIGNED_SHORT, indices);
	}

	public void init2D() {
		ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4 * 2 * 4);
		byteBuffer.order(ByteOrder.nativeOrder());
		vertices = byteBuffer.asFloatBuffer();
		// vertices.put( new float[] { -80f, -120f,0,1f,
		// 80f, -120f, 1f,1f,
		// -80f, 120f, 0f,0f,
		// 80f,120f, 1f,0f});
		vertices.put(new float[]{-80f, -120f, 80f, -120f, -80f, 120f, 80f, 120f});

		
		//����˳��(�̶�)
		ByteBuffer indicesBuffer = ByteBuffer.allocateDirect(6 * 2);
		indicesBuffer.order(ByteOrder.nativeOrder());
		indices = indicesBuffer.asShortBuffer();
		indices.put(new short[]{0, 1, 2, 1, 2, 3});

		
		//��ͼλ��(С���λ��)
		ByteBuffer textureBuffer = ByteBuffer.allocateDirect(4 * 2 * 4);
		textureBuffer.order(ByteOrder.nativeOrder());
		texture = textureBuffer.asFloatBuffer();
		texture.put(new float[]{0, 1f, 0.5f, 1f, 0f, 0f, 0.5f, 0f});

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
			//gl.glBindTexture(GL10.GL_TEXTURE_2D, 0);
			bitmap.recycle();
			return textureId;
		} catch (Exception e) {
			throw new RuntimeException("couldn't load asset '" + fileName + "'");
		}
	}

	@Override
	public boolean onSceneTouchEvent(MotionEvent e) {

		return true;
	}

}
