package com.ikags.ikalib.gameutil.opengl.ms3d;

import com.ikags.ikalib.gameutil.opengl.lib.Vector3f;


/** 
 *Stores an individual vertex.
 *@author Yong
 */
final class MS3DVertex {

    private byte mFlags;
    private Vector3f mvLocation;
    private byte mBoneID;
    private byte mRefCount;

    public Vector3f mvTransformedLocation = new Vector3f();
    
    public byte[] mpBoneIndexes;
    public byte[] mpWeights;

    public int mExtra;

    /** Creates new MS3DVertex */
    MS3DVertex() {
    }

    /** Creates new MS3DVertex.  Initializes based on provided parameters.
     * @param x x coordinate
     * @param y y coordinate
     * @param z z coordinate
     * @param boneID id of the bone associated with this vertex
     * @param refCount times this vertex is referenced (?)
     * @param flags  flags set on this bone
     */
    MS3DVertex(final float x,
            final float y,
            final float z,
            final byte boneID,
            final byte refCount,
            final byte flags) {
        this(new Vector3f(x, y, z), boneID, refCount, flags);
    }

    /** Creates new MS3DVertex.  Initializes based on provided parameters.
     * @param vertex position of this vertex
     * @param boneID id of the bone associated with this vertex
     * @param refCount times this vertex is referenced (?)
     * @param flags  flags set on this bone
     */
    MS3DVertex(final Vector3f location,
            final byte boneID,
            final byte refCount,
            final byte flags) {
        setLocation(location);
        setBoneID(boneID);
        setRefCount(refCount);
        setFlags(flags);
    }

    /** Retrieve the position of this vertex.
     * @return  a Point3f
     */
    final Vector3f getLocation() {
        return this.mvLocation;
    }

    /** Sets the location of this vertex.
     * @param location  a Point3f
     */
    final void setLocation(final Vector3f location) {
        this.mvLocation = location;
        mvTransformedLocation.set(location);
    }

    /** Retrieve the ID of the associated bone for animation.
     * @return  a byte
     */
    final byte getBoneID() {
        return this.mBoneID;
    }

    /** Sets the ID of the associated bone used for animation.
     * @param boneID  a byte
     */
    final void setBoneID(final byte boneID) {
        this.mBoneID = boneID;
    }

    /** Retrieve the reference count for this vertex
     * @return  a byte
     */
    final byte getRefCount() {
        return this.mRefCount;
    }

    /** Set the reference count for this vertex
     * @param refCount  a byte
     */
    final void setRefCount(final byte refCount) {
        this.mRefCount = refCount;
    }

    /** Retrieve the flags set on this object
     * @return  a byte
     */
    final byte getFlags() {
        return this.mFlags;
    }

    /** Set the flags on this object
     * @param flags  a byte
     */
    final void setFlags(final byte flags) {
        this.mFlags = flags;
    }
}