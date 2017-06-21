/*
 * Joint.java
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

import com.ikags.ikalib.gameutil.opengl.lib.Matrix4f;
import com.ikags.ikalib.gameutil.opengl.lib.Vector3f;

/**
 * һ�������Ĺؽ�
 *@author Yong
 */
public class Joint {
    public String mName;
    
    public int mId;
    public int mParentId;
    Vector3f mLocalRotation = new Vector3f();
    Vector3f mLocalTranslation = new Vector3f();
    Matrix4f mMatJointAbsolute = new Matrix4f();
    Matrix4f mMatJointRelative = new Matrix4f();
    Matrix4f mMatGlobal = new Matrix4f();
    public int mNumRotationKeyframes;
    public int mNumTranslationKeyframes;
    Keyframe[] mpTranslationKeyframes;
    Keyframe[] mpRotationKeyframes;

    /** Creates new Joint */
    public Joint() {
        mMatJointAbsolute.setIdentity();
        mMatJointRelative.setIdentity();
        mMatGlobal.setIdentity();
    }
}