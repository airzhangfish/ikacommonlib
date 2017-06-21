/*
 * MS3DMaterial.java
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

import com.ikags.ikalib.gameutil.opengl.lib.Color4f;


/** This object stores the material information for a MS3DGroup object.
 *
 *@author Yong
 */
final class MS3DMaterial {

    private String mName;
    private Color4f mAmbient;
    private Color4f mDiffuse;
    private Color4f mSpecular;
    private Color4f mEmissive;
    private float mfShininess;
    private float mfTransparency;
    private byte mMode;
    private String mTextureName;
    private String mAlpha;

    private String mComment;

    /**
     * TexInfo of this material
     */
    //public ICommonTexInfo pTexInfo;

    public String getComment() {
        return mComment;
    }

    public void setComment(String comment) {
        this.mComment = comment;
    }

    /** Creates new MS3DMaterial */
    MS3DMaterial() {
    }

    /** Creates new MS3DMaterial.  The object is initialized by the provided parameters
     * @param name the name of the material
     * @param ambient the ambient color for the material
     * @param diffuse the diffuse color for the material
     * @param specular the specular color for the material
     * @param emissive the emissive color for the material
     * @param shininess the shininess of the material
     * @param transparency the transparency level of the material
     * @param mode unused
     * @param mTextureName the filename of the primary textureName
     * @param alpha  the filename of the secondary textureName
     */
    MS3DMaterial(final String name,
            final Color4f ambient,
            final Color4f diffuse,
            final Color4f specular,
            final Color4f emissive,
            final float shininess,
            final float transparency,
            final byte mode,
            final String texture,
            final String alpha) {
        setName(name);
        setAmbient(ambient);
        setDiffuse(diffuse);
        setSpecular(specular);
        setEmissive(emissive);
        setShininess(shininess);
        setTransparency(transparency);
        setMode(mode);
        setTextureName(texture);
        setAlphaMap(alpha);
    }

    /** Retrieve the name of this material
     * @return  a String
     */
    final String getName() {
        return this.mName;
    }

    /** Set the name of this material
     * @param name  a String
     */
    final void setName(final String name) {
        this.mName = name;
    }

    /** Retrieve the ambient color of this material
     * @return  a Color4f object
     */
    final Color4f getAmbient() {
        return this.mAmbient;
    }

    /** Sets the ambient color of this material
     * @param ambient  a Color4f object
     */
    final void setAmbient(final Color4f ambient) {
        this.mAmbient = ambient;
    }

    /** Retrieve the diffuse color of this material
     * @return  a Color4f object
     */
    final Color4f getDiffuse() {
        return this.mDiffuse;
    }

    /** Sets the diffuse color of this material
     * @param diffuse  a Color4f object
     */
    final void setDiffuse(final Color4f diffuse) {
        this.mDiffuse = diffuse;
    }

    /** Retrieve the specular color of this material
     * @return  a Color4f object
     */
    final Color4f getSpecular() {
        return this.mSpecular;
    }

    /** Sets the specular color of this material
     * @param specular  a Color4f object
     */
    final void setSpecular(final Color4f specular) {
        this.mSpecular = specular;
    }

    /** Retrieve the emissive color of this material
     * @return  a Color4f object
     */
    final Color4f getEmissive() {
        return this.mEmissive;
    }

    /** Sets the emissive color of this material
     * @param emissive  a Color4f object
     */
    final void setEmissive(final Color4f emissive) {
        this.mEmissive = emissive;
    }

    /** Retrieve the shininess value of this material
     * @return  a float
     */
    final float getShininess() {
        return this.mfShininess;
    }

    /** Set the shininess value of this material
     * @param shininess  a float
     */
    final void setShininess(final float shininess) {
        this.mfShininess = shininess;
    }

    /** Retrieve the transparency value for this material
     * @return  a float
     */
    final float getTransparency() {
        return this.mfTransparency;
    }

    /** Set the transparency value for this material
     * @param transparency  a float
     */
    final void setTransparency(final float transparency) {
        this.mfTransparency = transparency;
    }

    /** not used
     * @return  a byte
     */
    final byte getMode() {
        return this.mMode;
    }

    /** not used
     * @param mode a byte
     */
    final void setMode(final byte mode) {
        this.mMode = mode;
    }

    /** Retrieve the filename of the primary textureName for this material
     * @return  a String containing a filename
     */
    final String getTextureName() {
        return this.mTextureName;
    }

    /** Set the filename of the primary textureName for this material.
     * @param mTextureName  a String containing a filename
     */
    final void setTextureName(final String texture) {
        this.mTextureName = convertSlash(texture);
    }

    /** Retrieve the filename of the secondary textureName for this material
     * @return  a String containing a filename
     */
    final String getAlphaMap() {
        return this.mAlpha;
    }

    /** Set the filename of the secondary textureName for this material
     * @param alpha  a String containing a filename
     */
    final void setAlphaMap(final String alpha) {
        this.mAlpha = convertSlash(alpha);
    }

    /** A utility function to convert Milkshape filenames to a platform-independent
     * format.  Milkshape stores all filenames in a DOS format.
     * @param filename the name of the file
     * @return the newly formatted string
     */
    private static final String convertSlash(final String filename) {
        final int len = filename.length();
        char temp[] = new char[len];
        filename.getChars(0, len, temp, 0);
        for (int x = 0; x < len; x++) {
            if (temp[x] == '\\') {
                temp[x] = java.io.File.separatorChar;
            }
        }
        return new String(temp);
    }
}