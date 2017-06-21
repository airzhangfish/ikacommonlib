/*
 * MS3DKeyFramePosition.java
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


/** Stores the list of position key frames.
 *
 *@author Yong
 */
final class MS3DKeyFramePosition {

    private float mfTime;
    private Vector3f mvPosition;

    /* Creates a MS3DKeyFramePosition */
    MS3DKeyFramePosition() {
    }

    /** Creates a MS3DKeyFramePosition.  Initializes based on provided parameters.
     * @param x x coordinate
     * @param y y coordinate
     * @param z z coordinate
     * @param time  time value
     */
    MS3DKeyFramePosition(final float x,
            final float y,
            final float z,
            final float time) {
        this(new Vector3f(x, y, z), time);
    }

    /** Creates a MS3DKeyFramePosition.  Initializes based on provided parameters.
     * @param position the position
     * @param time time value
     */
    MS3DKeyFramePosition(final Vector3f position,
            final float time) {
        setPosition(position);
        setTime(time);
    }

    /** Retrieve the time of this position
     * @return  a float
     */
    final float getTime() {
        return this.mfTime;
    }

    /** Sets the time of this position
     * @param time  a float
     */
    final void setTime(final float time) {
        this.mfTime = time;
    }

    /** Retrieves the position of this key frame
     * @return  a Point3f
     */
    final Vector3f getPosition() {
        return this.mvPosition;
    }

    /** Sets the position for this key frame
     * @param position a Point3f
     */
    final void setPosition(final Vector3f position) {
        this.mvPosition = position;
    }
}