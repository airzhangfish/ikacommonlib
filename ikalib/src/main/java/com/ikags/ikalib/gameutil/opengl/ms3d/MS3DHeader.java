/*
 * MS3DHeader.java
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

/** This class stores the information contained in the header of a Milkshape3D file.
 *
 *@author Yong
 */
public final class MS3DHeader {

    private byte mpIds[];
    private int mVersion;

    MS3DHeader() {
    }

    /** Constructs a MS3DHeader.  Initializes the header by the provided variables.
     * @param id the ID of the file
     * @param version the version of the Milkshape file format
     */
    public MS3DHeader(final byte id[],
            final int version) {
        setID(id);
        setVersion(version);
    }

    /** Retrieves the ID for the header
     * @return  a byte array
     */
    final byte[] getID() {
        return this.mpIds;
    }

    /** Sets the ID for the header
     * @param id  an array of bytes.  This should always be a length of 10 bytes and set to "MS3D000000"
     */
    final void setID(final byte id[]) {
        if (id.length != 10) {
            throw new IllegalArgumentException("MS3DHeader id length should be 10, is " + id.length);
        }
        if (!"MS3D000000".equals(new String(id))) {
            throw new IllegalArgumentException("MS3DHeader id \"" + id + "\" invalid");
        }
        this.mpIds = id;
    }

    /** Retrieve the version of this file format
     * @return  an int
     */
    final int getVersion() {
        return this.mVersion;
    }

    /** Sets the version for the header.  Only version 4 is understood at this time.
     * @param version  an int
     */
    final void setVersion(final int version) {
        if (version < 3 | version > 4) {
            throw new IllegalArgumentException("MS3DHeader version " + version + " unsupported");
        }
        this.mVersion = version;
    }
}