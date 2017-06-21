/*
 * MS3DGroup.java
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

/** Holds a group, or mesh, of triangles.
 *
 *@author Yong
 */
public class MS3DGroup {

    private byte mFlags;
    public String mName;
    private int mpTriangleIndicies[];
    private byte mMaterialIndex;
    private String mComment;

    public String getComment() {
        return mComment;
    }

    public void setComment(String comment) {
        this.mComment = comment;
    }

    /** Creates new MS3DGroup */
    MS3DGroup() {
    }

    /** Creates a new MS3DGroup
     * @param name the name of the group
     * @param triangleIndicies an array of indicies pointing to a list of triangles stored in {@link MS3DTriangle}
     * @param materialIndex points to the material that should be used for this mesh
     * @param flags not used in this release
     */
    MS3DGroup(final String name, final int[] triangleIndicies, final byte materialIndex, final byte flags) {
        setName(name);
        setTriangleIndicies(triangleIndicies);
        setMaterialIndex(materialIndex);
        setFlags(flags);
    }

    /** Retrieve the name of the mesh
     * @return  a String
     */
    final String getName() {
        return this.mName;
    }

    /** Sets the name of the mesh
     * @param name  a String
     */
    final void setName(final String name) {
        this.mName = name;
    }

    /** Retrieve the flags set for this group
     * @return  a byte indicating state
     */
    final byte getFlags() {
        return this.mFlags;
    }

    /** Sets the flags for this group
     * @param flags  a byte indicating state (not used in this release)
     */
    final void setFlags(final byte flags) {
    }

    /** Retrieve the list of indicies pointing into an array of MS3DTriangles.  These triangles
     * make up the mesh.
     * @return  an array of integers
     */
    final int[] getTriangleIndicies() {
        return this.mpTriangleIndicies;
    }

    /** Set the list of indicies.
     * @param indicies  an array of integers
     */
    final void setTriangleIndicies(final int indicies[]) {
        this.mpTriangleIndicies = indicies;
    }

    /** Retrieve the number of triangles in this mesh.
     * @return  an int
     */
    public final int getTriangleCount() {
        return this.mpTriangleIndicies.length;
    }

    /** Retrieve the index of the material used in this mesh.  The material is stored in a list
     * of {@link MS3DMaterial} objects.
     * @return  a byte
     */
    final byte getMaterialIndex() {
        return this.mMaterialIndex;
    }

    /** Sets the index of the material used in this mesh.
     * @param index  a byte
     */
    final void setMaterialIndex(final byte index) {
        this.mMaterialIndex = index;
    }
}