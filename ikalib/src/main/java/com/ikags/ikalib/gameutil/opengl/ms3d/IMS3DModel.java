/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikags.ikalib.gameutil.opengl.ms3d;

import java.io.InputStream;
import java.nio.FloatBuffer;
import javax.microedition.khronos.opengles.GL10;

import com.ikags.ikalib.gameutil.opengl.lib.IBufferFactory;
import com.ikags.ikalib.gameutil.opengl.lib.Matrix4f;
import com.ikags.ikalib.gameutil.opengl.lib.Quat4f;
import com.ikags.ikalib.gameutil.opengl.lib.Ray;
import com.ikags.ikalib.gameutil.opengl.lib.TextureInfo;
import com.ikags.ikalib.gameutil.opengl.lib.Vector3f;
import com.ikags.ikalib.gameutil.opengl.lib.Vector4f;

/**
 *
 * @author Yong.Xue
 */
public class IMS3DModel {
	public static final String MODEL_TYPE_MS3D = "ms3d";
	public String mName = "";
	public MS3DHeader mHeader;
	public MS3DVertex[] mpVertices;
	public MS3DTriangle[] mpTriangles;
	public MS3DGroup[] mpGroups;
	public MS3DMaterial[] mpMaterials;
	public Joint[] mpJoints;
	public float mTotalTime;
	public float mCurrentTime;
	public float mFps;
	public int mNumFrames;
	public int mNumPrimitives;
	private String mStrComment;
	private FloatBuffer[] mpBufVertices;

	private FloatBuffer[] mpBufColor;
	private FloatBuffer[] mpBufTextureCoords;

	private Vector3f mvMax = new Vector3f(), mvMin = new Vector3f(),
			mvCenter = new Vector3f();
	private float mfRadius;

	private boolean mbInitBoundingBox;

	private TextureInfo[] mpTexInfo;

	private boolean mbDirtFlag = false;

	private FloatBuffer mBufJointLinePosition;
	private FloatBuffer mBufJointPointPosition;
	private int mJointPointCount, mJointLineCount;

	public String getComment() {
		return mStrComment;
	}

	public void setComment(String comment) {
		this.mStrComment = comment;
	}

	public void setTexture(TextureInfo[] pTexInfo) {
		mpTexInfo = pTexInfo;
	}

	public boolean loadModel(InputStream is) {
		IMS3DLoader loader = new IMS3DLoader();
		boolean resultOK = loader.Load(is, this);

		if (!resultOK) {
			return false;
		}

		mCurrentTime = 0.0f;
		mTotalTime = mNumFrames / mFps;

		// m_bufIndexes = new IntBuffer[m_groups.length];
		mpBufVertices = new FloatBuffer[mpGroups.length];
		mpBufColor = new FloatBuffer[mpGroups.length];
		mpBufTextureCoords = new FloatBuffer[mpGroups.length];
		//mpBufVerticesExpand = new FloatBuffer[mpGroups.length];

		for (int i = 0; i < mpGroups.length; i++) {
			mpBufColor[i] = IBufferFactory.newFloatBuffer(mpGroups[i]
					.getTriangleCount() * 3 * 4);
			mpBufVertices[i] = IBufferFactory.newFloatBuffer(mpGroups[i]
					.getTriangleCount() * 3 * 3);
			mpBufTextureCoords[i] = IBufferFactory.newFloatBuffer(mpGroups[i]
					.getTriangleCount() * 3 * 2);
			//mpBufVerticesExpand[i] = ILib.newFloatBuffer(mpGroups[i]
			//.getTriangleCount() * 3 * 3);

			for (int j = 0; j < mpGroups[i].getTriangleCount(); j++) {
				// fill
				MS3DTriangle triangle = mpTriangles[mpGroups[i]
						.getTriangleIndicies()[j]];

				for (int k = 0; k < 3; k++) {
					mpBufTextureCoords[i].put(triangle.getS()[k]);
					mpBufTextureCoords[i].put(triangle.getT()[k]);
				}
			}
			mpBufTextureCoords[i].rewind();
		}


		mbDirtFlag = true;
		animate(0.0f);
		mbInitBoundingBox = true;
		fillRenderBuffer();
		mbInitBoundingBox = false;
		return true;
	}

	private void updateJointsHelper() {
		if(!containsJoint()) {
			return;
		}
		if(mBufJointPointPosition == null) {
			mJointPointCount = mpJoints.length;
			mBufJointPointPosition = IBufferFactory.newFloatBuffer(mpJoints.length * 3);
		}
		mBufJointPointPosition.position(0);

		for(int i = 0, n = mpJoints.length; i < n; i++) {
			Joint joint = mpJoints[i];

			float x = joint.mMatGlobal.m03;
			float y = joint.mMatGlobal.m13;
			float z = joint.mMatGlobal.m23;

			mBufJointPointPosition.put(x);
			mBufJointPointPosition.put(y);
			mBufJointPointPosition.put(z);
		}

		mBufJointPointPosition.position(0);

		//fill joint line buffer
		if(mBufJointLinePosition == null) {
			mJointLineCount = mpJoints.length * 2;
			mBufJointLinePosition = IBufferFactory.newFloatBuffer(mpJoints.length * 2 * 3);
		}
		mBufJointLinePosition.position(0);

		for(int i = 0, n = mpJoints.length; i < n; i++) {
			Joint joint = mpJoints[i];

			float x0, y0, z0;
			float x1, y1, z1;
			x0 = joint.mMatGlobal.m03;
			y0 = joint.mMatGlobal.m13;
			z0 = joint.mMatGlobal.m23;
			if(joint.mParentId == -1) {
				//no parent
				x1 = x0;
				y1 = y0;
				z1 = z0;
			} else {
				joint = mpJoints[joint.mParentId];
				x1 = joint.mMatGlobal.m03;
				y1 = joint.mMatGlobal.m13;
				z1 = joint.mMatGlobal.m23;
			}

			mBufJointLinePosition.put(x0);
			mBufJointLinePosition.put(y0);
			mBufJointLinePosition.put(z0);

			mBufJointLinePosition.put(x1);
			mBufJointLinePosition.put(y1);
			mBufJointLinePosition.put(z1);
		}

		mBufJointLinePosition.position(0);
	}

	/**
	 * 根据时间来更新模型动画
	 *
	 * @param timedelta - 本次tick时间
	 */
	public void animate(float timedelta) {
		//累加时间
		mCurrentTime += timedelta;

		if (mCurrentTime > mTotalTime) {
			mCurrentTime = 0.0f;
		}
		//首先要更新每个骨骼节点的当前位置信息
		for (int i = 0; i < mpJoints.length; i++) {
			Joint joint = mpJoints[i];
			//如果不包含动画信息那就无需更新
			if (joint.mNumTranslationKeyframes == 0
					&& joint.mNumRotationKeyframes == 0) {
				joint.mMatGlobal.set(joint.mMatJointAbsolute);
				continue;
			}

			//开始进行插值计算
			//首先进行旋转插值
			Matrix4f matKeyframe = getJointRotation(i, mCurrentTime);
			//进行偏移的线性插值
			matKeyframe.setTranslation(getJointTranslation(i, mCurrentTime));
			//乘以节点本身的相对矩阵
			matKeyframe.mul(joint.mMatJointRelative, matKeyframe);

			//乘以父矩阵，得到最终矩阵
			if (joint.mParentId == -1) {
				joint.mMatGlobal.set(matKeyframe);
			} else {
				matKeyframe.mul(mpJoints[joint.mParentId].mMatGlobal,
						matKeyframe);
				joint.mMatGlobal.set(matKeyframe);
			}
		}
		//更新点线渲染的骨骼帮助信息
		updateJointsHelper();

		//开始更新每个顶点
		for (int i = 0, n = mpVertices.length; i < n; i++) {
			MS3DVertex vertex = mpVertices[i];

			if (vertex.getBoneID() == -1) {
				//如果该顶点不受骨骼影响，那么就无需计算
				vertex.mvTransformedLocation.set(vertex.getLocation());
			} else {
				//通过骨骼运算，得到顶点的当前位置
				transformVertex(vertex);
			}
		}

		mbDirtFlag = true;
	}

	static Matrix4f tmpMatrixJointRotation = new Matrix4f();

	private Matrix4f getJointRotation(int jointIndex, float time) {
		Quat4f quat = lerpKeyframeRotate(
				mpJoints[jointIndex].mpRotationKeyframes, time);

		Matrix4f matRot = tmpMatrixJointRotation;
		matRot.set(quat);
		return matRot;
	}

	static Quat4f tmpQuatLerp = new Quat4f();
	static Quat4f tmpQuatLerpLeft = new Quat4f(), tmpQuatLerpRight = new Quat4f();

	/**
	 * 根据传入的时间，计算插值后的旋转量
	 *
	 * @param frames
	 *            旋转量关键帧数组
	 * @param time
	 *            目标时间
	 * @return 插值后的旋转量数据
	 */
	private Quat4f lerpKeyframeRotate(Keyframe[] frames, float time) {
		Quat4f quat = tmpQuatLerp;
		int frameIndex = 0;
		int numFrames = frames.length;

		//这里可以使用二分查找进行优化
		while (frameIndex < numFrames && frames[frameIndex].mfTime < time) {
			++frameIndex;
		}
		//首先处理边界情况
		if (frameIndex == 0) {
			quat.set(frames[0].mvParam);
		} else if (frameIndex == numFrames) {
			quat.set(frames[numFrames - 1].mvParam);
		} else {
			int prevFrameIndex = frameIndex - 1;
			//找到最邻近的两帧
			Keyframe right = frames[frameIndex];
			Keyframe left = frames[prevFrameIndex];
			//计算好插值因子
			float timeDelta = right.mfTime - left.mfTime;
			float interpolator = (time - left.mfTime) / timeDelta;
			//进行四元数插值
			Quat4f quatRight = tmpQuatLerpRight;
			Quat4f quatLeft = tmpQuatLerpLeft;

			quatRight.set(right.mvParam);
			quatLeft.set(left.mvParam);
			quat.interpolate(quatLeft, quatRight, interpolator);
		}

		return quat;
	}

	private Vector3f getJointTranslation(int jointIndex, float time) {
		Vector3f translation = lerpKeyframeLinear(
				mpJoints[jointIndex].mpTranslationKeyframes, time);

		return translation;
	}

	static Vector3f tmpVectorLerp = new Vector3f();

	/**
	 * 根据传入的时间，返回插值后的位置信息
	 *
	 * @param frames
	 *            偏移量关键帧数组
	 * @param time
	 *            目标时间
	 * @return 插值后的位置信息
	 */
	private Vector3f lerpKeyframeLinear(Keyframe[] frames, float time) {
		int frameIndex = 0;
		int numFrames = frames.length;

		//这里可以使用二分查找进行优化
		while (frameIndex < numFrames && frames[frameIndex].mfTime < time) {
			++frameIndex;
		}

		//首先处理边界情况
		Vector3f parameter = tmpVectorLerp;
		if (frameIndex == 0) {
			parameter.set(frames[0].mvParam.x, frames[0].mvParam.y,
					frames[0].mvParam.z);
		} else if (frameIndex == numFrames) {
			parameter.set(frames[numFrames - 1].mvParam.x,
					frames[numFrames - 1].mvParam.y,
					frames[numFrames - 1].mvParam.z);
		} else {
			int prevFrameIndex = frameIndex - 1;
			//得到临近两帧
			Keyframe right = frames[frameIndex];
			Keyframe left = frames[prevFrameIndex];
			//计算插值因子
			float timeDelta = right.mfTime - left.mfTime;
			float interpolator = (time - left.mfTime) / timeDelta;
			//进行简单的线性插值
			parameter.interpolate(left.mvParam, right.mvParam, interpolator);
		}

		return parameter;
	}

	/**
	 * 填充渲染缓存数据
	 */
	public void fillRenderBuffer() {
		if(!mbDirtFlag) {
			//如果模型数据没有更新，那么就无需重新填充
			return;
		}
		Vector3f position = null;
		//遍历所有Group
		for (int i = 0; i < mpGroups.length; i++) {
			//获得该Group内所有的三角形索引
			int[] indexes = mpGroups[i].getTriangleIndicies();
			mpBufVertices[i].position(0);
			int vertexIndex = 0;
			//遍历每一个三角形
			for (int j = 0; j < indexes.length; j++) {
				//从三角形池内找到对应三角形
				MS3DTriangle triangle = mpTriangles[indexes[j]];
				//遍历三角形的每个顶点
				for (int k = 0; k < 3; k++) {
					//从顶点池中找到相应顶点
					MS3DVertex vertex = mpVertices[triangle
							.getVertexIndicies()[k]];
					//获得最新的位置
					//如果模型带骨骼，那么就是当前的变换后的位置
					//否则就是初始位置
					//具体的变换过程请参考animate(float timedelta)函数
					position = vertex.mvTransformedLocation;
					//填充顶点位置信息到缓存中
					mpBufVertices[i].put(position.x);
					mpBufVertices[i].put(position.y);
					mpBufVertices[i].put(position.z);


					if (mbInitBoundingBox) {
						//计算模型绑定框，仅在模型载入时启用
						mvMin.x = Math.min(mvMin.x, position.x);
						mvMin.y = Math.min(mvMin.y, position.y);
						mvMin.z = Math.min(mvMin.z, position.z);

						mvMax.x = Math.max(mvMax.x, position.x);
						mvMax.y = Math.max(mvMax.y, position.y);
						mvMax.z = Math.max(mvMax.z, position.z);
					}
				}
			}

			mpBufVertices[i].position(0);
			//mpBufVerticesExpand[i].position(0);
		}

		if (mbInitBoundingBox) {
			//计算动态绑定球
			float distance = Vector3f.distance(mvMin, mvMax);
			mfRadius = distance * 0.5f;

			mvCenter.set(mvMin);
			mvCenter.add(mvMax);
			mvCenter.scale(0.5f);

			mbInitBoundingBox = false;
		}

		mbDirtFlag = false;
	}

	private static final int[] JOINT_INDEXES = new int[4],
			JOINT_WEIGHTS = new int[4];
	private static final float[] WEIGHTS = new float[4];

	static Vector3f tmp = new Vector3f(), tmpResult = new Vector3f(),
			tmpPos = new Vector3f();

	/**
	 * transfrom vertex by joint matrix
	 *
	 * @param vertex
	 * @return
	 */
	private Vector3f transformVertex(MS3DVertex vertex) {
		Vector3f position = vertex.mvTransformedLocation;
		fillJointIndexesAndWeights(vertex, JOINT_INDEXES, JOINT_WEIGHTS);

		if (JOINT_INDEXES[0] < 0 || JOINT_INDEXES[0] >= mpJoints.length
				|| mCurrentTime < 0.0f) {
			position.set(vertex.getLocation());
		} else {
			// count valid weights
			int numWeight = 0;
			for (int i = 0; i < 4; i++) {
				if (JOINT_WEIGHTS[i] > 0 && JOINT_INDEXES[i] >= 0
						&& JOINT_INDEXES[i] < mpJoints.length) {
					++numWeight;
				} else {
					break;
				}
			}

			// init
			position.zero();
			for (int i = 0; i < 4; i++) {
				WEIGHTS[i] = (float) JOINT_WEIGHTS[i] * 0.01f; // /100.0f
			}
			if (numWeight == 0) {
				numWeight = 1;
				WEIGHTS[0] = 1.0f;
			}

			for (int i = 0; i < numWeight; i++) {
				Joint joint = mpJoints[JOINT_INDEXES[i]];

				Matrix4f mat = joint.mMatJointAbsolute;
				// Vector3f tmp = tmp;
				Vector3f result = tmpResult;
				Vector3f pos = tmpPos;
				pos.set(vertex.getLocation());
				mat.invTransform(pos, tmp);
				joint.mMatGlobal.transform(tmp, result);

				position.x += result.x * WEIGHTS[i];
				position.y += result.y * WEIGHTS[i];
				position.z += result.z * WEIGHTS[i];
			}
		}

		return position;
	}

	/**
	 * 填充顶点的骨骼和权重信息，以便统一计算。
	 *
	 * @param vertex
	 * @param jointIndexes
	 * @param jointWeights
	 */
	private void fillJointIndexesAndWeights(MS3DVertex vertex,
											int[] jointIndexes, int[] jointWeights) {
		jointIndexes[0] = vertex.getBoneID();
		if (vertex.mpBoneIndexes == null) {
			for (int i = 0; i < 3; i++) {
				jointIndexes[i + 1] = 0;
			}
		} else {
			for (int i = 0; i < 3; i++) {
				jointIndexes[i + 1] = vertex.mpBoneIndexes[i] & 0xff;
			}
		}

		jointWeights[0] = 100;
		for (int i = 0; i < 3; i++) {
			jointWeights[i + 1] = 0;
		}

		if (vertex.mpWeights != null && vertex.mpWeights[0] != 0
				&& vertex.mpWeights[1] != 0 && vertex.mpWeights[2] != 0) {
			int sum = 0;
			for (int i = 0; i < 3; i++) {
				jointWeights[i] = vertex.mpWeights[i] & 0xff;
				sum += jointWeights[i];
			}

			jointWeights[3] = 100 - sum;
		}
	}

	/**
	 * 渲染实体模型
	 * @param gl
	 */
	public void render(GL10 gl) {
		gl.glPushMatrix();
		{
			//设置默认颜色
			//gl.glColor4f(1.0f, 0.5f, 0.5f, 1.0f);

			//启用客户端状态
			gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);

			//遍历所有的MS3D Group，渲染每一个Group
			for (int i = 0; i < mpGroups.length; i++) {
				if (mpGroups[i].getTriangleCount() == 0) {
					//如果该Group包含的三角形个数为零，则直接跳过
					continue;
				}
				//得到相应纹理
				TextureInfo tex = mpTexInfo[i % mpTexInfo.length];

				if (tex != null) {
					//如果纹理不为空，则绑定相应纹理
					gl.glBindTexture(GL10.GL_TEXTURE_2D, tex.mTexID);
					//启用纹理贴图
					gl.glEnable(GL10.GL_TEXTURE_2D);
					//绑定纹理坐标数据
					gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
					gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0,
							mpBufTextureCoords[i]);

				} else {
					//如果纹理为空，禁用纹理贴图
					//禁用纹理客户端状态
					gl.glDisable(GL10.GL_TEXTURE_2D);
					gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
				}
				//绑定顶点数据
				gl.glVertexPointer(3, GL10.GL_FLOAT, 0, mpBufVertices[i]);
				//提交渲染
				gl.glDrawArrays(GL10.GL_TRIANGLES, 0, mpGroups[i]
						.getTriangleCount() * 3);
			}
			//渲染完毕，重置客户端状态
			gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
			gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
			gl.glDisable(GL10.GL_TEXTURE_2D);
		}
		gl.glPopMatrix();
	}

	/**
	 * 渲染骨骼帮助信息
	 * @param gl
	 */
	public void renderJoints(GL10 gl) {
		if(!containsJoint()) {
			return;
		}
		//为保证骨骼始终可见，暂时禁用深度测试
		gl.glDisable(GL10.GL_DEPTH_TEST);
		//设置点和线的宽度
		gl.glPointSize(4.0f);
		gl.glLineWidth(2.0f);
		//仅仅启用顶点数据
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);

		//渲染骨骼连线
		gl.glColor4f(1.0f, 0.0f, 0.0f, 1.0f);//设置颜色
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, mBufJointLinePosition);
		//提交渲染
		gl.glDrawArrays(GL10.GL_LINES, 0, mJointLineCount);

		//渲染关节点
		gl.glColor4f(1.0f, 1.0f, 0.0f, 1.0f);//设置颜色
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, mBufJointPointPosition);
		//提交渲染
		gl.glDrawArrays(GL10.GL_POINTS, 0, mJointPointCount);

		//重置
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glPointSize(1.0f);
		gl.glLineWidth(1.0f);
		gl.glEnable(GL10.GL_DEPTH_TEST);
	}

	public Vector3f getSphereCenter() {
		return mvCenter;
	}

	public float getSphereRadius() {
		return mfRadius;
	}

	public boolean containsAnimation() {
		return mNumFrames > 0 && mpJoints != null && mpJoints.length > 0;
	}

	public boolean containsJoint() {
		return mpJoints != null && mpJoints.length > 0;
	}

	private static Vector3f v0 = new Vector3f(), v1 = new Vector3f(), v2 = new Vector3f();
	private static Vector4f location = new Vector4f();
	/**
	 * 射线与模型的精确碰撞检测
	 * @param ray - 转换到模型空间中的射线
	 * @param trianglePosOut - 返回的拾取后的三角形顶点位置
	 * @return 如果相交，返回true
	 */
	public boolean intersect(Ray ray, Vector3f[] trianglePosOut) {
		boolean bFound = false;
		//存储着射线原点与三角形相交点的距离
		//我们最后仅仅保留距离最近的那一个
		float closeDis = 0.0f;

		for(int i = 0; i < mpGroups.length; i++) {
			//遍历每个Group
			mpBufVertices[i].position(0);
			int vertexCount = mpBufVertices[i].limit() / 3;
			int triangleCount = vertexCount / 3;
			//由于我们提交渲染的Buffer数据是以Triangle List的形式填充的，不牵扯到索引值
			//因此，每3个顶点就组成一个三角形
			for(int idxTriangle = 0; idxTriangle < triangleCount; idxTriangle++) {
				//遍历每个三角形
				//填充三角形数据，顶点v0, v1, v2
				IBufferFactory.read(mpBufVertices[i], v0);
				IBufferFactory.read(mpBufVertices[i], v1);
				IBufferFactory.read(mpBufVertices[i], v2);
				//进行射线和三角行的碰撞检测
				if(ray.intersectTriangle(v0, v1, v2, location)) {
					//如果发生了相交
					if(!bFound) {
						//如果是初次检测到，需要存储射线原点与三角形交点的距离值
						bFound = true;
						closeDis = location.w;
						trianglePosOut[0].set(v0);
						trianglePosOut[1].set(v1);
						trianglePosOut[2].set(v2);
					} else {
						//如果之前已经检测到相交事件，则需要把新相交点与之前的相交数据相比较
						//最终保留离射线原点更近的
						if(closeDis > location.w) {
							closeDis = location.w;
							trianglePosOut[0].set(v0);
							trianglePosOut[1].set(v1);
							trianglePosOut[2].set(v2);
						}
					}
				}
			}
			//重置Buffer
			mpBufVertices[i].position(0);
		}

		return bFound;
	}
}