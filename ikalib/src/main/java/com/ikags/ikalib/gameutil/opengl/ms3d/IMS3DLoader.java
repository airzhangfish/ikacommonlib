/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ikags.ikalib.gameutil.opengl.ms3d;

import java.io.IOException;
import java.io.InputStream;

import com.ikags.ikalib.gameutil.opengl.lib.Color4f;
import com.ikags.ikalib.gameutil.opengl.lib.Matrix4f;
import com.ikags.ikalib.gameutil.opengl.lib.TextureInfo;
import com.ikags.ikalib.gameutil.opengl.lib.Vector3f;

/**
 * 载入MS3D
 * @author Yong.Xue
 */
public class IMS3DLoader {
    private final int UNKNOWN = 0;
    private final int FROM_FILE = 1;
    private final int FROM_URL = 2;
    private final int LEN_NAME = 32;
    private final int LEN_FILENAME = 128;

    private LittleEndianDataInputStream mLedis = null;
    private long mFileSize = 0;

    /**
     * 载入一个MS3D模型
     * @param is - IO输入流
     * @param model - 要载入的模型实例
     * @return true - if load success
     */
    public boolean Load(InputStream is, IMS3DModel model) {
        try {
            mLedis = new LittleEndianDataInputStream(is);
            mFileSize = mLedis.available();

            model.mHeader = loadHeader();
            model.mpVertices = loadVertexList();
            model.mpTriangles = loadTriangleList();
            model.mpGroups = loadGroupList();
            model.mpMaterials = loadMaterialList();

            model.mFps = mLedis.readFloat();
            model.mCurrentTime = mLedis.readFloat();
            model.mNumFrames = mLedis.readInt();

            MS3DJoint[] _joints = loadJointList();
            model.mpJoints = setupJoints(_joints, model.mpVertices, model.mpTriangles);

            loadExtra(model);
            return true;
        } catch(Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }


    /** Loads the header of the file
     * @throws IOException thrown if an error occurs
     * @return  a MS3DHeader object
     */
    private final MS3DHeader loadHeader()
            throws IOException {
        final byte id[] = new byte[10];
        this.mLedis.read(id, 0, id.length);
        return new MS3DHeader(id, this.mLedis.readInt());
    }

    /** Loads in the list of vertecies used in the model
     * @throws IOException thrown if an error occurs
     * @return  An array of MS3DVertex objects
     */
    private final MS3DVertex[] loadVertexList()
            throws IOException {
        final int numVerts = this.mLedis.readUnsignedShort();
        final MS3DVertex vertList[] = new MS3DVertex[numVerts];
        for (int i = 0; i < numVerts; i++) {
            final byte flags = this.mLedis.readByte();
            final Vector3f vertex = new Vector3f(this.mLedis.readFloat(),
                    this.mLedis.readFloat(),
                    this.mLedis.readFloat());
            final byte boneID = this.mLedis.readByte();
            final byte refCount = this.mLedis.readByte();
            vertList[i] = new MS3DVertex(vertex, boneID, refCount, flags);
        }
        return vertList;
    }

    /** Loads in all of the triangles (faces) of the object.  These are stored in an array
     * and looked up via index when building the Shape3D objects.
     * @throws IOException thrown if an error occurs
     * @return  an array of MS3DTriangle objects
     */
    private final MS3DTriangle[] loadTriangleList()
            throws IOException {
        final int numTris = this.mLedis.readUnsignedShort();
        final MS3DTriangle triList[] = new MS3DTriangle[numTris];
        for (int x = 0; x < numTris; x++) {
            final MS3DTriangle triangle = new MS3DTriangle();
            triangle.setFlags(this.mLedis.readUnsignedShort());

            final int vertIndicies[] = new int[3];
            vertIndicies[0] = this.mLedis.readUnsignedShort();
            vertIndicies[1] = this.mLedis.readUnsignedShort();
            vertIndicies[2] = this.mLedis.readUnsignedShort();
            triangle.setVertexIndicies(vertIndicies);

            final Vector3f normals[] = new Vector3f[3];
            for (int i = 0; i < 3; i++) {
                normals[i] = new Vector3f(this.mLedis.readFloat(), this.mLedis.readFloat(), this.mLedis.readFloat());
            }
            triangle.setVertexNormals(normals);

            final float s[] = new float[3];
            s[0] = this.mLedis.readFloat();
            s[1] = this.mLedis.readFloat();
            s[2] = this.mLedis.readFloat();
            triangle.setS(s);

            final float t[] = new float[3];
            t[0] = this.mLedis.readFloat();
            t[1] = this.mLedis.readFloat();
            t[2] = this.mLedis.readFloat();
            triangle.setT(t);

            triangle.setSmoothingGroup(this.mLedis.readByte());
            triangle.setGroupIndex(this.mLedis.readByte());
            triList[x] = triangle;
        }
        return triList;
    }

    /** Loads in the groups (meshes) in the model.
     * @throws IOException thrown if an error occurs
     * @return  an array of MS3DGroup objects
     */
    private final MS3DGroup[] loadGroupList()
            throws IOException {
        final int numGroups = this.mLedis.readUnsignedShort();
        final MS3DGroup groupList[] = new MS3DGroup[numGroups];
        for (int x = 0; x < numGroups; x++) {
            final MS3DGroup group = new MS3DGroup();
            group.setFlags(this.mLedis.readByte());
            final byte name[] = new byte[this.LEN_NAME];
            this.mLedis.read(name, 0, name.length);
            group.setName(makeSafeString(name));
            final int indexCount = this.mLedis.readUnsignedShort();
            final int indicies[] = new int[indexCount];
            for (int i = 0; i < indexCount; i++) {
                indicies[i] = this.mLedis.readUnsignedShort();
            }
            group.setTriangleIndicies(indicies);
            group.setMaterialIndex(this.mLedis.readByte());
            groupList[x] = group;
        }
        return groupList;
    }

    /** Loads in the materials used in the model
     * @throws IOException thrown if there is a problem reading the model
     * @return an array of MS3DMaterial objects
     */
    private final MS3DMaterial[] loadMaterialList()
            throws IOException {
        final int numMaterials = this.mLedis.readUnsignedShort();
        final MS3DMaterial materialList[] = new MS3DMaterial[numMaterials];
        for (int x = 0; x < numMaterials; x++) {
            final MS3DMaterial mat = new MS3DMaterial();
            final byte name[] = new byte[this.LEN_NAME];
            this.mLedis.read(name, 0, name.length);
            mat.setName(makeSafeString(name));
            mat.setAmbient(new Color4f(this.mLedis.readFloat(), this.mLedis.readFloat(),
                    this.mLedis.readFloat(), this.mLedis.readFloat()));
            mat.setDiffuse(new Color4f(this.mLedis.readFloat(), this.mLedis.readFloat(),
                    this.mLedis.readFloat(), this.mLedis.readFloat()));
            mat.setSpecular(new Color4f(this.mLedis.readFloat(), this.mLedis.readFloat(),
                    this.mLedis.readFloat(), this.mLedis.readFloat()));
            mat.setEmissive(new Color4f(this.mLedis.readFloat(), this.mLedis.readFloat(),
                    this.mLedis.readFloat(), this.mLedis.readFloat()));
            mat.setShininess(this.mLedis.readFloat());
            mat.setTransparency(this.mLedis.readFloat());
            mat.setMode(this.mLedis.readByte());
            final byte texture[] = new byte[this.LEN_FILENAME];
            this.mLedis.read(texture, 0, texture.length);
            mat.setTextureName(makeSafeString(texture));
            final byte alphamap[] = new byte[this.LEN_FILENAME];
            this.mLedis.read(alphamap, 0, alphamap.length);
            mat.setAlphaMap(makeSafeString(alphamap));

            loadTexture(mat.getTextureName(), mat.getAlphaMap());

            materialList[x] = mat;
        }
        return materialList;
    }

    /** Loads in the joints used in animating the model
     * @throws IOException thrown if an error occurs
     * @return  an array of MS3DJoint objects
     */
    private final MS3DJoint[] loadJointList()
            throws IOException {
        final int numJoints = this.mLedis.readUnsignedShort();
        final MS3DJoint jointList[] = new MS3DJoint[numJoints];
        for (int x = 0; x < numJoints; x++) {
            final MS3DJoint joint = new MS3DJoint();
            joint.setFlags(this.mLedis.readByte());
            final byte name[] = new byte[this.LEN_NAME];
            this.mLedis.read(name, 0, name.length);
            joint.setName(makeSafeString(name));

            final byte parent[] = new byte[this.LEN_NAME];
            this.mLedis.read(parent, 0, parent.length);
            joint.setParentName(makeSafeString(parent));

            joint.setRotation(new Vector3f(this.mLedis.readFloat(), this.mLedis.readFloat(), this.mLedis.readFloat()));
            joint.setPosition(new Vector3f(this.mLedis.readFloat(), this.mLedis.readFloat(), this.mLedis.readFloat()));

            final int numKeyFramesRot = this.mLedis.readUnsignedShort();
            final int numKeyFramesPos = this.mLedis.readUnsignedShort();
            final MS3DKeyFrameRotation keyFramesRot[] = new MS3DKeyFrameRotation[numKeyFramesRot];
            final MS3DKeyFramePosition keyFramesPos[] = new MS3DKeyFramePosition[numKeyFramesPos];

            for (int i = 0; i < numKeyFramesRot; i++) {
                final MS3DKeyFrameRotation keyFrameRot = new MS3DKeyFrameRotation();
                keyFrameRot.setTime(this.mLedis.readFloat());
                keyFrameRot.setRotation(new Vector3f(this.mLedis.readFloat(), this.mLedis.readFloat(), this.mLedis.readFloat()));
                keyFramesRot[i] = keyFrameRot;
            }
            joint.setRotationKeyFrames(keyFramesRot);

            for (int i = 0; i < numKeyFramesPos; i++) {
                final MS3DKeyFramePosition keyFramePos = new MS3DKeyFramePosition();
                keyFramePos.setTime(this.mLedis.readFloat());
                keyFramePos.setPosition(new Vector3f(this.mLedis.readFloat(), this.mLedis.readFloat(), this.mLedis.readFloat()));
                keyFramesPos[i] = keyFramePos;
            }
            joint.setPositionKeyFrames(keyFramesPos);

            jointList[x] = joint;
        }
        return jointList;
    }

    /**
     * 载入额外信息
     * @param model
     * @throws IOException
     */
    private void loadExtra(IMS3DModel model) throws IOException {
        if(this.mLedis.getPosition() < mFileSize) {
            //still can read
            int subVersion = mLedis.readInt();
            if(subVersion == 1) {
                //group comments
                int numComments = mLedis.readInt();

                for(int i = 0; i < numComments; i++) {
                    int index = mLedis.readInt();
                    int commentSize = mLedis.readInt();
                    byte[] data = new byte[commentSize];
                    mLedis.read(data, 0, data.length);


                }
                //material comments
                numComments = mLedis.readInt();

                for(int i = 0; i < numComments; i++) {
                    int index = mLedis.readInt();
                    int commentSize = mLedis.readInt();
                    byte[] data = new byte[commentSize];
                    mLedis.read(data, 0, data.length);


                }

                //joint comments
                numComments = mLedis.readInt();

                for(int i = 0; i < numComments; i++) {
                    int index = mLedis.readInt();
                    int commentSize = mLedis.readInt();
                    byte[] data = new byte[commentSize];
                    mLedis.read(data, 0, data.length);


                }

                //model comments
                numComments = mLedis.readInt();
                if(numComments == 1) {
                    int commentSize = mLedis.readInt();
                    byte[] data = new byte[commentSize];
                    mLedis.read(data, 0, data.length);
                    String comment = new String(data);
                    model.setComment(comment);
                }
            }
        }

        //vertex data
        if(mLedis.getPosition() < mFileSize) {
            int subVersion = mLedis.readInt();
            if(subVersion == 2) {
                for(int i = 0; i < model.mpVertices.length; i++) {
                    model.mpVertices[i].mpBoneIndexes = new byte[3];
                    model.mpVertices[i].mpWeights = new byte[3];
                    for(int j = 0; j < 3; j++) {
                        model.mpVertices[i].mpBoneIndexes[i] = mLedis.readByte();
                        model.mpVertices[i].mpWeights[i] = mLedis.readByte();
                    }

                    model.mpVertices[i].mExtra = mLedis.readInt();
                }
            } else if(subVersion == 1) {
                for(int i = 0; i < model.mpVertices.length; i++) {
                    model.mpVertices[i].mpBoneIndexes = new byte[3];
                    model.mpVertices[i].mpWeights = new byte[3];
                    for(int j = 0; j < 3; j++) {
                        model.mpVertices[i].mpBoneIndexes[i] = mLedis.readByte();
                        model.mpVertices[i].mpWeights[i] = mLedis.readByte();
                    }

                    //model.m_vertexes[i].m_extra = ledis.readInt();
                }
            }
        }
    }

    /** Converts an array of bytes into a java.lang.String object.  The String is created by
     * searching for the first occurrence of null ('\0').  If a null is not within the byte
     * array, the entire array is used to create the String.
     * @param buffer - an array of bytes
     * @return  a String
     */
    private final String makeSafeString(final byte buffer[]) {
        final int len = buffer.length;
        for (int i = 0; i < len; i++) {
            if (buffer[i] == (byte) 0) {
                return new String(buffer, 0, i);
            }
        }
        return new String(buffer);
    }

    private final int[] buildParentJointMap(final MS3DJoint[] jointList, final int numJoints) {
        final int[] parentMap = new int[numJoints];
        for (int x = 0; x < numJoints; x++) {
            if (jointList[x].getParentName().length() > 0) {
                for (int y = 0; y < numJoints; y++) {
                    if (jointList[x].getParentName().equals(jointList[y].getName())) {
                        parentMap[x] = y;
                        break;
                    }
                    parentMap[x] = -1;
                }
            } else {
                parentMap[x] = -1;
            }
        }
        return parentMap;
    }

    private final Joint[] setupJoints(final MS3DJoint[] jointList,
                                      final MS3DVertex[] vertList,
                                      final MS3DTriangle[] triList) {
        final int numJoints = jointList.length;
        final int numVerts = vertList.length;
        final int numTris = triList.length;

        Joint[] joints = new Joint[numJoints];

        for (int x = 0; x < numJoints; x++) {
            Joint joint = new Joint();
            joint.mId = x;
            if (jointList[x].getParentName().length() > 0) {
                for (int y = 0; y < numJoints; y++) {
                    if (jointList[x].getParentName().equals(jointList[y].getName())) {
                        joint.mParentId = y;
                        break;
                    }
                    joint.mParentId = -1;
                }
            } else {
                joint.mParentId = -1;
            }
            joint.mName = jointList[x].getName();
            joint.mNumRotationKeyframes = jointList[x].getRotationKeyFramesCount();
            joint.mNumTranslationKeyframes = jointList[x].getPositionKeyFramesCount();
            joint.mpRotationKeyframes = new Keyframe[joint.mNumRotationKeyframes];
            for(int k = 0; k < joint.mNumRotationKeyframes; k++) {
                joint.mpRotationKeyframes[k] = new Keyframe();
                joint.mpRotationKeyframes[k].getValue(jointList[x].getRotationKeyFrames()[k]);
            }
            joint.mpTranslationKeyframes = new Keyframe[joint.mNumTranslationKeyframes];
            for(int k = 0; k < joint.mNumTranslationKeyframes; k++) {
                joint.mpTranslationKeyframes[k] = new Keyframe();
                joint.mpTranslationKeyframes[k].getValue(jointList[x].getPositionKeyFrames()[k]);
            }

            joint.mLocalRotation = jointList[x].getRotation();
            joint.mLocalTranslation = jointList[x].getPosition();
            joint.mMatJointRelative = setRotationRadians(joint.mLocalRotation);
            joint.mMatJointRelative.setTranslation(joint.mLocalTranslation);
            if (joint.mParentId == -1) {
                joint.mMatJointAbsolute.set(joint.mMatJointRelative);
            } else {
                joint.mMatJointAbsolute.set(joints[joint.mParentId].mMatJointAbsolute);
                joint.mMatJointAbsolute.mul(joint.mMatJointRelative);
            }
            joint.mMatGlobal.set(joint.mMatJointAbsolute);
            joints[x] = joint;
        }

        for (int x = 0; x < numVerts; x++) {
            MS3DVertex vertex = vertList[x];
            if (vertex.getBoneID() != -1) {
                if(vertex.getBoneID()<joints.length){
                    final Matrix4f matrix = joints[vertex.getBoneID()].mMatJointAbsolute;
                }
                inverseTranslateVect(vertex.getLocation());
                inverseRotateVect(vertex.getLocation());
            }
        }

        for (int x = 0; x < numTris; x++) {
            MS3DTriangle triangle = triList[x];
            for (int y = 0; y < 3; y++) {
                final MS3DVertex vertex = vertList[triangle.getVertexIndicies()[y]];
                if (vertex.getBoneID() != -1) {
                    if(vertex.getBoneID()<joints.length){
                        final Matrix4f matrix = joints[vertex.getBoneID()].mMatJointAbsolute;
                    }
                    inverseRotateVect(triangle.getVertexNormals()[y]);
                }
            }
        }


        return joints;
    }

    public final static Matrix4f setRotationRadians(final Vector3f angles) {
        Matrix4f ret = new Matrix4f();
        ret.setIdentity();
        final double cr = Math.cos(angles.x);
        final double sr = Math.sin(angles.x);
        final double cp = Math.cos(angles.y);
        final double sp = Math.sin(angles.y);
        final double cy = Math.cos(angles.z);
        final double sy = Math.sin(angles.z);
        final double srsp = sr * sp;
        final double crsp = cr * sp;

        ret.m00 = (float) (cp * cy);
        ret.m10 = (float) (cp * sy);
        ret.m20 = (float) (-sp);

        ret.m01 = (float) (srsp * cy - cr * sy);
        ret.m11 = (float) (srsp * sy + cr * cy);
        ret.m21 = (float) (sr * cp);

        ret.m02 = (float) (crsp * cy + sr * sy);
        ret.m12 = (float) (crsp * sy - sr * cy);
        ret.m22 = (float) (cr * cp);
        return ret;
    }

    private final static Matrix4f inverseRotateVect(final Vector3f vector) {
        return new Matrix4f();
    }

    private final static Matrix4f inverseTranslateVect(final Vector3f vector) {
        return new Matrix4f();
    }

    /**
     * Notice--currently not support for alphamap
     *
     * Loads the texture from the provided file.  If an alphamap is provided, then the
     * alpha values will be automatically calculated.  The formula to generate an alphamap
     * from RGB values is A = (R + G + B) / 3.
     * @param texFile the texture filename
     * @param alphaFile the alphamap file name
     * @throws IOException thrown if there is a problem reading the files
     * @return a Texture object
     */
    private final TextureInfo loadTexture(String texFile, String alphaFile)
            throws IOException {
        TextureInfo texture = null;

        if (texFile.length() > 0) {
            //texture = ITextureMgr.GetTexture(texFile);
        }

//        // if we're going to use an alphamap, we need to build a new texture
//        if (texture != null && alphaFile.length() > 0) {
//            TextureLoader alphaLoader = null;
//            switch (source) {
//                case FROM_FILE:
//                    if (this.getBasePath() != null) {
//                        alphaFile = this.getBasePath() + alphaFile;
//                    }
//                    alphaLoader = new TextureLoader(alphaFile, observer);
//                    break;
//                case FROM_URL:
//                    alphaLoader = new TextureLoader(new URL(getBaseUrl(), urlSlash(alphaFile)), observer);
//                    break;
//            }
//            final int width = texture.getWidth();
//            final int height = texture.getHeight();
//            final int length = width * height;
//
//            final BufferedImage buffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
//            final BufferedImage alphaBuff = new BufferedImage();
//            final BufferedImage texBuff = texLoader.getImage().getImage();
//
//            final int temp[] = new int[length];
//            final int rgb[] = new int[length];
//            alphaBuff.getRGB(0, 0, width, height, temp, 0, width);
//            texBuff.getRGB(0, 0, width, height, rgb, 0, width);
//            int x = -1;
//            while (++x < length) {
//                final int a = ((((temp[x] & 0xff0000) >> 16) + ((temp[x] & 0xff00) >> 8) + (temp[x] & 0xff)) / 3);
//                temp[x] = (a << 24) + (rgb[x] & 0xffffff);
//            }
//            buffer.setRGB(0, 0, width, height, temp, 0, width);
//
//            texture = new Texture
//            texture = new Texture2D(Texture2D.BASE_LEVEL, Texture2D.RGBA, width, height);
//            texture.setImage(0, new ImageComponent2D(ImageComponent2D.FORMAT_RGBA, buffer));
//        }

        return texture;
    }
}