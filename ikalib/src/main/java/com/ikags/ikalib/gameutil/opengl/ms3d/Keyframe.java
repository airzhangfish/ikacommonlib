/*
 * Keyframe.java
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
 *
 * Created on October 2, 2001, 1:05 PM
 */
package com.ikags.ikalib.gameutil.opengl.ms3d;

import com.ikags.ikalib.gameutil.opengl.lib.Vector3f;


/**
 *
 *@author Yong
 */
final class Keyframe {
    float mfTime;
    Vector3f mvParam;

    /** Creates new Keyframe */
    public Keyframe() {
    }

    public void getValue(MS3DKeyFramePosition frame) {
        mfTime = frame.getTime();
        mvParam = new Vector3f(frame.getPosition().x, frame.getPosition().y, frame.getPosition().z);
    }

    public void getValue(MS3DKeyFrameRotation frame) {
        mfTime = frame.getTime();
        mvParam = new Vector3f(frame.getRotation().x, frame.getRotation().y, frame.getRotation().z);
    }

    public float getTime() {
        return mfTime;
    }

    public Vector3f getParameter() {
        return mvParam;
    }
}
