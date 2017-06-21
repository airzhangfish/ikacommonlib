/*
 * MS3DKeyFrameRotation.java
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


/** Stores the list of rotation key frames.
 *
 *@author Yong
 */
final class MS3DKeyFrameRotation {

    private float mfTime;
    private Vector3f mvRotation;

    /* Creates a new MS3DKeyFrameRotation */
    MS3DKeyFrameRotation() {
    }

    /* Creates a new MS3DKeyFrameRotation. Initializes based on provided parameters.
     * @param x rotation along the x axis
     * @param y rotation along the y axis
     * @param z rotation along the z axis
     * @param time  time value
     */
    MS3DKeyFrameRotation(final float x,
            final float y,
            final float z,
            final float time) {
        this(new Vector3f(x, y, z), time);
    }

    /* Creates a new MS3DKeyFrameRotation. Initializes based on provided parameters.
     * @param rotation a Vector3f
     * @param time  time value
     */
    MS3DKeyFrameRotation(final Vector3f rotation,
            final float time) {
        setRotation(rotation);
        setTime(time);
    }

    /** Retrieve the time of this keyframe
     * @return  a float
     */
    final float getTime() {
        return this.mfTime;
    }

    /** Set the time for this keyframe
     * @param time  a float
     */
    final void setTime(final float time) {
        this.mfTime = time;
    }

    /** Retrieve the rotation for this keyframe
     * @return  a Vector3f
     */
    final Vector3f getRotation() {
        return this.mvRotation;
    }

    /** Set the rotation of this keyframe
     * @param rotation  a Vector3f
     */
    final void setRotation(final Vector3f rotation) {
        this.mvRotation = rotation;
    }
}