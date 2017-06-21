package com.ikags.ikalib.gameutil.opengl;

import com.ikags.ikalib.gameutil.opengl.lib.Projector;
import com.ikags.ikalib.gameutil.opengl.lib.Ray;

/**
 * �����������Ƿ�ץȡ�Ĺ�����
 * @author zhangxiasheng
 *
 */
public class PickFactory {
	private static Ray gPickRay = new Ray();
	
	public static Ray getPickRay() {
		return gPickRay;
	}
	
	private static Projector gProjector = new Projector();
	
	private static float[] gpObjPosArray = new float[4];
	
	/**
	 * ����ʰȡ����
	 * @param screenX - ��Ļ����X
	 * @param screenY - ��Ļ����Y
	 */
	public static void update(float screenX, float screenY) {
		IKA3DConfig.gMatView.fillFloatArray(IKA3DConfig.gpMatrixViewArray);
		
		//����OpenGL����ϵԭ��Ϊ���½ǣ�����������ϵԭ��Ϊ���Ͻ�
		//��ˣ���OpenGl�е�YӦ����Ҫ�õ�ǰ�ӿڸ߶ȣ���ȥ��������Y
		float openglY = IKA3DConfig.gpViewport[3] - screenY;
		//z = 0 , �õ�P0
		gProjector.gluUnProject(screenX, openglY, 0.0f, IKA3DConfig.gpMatrixViewArray, 0, 
				IKA3DConfig.gpMatrixProjectArray, 0, IKA3DConfig.gpViewport, 0, gpObjPosArray, 0);
		//�������ԭ��P0
		
		gPickRay.mvOrigin.set(gpObjPosArray[0], gpObjPosArray[1], gpObjPosArray[2]);
		
		//z = 1 ���õ�P1
		gProjector.gluUnProject(screenX, openglY, 1.0f, IKA3DConfig.gpMatrixViewArray, 0, 
				IKA3DConfig.gpMatrixProjectArray, 0, IKA3DConfig.gpViewport, 0, gpObjPosArray, 0);
		//�������ߵķ���P1 - P0
		gPickRay.mvDirection.set(gpObjPosArray[0], gpObjPosArray[1], gpObjPosArray[2]);
		gPickRay.mvDirection.sub(gPickRay.mvOrigin);
		//������һ��
		gPickRay.mvDirection.normalize();
	}
}
