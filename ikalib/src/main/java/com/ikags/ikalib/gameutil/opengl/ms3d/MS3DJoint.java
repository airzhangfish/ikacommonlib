/*
 * MS3DJoint.java
 *
 * Copyright (C) 2001-2002 Kevin J. Duling (kevin@dark-horse.net)
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package com.ikags.ikalib.gameutil.opengl.ms3d;

import com.ikags.ikalib.gameutil.opengl.lib.Vector3f;

/** MS3DJoint contains joint information, used for animating a Milkshape3D model.
 *
 *@author Yong
 */
final class MS3DJoint {

    private byte mFlags;
    private String mName;
    private String mParent;
    private Vector3f mvRotation;
    private Vector3f mvPosition;
    private short mNumKeyFramesRot;
    private short mNumKeyFramesTrans;
    private MS3DKeyFrameRotation mpKeyFramesRot[];
    private MS3DKeyFramePosition mpKeyFramesPos[];

    private String mComment;

    public String getComment() {
        return mComment;
    }

    public void setComment(String comment) {
        this.mComment = comment;
    }

    /** Creates new MS3DJoint */
    MS3DJoint() {
    }

    /** Creates a new MS3DJoint.  Values are initialized by the provided parameters.
     * @param name the name of the joint
     * @param parent the name of the parent joint
     * @param rotation the rotaiton of the joint
     * @param position the position of the joint
     * @param keyFramesRot the keyframes for rotations
     * @param keyFramesPos  the keyframes for positions
     */
    MS3DJoint(final String name,
            final String parent,
            final Vector3f rotation,
            final Vector3f position,
            final MS3DKeyFrameRotation keyFramesRot[],
            final MS3DKeyFramePosition keyFramesPos[]) {
        setName(name);
        setParentName(parent);
        setRotation(rotation);
        setPosition(position);
        setRotationKeyFrames(keyFramesRot);
        setPositionKeyFrames(keyFramesPos);
    }

    /** Retreive the name of this joint
     * @return  a String
     */
    final String getName() {
        return this.mName;
    }

    /** Sets the name of this joint.
     * @param name  a String
     */
    final void setName(final String name) {
        if (name.length() > 32) {
            throw new IllegalArgumentException("MS3DJoint: name is " + name.length() + ", can only be 32");
        }
        this.mName = name;
    }

    /** Retrieve the name of the parent joint.
     * @return  a String
     */
    final String getParentName() {
        if (mName.length() > 32) {
            throw new IllegalArgumentException("MS3DJoint: parent name is " + mName.length() + ", can only be 32");
        }
        return this.mParent;
    }

    /** Tell this joint what the name of its parent is.
     * @param name  a String
     */
    final void setParentName(final String name) {
        this.mParent = name;
    }

    /** Retrieve the flags set on this joint.
     * @return  a byte
     */
    final byte getFlags() {
        return this.mFlags;
    }

    /** Set the flags on this joint.
     * @param flags  a byte
     */
    final void setFlags(final byte flags) {
        this.mFlags = flags;
    }

    /** Retrieve the position of this joint.
     * @return  a Vector3f representing the position
     */
    final Vector3f getPosition() {
        return this.mvPosition;
    }

    /** Set the position of this joint.
     * @param position  a Vector3f representing this joint's position
     */
    final void setPosition(final Vector3f position) {
        this.mvPosition = position;
    }

    /** Retrieve the rotation of this joint.
     * @return  a Vector3f representing the rotation
     */
    final Vector3f getRotation() {
        return this.mvRotation;
    }

    /** Set the rotation of this joint.
     * @param rotation  a Vector3f representing this joint's rotation
     */
    final void setRotation(final Vector3f rotation) {
        this.mvRotation = rotation;
    }

    /** Retrieve the number of rotational key frames.
     * @return  a short
     */
    final short getRotationKeyFramesCount() {
        return (short) this.mpKeyFramesRot.length;
    }

    /** Retrieve the number of positional key frames.
     * @return  a short
     */
    final short getPositionKeyFramesCount() {
        return (short) this.mpKeyFramesPos.length;
    }

    /** Retreive the Rotation Key Frames
     * @return  an array of MS3DKeyFrameRotation objects
     */
    final MS3DKeyFrameRotation[] getRotationKeyFrames() {
        return this.mpKeyFramesRot;
    }

    /** Set the rotation key frames
     * @param frames  an array of MS3DKeyFrameRotation objects */
    final void setRotationKeyFrames(final MS3DKeyFrameRotation frames[]) {
        this.mpKeyFramesRot = frames;
    }

    /** Retreive the position key frames
     * @return  an array of MS3DKeyFramePosition objects
     */
    final MS3DKeyFramePosition[] getPositionKeyFrames() {
        return this.mpKeyFramesPos;
    }

    /** Set the position key frames
     * @param frames  an array of setPositionKeyFrames objects */
    final void setPositionKeyFrames(final MS3DKeyFramePosition frames[]) {
        this.mpKeyFramesPos = frames;
    }
}