/*
 * LittleEndianDataInputStream.java
 *
 * Copyright (C) 2002 Kevin J. Duling (kevin@dark-horse.net)
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
 * Created on May 13, 2002, 2:52 PM
 */
package com.ikags.ikalib.gameutil.opengl.ms3d;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.FilterInputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.EOFException;

/** A DataInputStream-like object that reads in Little Endian data.
 *
 * @author Kevin J. Duling
 * @version $Revision: 1.1 $
 * @see DataInputStream
 */
public class LittleEndianDataInputStream
        extends FilterInputStream
        implements DataInput {

    private DataInputStream mDis;

    private int mPosition;
    
    public int getPosition() {
        return mPosition;
    }

    /** Construct a LittleEndianDataInputStream from a InputStream
     * @param in a java.io.InputStream object
     */
    public LittleEndianDataInputStream(InputStream in) {
        super(in);
        mDis = new DataInputStream(in);

        mPosition = 0;
    }

    /** See the general contract of the readBoolean method of DataInput.
     * Bytes for this operation are read from the contained input stream.
     * @throws IOException if an I/O error occurs.
     * @throws EOFException if this input stream has reached the end.
     * @return the boolean value read
     */
    public boolean readBoolean()
            throws IOException {
        mPosition += 1;
        return this.mDis.readBoolean();
    }

    /** See the general contract of the readByte method of DataInput.
     * Bytes for this operation are read from the contained input stream.
     * @throws IOException if an I/O error occurs.
     * @return a byte
     */
    public byte readByte()
            throws IOException {
        mPosition += 1;
        return this.mDis.readByte();
    }

    /** See the general contract of the readFloat method of DataInput.
     * Bytes for this operation are read from the contained input stream.
     * @throws IOException if an I/O error occurs.
     * @throws EOFException if this input stream reaches the end before reading four bytes.
     * @return the next four bytes of this input stream, interpreted as a float.
     */
    public float readFloat()
            throws IOException,
            EOFException {
        mPosition += 4;
        return Float.intBitsToFloat(this.readInt());
    }

    /** See the general contract of the readFully method of DataInput.
     * Bytes for this operation are read from the contained input stream.
     * @param b the buffer into which the data is read.
     * @throws EOFException if this input stream reaches the end before reading all the bytes.
     * @throws IOException if an I/O error occurs.
     */
    public void readFully(byte[] b)
            throws IOException,
            EOFException {
        mPosition += b.length;
        this.mDis.readFully(b);
    }

    /**Reads len bytes from an input stream.
     * This method blocks until one of the following conditions occurs:
     * <UL><LI>len bytes of input data are available, in which case a normal return is made.</LI>
     * <LI>End of file is detected, in which case an EOFException is thrown.</LI>
     * <LI>An I/O error occurs, in which case an IOException other than EOFException is thrown.</LI></UL>
     * If b is null, a NullPointerException is thrown. If off is negative, or len
     * is negative, or off+len is greater than the length of the array b, then an
     * IndexOutOfBoundsException is thrown. If len is zero, then no bytes are read.
     * Otherwise, the first byte read is stored into element b[off], the next one into
     * b[off+1], and so on. The number of bytes read is, at most, equal to len.
     * @param b the buffer into which the data is read.
     * @param off an int specifying the offset into the data.
     * @param len an int specifying the number of bytes to read.
     * @throws IOException if an I/O error occurs.
     * @throws EOFException if this stream reaches the end before reading all the bytes.
     */
    public void readFully(byte[] b, int off, int len)
            throws IOException,
            EOFException {
        mPosition += len;
        this.mDis.readFully(b, off, len);
    }

    /** See the general contract of the readInt method of DataInput.
     * Bytes for this operation are read from the contained input stream.
     * @throws IOException if an I/O error occurs.
     * @throws EOFException if this input stream reaches the end before reading four bytes.
     * @return the next four bytes of this input stream, interpreted as an int.
     */
    public int readInt()
            throws IOException,
            EOFException {
        int res = 0;
        for (int shiftBy = 0; shiftBy < 32; shiftBy += 8) {
            res |= (this.mDis.readByte() & 0xff) << shiftBy;
        }
        mPosition += 4;
        return res;
    }

    /** See the general contract of the readLong method of DataInput.
     * Bytes for this operation are read from the contained input stream.
     * @throws IOException if an I/O error occurs.
     * @throws EOFException if this input stream reaches the end before reading two bytes.
     * @return the next eight bytes of this input stream, interpreted as a long.
     */
    public long readLong()
            throws IOException,
            EOFException {
        long res = 0;
        for (int shiftBy = 0; shiftBy < 64; shiftBy += 8) {
            res |= (this.mDis.readByte() & 0xff) << shiftBy;
        }
        mPosition += 8;
        return res;
    }

    /** See the general contract of the readShort method of DataInput.
     * Bytes for this operation are read from the contained input stream.
     * @throws EOFException if this input stream reaches the end before reading two bytes.
     * @throws IOException if an I/O error occurs.
     * @return the next two bytes of this input stream, interpreted as a signed 16-bit number.
     */
    public short readShort()
            throws IOException,
            EOFException {
        final int low = readByte() & 0xff;
        final int high = readByte() & 0xff;
        mPosition += 2;
        return (short) (high << 8 | low);
    }

    /** See the general contract of the readUnsignedByte method of DataInput.
     * Bytes for this operation are read from the contained input stream.
     * @throws IOException if an I/O error occurs.
     * @throws EOFException if this input stream reaches the end before reading two bytes.
     * @return the next byte of this input stream, interpreted as an unsigned 8-bit number.
     */
    public int readUnsignedByte()
            throws IOException, EOFException {
        mPosition += 1;
        return this.mDis.readUnsignedByte();
    }

    /** See the general contract of the readUnsignedShort method of DataInput.
     * Bytes for this operation are read from the contained input stream.
     * @throws IOException if an I/O error occurs.
     * @return the next two bytes of this input stream, interpreted as an unsigned 16-bit integer.
     */
    public int readUnsignedShort()
            throws IOException {
        final int low = readByte() & 0xff;
        final int high = readByte() & 0xff;
        mPosition += 2;
        return (high << 8 | low);
    }

    /** See the general contract of the skipBytes method of DataInput.
     * Bytes for this operation are read from the contained input stream.
     * @param n the number of bytes to be skipped.
     * @throws IOException if an I/O error occurs.
     * @return the actual number of bytes skipped.
     */
    public int skipBytes(int n)
            throws IOException {
        mPosition += n;
        return this.mDis.skipBytes(n);
    }

    public char readChar() throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public double readDouble() throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String readLine() throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String readUTF() throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}