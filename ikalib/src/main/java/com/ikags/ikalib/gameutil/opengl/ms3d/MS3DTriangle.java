
package com.ikags.ikalib.gameutil.opengl.ms3d;

import com.ikags.ikalib.gameutil.opengl.lib.Vector3f;

/** 
 * Represents a triangle.
 *@author Yong
 */
final class MS3DTriangle {

    private int mFlags;
    private int mpVertexIndices[];
    private Vector3f mpVertexNormals[];
    private float mps[];
    private float mpt[];
    private byte mSmoothingGroup;
    private byte mGroupIndex;

    /* Creates a MS3DTriangle */
    MS3DTriangle() {
    }

    /** Retrieves this object's flags
     * @return  an int
     */
    final int getFlags() {
        return this.mFlags;
    }

    /** Sets the flags on this object
     * @param flags  an int
     */
    final void setFlags(final int flags) {
        this.mFlags = flags;
    }

    /** Retrieves the list indicies that point to a list of MS3DVertex objects
     * @return  an array of int
     */
    final int[] getVertexIndicies() {
        return this.mpVertexIndices;
    }

    /** Sets the list of indicies pointing to a list of MS3DVertex objects
     * @param indicies  an array of int
     */
    final void setVertexIndicies(final int indicies[]) {
        if (indicies.length != 3) {
            throw new IllegalArgumentException("MS3DTriangle: indicies must be a length of 3");
        }
        this.mpVertexIndices = indicies;
    }

    /** Retrieve the horizontal texture coordinates for this triangle
     * @return  an array of floats
     */
    final float[] getS() {
        return this.mps;
    }

    /** Set the horizontal texture coordinates for this triangle
     * @param s  an array of floats
     */
    final void setS(final float s[]) {
        if (s.length != 3) {
            throw new IllegalArgumentException("MS3DTriangle: s must be a length of 3");
        }
        this.mps = s;
    }

    /** Retrieve the smoothing group for this triangle
     * @return  a byte
     */
    final byte getSmoothingGroup() {
        return this.mSmoothingGroup;
    }

    /** Set the smoothing group for this triangle
     * @param smooth  a byte
     */
    final void setSmoothingGroup(final byte smooth) {
        this.mSmoothingGroup = smooth;
    }

    /** Retrieve the index of the group that this triangle is part of
     * @return  a byte
     */
    final byte getGroupIndex() {
        return this.mGroupIndex;
    }

    /** Set the group index to indicate which group this triangle is part of
     * @param index  a byte
     */
    final void setGroupIndex(final byte index) {
        this.mGroupIndex = index;
    }

    /** Retrieve the vertical texture coordinates for this triangle
     * @return  an array of float
     */
    final float[] getT() {
        return this.mpt;
    }

    /** Set the vertical texture coordinates for this triangle
     * @param t  an array of float
     */
    final void setT(final float t[]) {
        if (t.length != 3) {
            throw new IllegalArgumentException("MS3DTriangle: t must be a length of 3");
        }
        this.mpt = t;
    }

    /** Retrieve the vertex normals for this triangle
     * @return  an array of three Vector3f objects
     */
    final Vector3f[] getVertexNormals() {
        return this.mpVertexNormals;
    }

    /** Sets the vertex normals for this triangle
     * @param normals  an array of three Vector3f objects
     */
    final void setVertexNormals(final Vector3f normals[]) {
        if (normals.length != 3) {
            throw new IllegalArgumentException("MS3DTriangle: normals must be a length of 3");
        }
        this.mpVertexNormals = normals;
    }
}