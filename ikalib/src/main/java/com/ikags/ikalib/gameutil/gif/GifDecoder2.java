package com.ikags.ikalib.gameutil.gif;

import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
/**
 * gif解码
 *
 * @author archko
 */
public class GifDecoder2 extends Thread {

    public static final String TAG="GifDecoder2";
    /**
     * 状态：正在解码中
     */
    public static final int STATUS_PARSING=0;
    /**
     * 状态：图片格式错误
     */
    public static final int STATUS_FORMAT_ERROR=1;
    /**
     * 状态：打开失败
     */
    public static final int STATUS_OPEN_ERROR=2;
    /**
     * 状态：解码成功
     */
    public static final int STATUS_FINISH=-1;

    private InputStream in;//需要解码的流
    private byte[] gifData=null;//需要解码的数组
    private int status;

    public int width; // full image width
    public int height; // full image height
    private boolean gctFlag; // global color table used
    private int gctSize; // size of global color table
    private int loopCount=1; // iterations; 0 = repeat forever

    private int[] gct; // global color table
    private int[] lct; // local color table
    private int[] act; // active color table

    private int bgIndex; // background color index
    private int bgColor; // background color
    private int lastBgColor; // previous bg color
    private int pixelAspect; // pixel aspect ratio

    private boolean lctFlag; // local color table flag
    private boolean interlace; // interlace flag
    private int lctSize; // local color table size

    private int ix, iy, iw, ih; // current image rectangle
    private int lrx, lry, lrw, lrh;
    //private Bitmap image; // current frame
    //private Bitmap lastImage; // previous frame
    //private ColorsFrame colors;// current frame
    private ColorsFrame lastColors; // previous frame

    private boolean isShow=false;

    private byte[] block=new byte[256]; // current data block
    private int blockSize=0; // block size

    // last graphic control extension info
    private int dispose=0;
    // 0=no action; 1=leave in place; 2=restore to bg; 3=restore to prev
    private int lastDispose=0;
    private boolean transparency=false; // use transparent color
    private int delay=0; // delay in milliseconds
    private int transIndex; // transparent color index
    public boolean isFaceRight=true;
    private static final int MaxStackSize=4096;
    // max decoder pixel stack size

    // LZW decoder working arrays
    private short[] prefix;
    private byte[] suffix;
    private byte[] pixelStack;
    private byte[] pixels;

    //private GifFrame gifFrame; // frames read from current file
    private int frameCount;

    private GifAction action=null;
    ArrayList<ColorsFrame> colorsArr;//存放解析的帧。据说放rgb数组会更高效，特别是图片颜色变化多时。
    boolean is_show_one=false;

    public void setIs_show_one(boolean is_show_one) {
        this.is_show_one=is_show_one;
    }

    public void setStatus(int status) {
        this.status=status;
    }

    public class ColorsFrame {

        private ColorsFrame(int[] colors, int delay, int width, int height,boolean transparency) {
            this.colors=colors;
            this.delay=delay;
            this.width=width;
            this.height=height;
            this.transparency=transparency;

            System.out.println("\n fc:"+colorsArr.size()+" delay:"+delay+" width:"+width+" height:"+height+" transparency:"+transparency);
            for(int i=0;i<colors.length;i++) {
                System.out.print(colors[i]);
            }
        }

        public int[] colors;
        public int delay;
        public int width;
        public int height;
        public boolean transparency;
    }

    public ArrayList<ColorsFrame> getFrameArrayList() {
        return colorsArr;
    }

    public GifDecoder2(byte[] data, GifAction act,boolean faceright) {
        isFaceRight=faceright;
        gifData=data;
        action=act;
        colorsArr=new ArrayList<ColorsFrame>();
    }

    public GifDecoder2(InputStream is, GifAction act,boolean faceright) {
        isFaceRight=faceright;
        in=is;
        action=act;
        colorsArr=new ArrayList<ColorsFrame>();
    }

    /**
     * 释放资源
     */
    public void free() {
        Log.d(TAG, "free.");
        colorsArr=null;
        /*gct=null;
        lct=null;
        act=null;
        prefix=null;
        suffix=null;
        pixelStack=null;
        pixels=null;
        lastColors=null;*/

        if (in!=null) {
            try {
                in.close();
            } catch (Exception ex) {
            }
            in=null;
        }
        gifData=null;
        //resetFrame();
    }

    @Override
    public void run() {
        Log.d(TAG, "run.");
        if (in!=null) {
            readStream();
        } else if (gifData!=null) {
            readByte();
        }
        transLRs();
    }

    private void transLRs(){
        try{
            if(!isFaceRight){
                for(int i=0;i<colorsArr.size();i++){
                    ColorsFrame cf=colorsArr.get(i);
                    cf.colors=transLRFrame(cf.colors);
                }
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    private int[] transLRFrame(int[] dest){
        int[] newsdest=new int[width*height];
        try{
            //TODO int[] dest左右转换
            for(int j=0;j<height;j++){
                for(int i=0;i<width;i++){
                    newsdest[(j+1)*width-i-1]=dest[j*width+i];
                }
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return newsdest;
    }


    private void setPixels() {
        try {
            int[] dest=new int[width*height];//这里申请内存会溢出。
            // fill in starting image contents based on last image's dispose code
            if (lastDispose>0) {
                if (lastDispose==3) {
                    // use image before last
                    int n=frameCount-2;
                    if (n>0) {
                        lastColors=getColorFrame(n-1);
                    } else {
                        lastColors = null;
                    }
                }
                //Log.d(TAG, "lastColors:"+lastColors+" lastDispose:"+lastDispose+" n:"+frameCount);
                if (lastColors!=null) {
                    //dest=lastColors.colors;

                    System.arraycopy(lastColors.colors, 0, dest, 0, lastColors.colors.length);

                    // copy pixels
                    if (lastDispose==2) {
                        // fill last image rect area with background color
                        int c=0;
                        if (!transparency) {
                            c=lastBgColor;
                        }
                        for (int i=0; i<lrh; i++) {
                            int n1=(lry+i)*width+lrx;
                            int n2=n1+lrw;
                            for (int k=n1; k<n2; k++) {
                                dest[k]=c;
                            }
                        }
                    }
                }
            }
            /*if (dest==null) {
                dest=new int[width*height];
            }*/

            // copy each source line to the appropriate place in the destination
            int pass=1;
            int inc=8;
            int iline=0;
            for (int i=0; i<ih; i++) {
                int line=i;
                if (interlace) {
                    if (iline>=ih) {
                        pass++;
                        switch (pass) {
                            case 2:
                                iline=4;
                                break;
                            case 3:
                                iline=2;
                                inc=4;
                                break;
                            case 4:
                                iline=1;
                                inc=2;
                        }
                    }
                    line=iline;
                    iline+=inc;
                }

                line+=iy;
                if (line<height) {
                    int k=line*width;
                    int dx=k+ix; // start of line in dest
                    int dlim=dx+iw; // end of dest line
                    if ((k+width)<dlim) {
                        dlim=k+width; // past dest edge
                    }
                    int sx=i*iw; // start of line in source
                    while (dx<dlim) {
                        // map color and insert in destination
                        int index=((int) pixels[sx++])&0xff;
                        int c=act[index];
                        if (c!=0) {
                            dest[dx]=c;
                        }
                        dx++;
                    }
                }
            }



            lastColors=new ColorsFrame(dest, delay, width, height, transparency);
            colorsArr.add(lastColors);
        } catch (OutOfMemoryError e) {
            Log.d(TAG, "colorsArr.size():"+colorsArr.size()+"-"+colorsArr.get(0).colors.length);
            status=STATUS_FINISH;
            e.printStackTrace();
        } catch (Exception e) {
            Log.d(TAG, "出错了。");
            status=STATUS_FINISH;
        }
    }

    public ColorsFrame getColorFrame(int n) {
        Log.d(TAG, "getColorFrame.n:"+n);
        ColorsFrame colorsFrame=colorsArr.get(n);

        if (colorsFrame==null) {
            return null;
        } else {
            return colorsFrame;
        }
    }

    private int readByte() {
        Log.d(TAG, "readByte");
        in=new ByteArrayInputStream(gifData);
        gifData=null;
        return readStream();
    }

    /*public int read(byte[] data) {
        InputStream is=new ByteArrayInputStream(data);
        return read(is);
    }*/

    private int readStream() {
        Log.d(TAG, "readStream.");
        init();
        if (in!=null) {
            readHeader();
            if (!err()) {
                readContents();
                if (frameCount<0) {
                    status=STATUS_FORMAT_ERROR;
                    Log.d(TAG, "readContents 解析gif失败。");
                    action.parseOk(false, -1);
                } else {
                    status=STATUS_FINISH;
                    Log.d(TAG, "总解析帧数:"+(colorsArr==null?0:colorsArr.size()));
                    action.parseOk(true, -1);
                }
            } else {
                Log.d(TAG, "readHeader.解析gif失败。");
                action.parseOk(false, -1);
            }

            try {
                if (null!=in) {
                    in.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            status=STATUS_OPEN_ERROR;
            Log.d(TAG, "解析的gif数据为空。");
            action.parseOk(false, -1);
        }
        return status;
    }

    private void decodeImageData() {
        //Log.d(TAG, "decodeImageData");
        int NullCode=-1;
        int npix=iw*ih;
        int available, clear, code_mask, code_size, end_of_information, in_code, old_code, bits, code, count, i, datum, data_size, first, top, bi, pi;

        if ((pixels==null)||(pixels.length<npix)) {
            pixels=new byte[npix]; // allocate new pixel array
        }
        if (prefix==null) {
            prefix=new short[MaxStackSize];
        }
        if (suffix==null) {
            suffix=new byte[MaxStackSize];
        }
        if (pixelStack==null) {
            pixelStack=new byte[MaxStackSize+1];
        }
        // Initialize GIF data stream decoder.
        data_size=read();
        clear=1<<data_size;
        end_of_information=clear+1;
        available=clear+2;
        old_code=NullCode;
        code_size=data_size+1;
        code_mask=(1<<code_size)-1;
        for (code=0; code<clear; code++) {
            prefix[code]=0;
            suffix[code]=(byte) code;
        }

        // Decode GIF pixel stream.
        datum=bits=count=first=top=pi=bi=0;
        for (i=0; i<npix; ) {
            if (top==0) {
                if (bits<code_size) {
                    // Load bytes until there are enough bits for a code.
                    if (count==0) {
                        // Read a new data block.
                        count=readBlock();
                        if (count<=0) {
                            break;
                        }
                        bi=0;
                    }
                    datum+=(((int) block[bi])&0xff)<<bits;
                    bits+=8;
                    bi++;
                    count--;
                    continue;
                }
                // Get the next code.
                code=datum&code_mask;
                datum>>=code_size;
                bits-=code_size;

                // Interpret the code
                if ((code>available)||(code==end_of_information)) {
                    break;
                }
                if (code==clear) {
                    // Reset decoder.
                    code_size=data_size+1;
                    code_mask=(1<<code_size)-1;
                    available=clear+2;
                    old_code=NullCode;
                    continue;
                }
                if (old_code==NullCode) {
                    pixelStack[top++]=suffix[code];
                    old_code=code;
                    first=code;
                    continue;
                }
                in_code=code;
                if (code==available) {
                    pixelStack[top++]=(byte) first;
                    code=old_code;
                }
                while (code>clear) {
                    pixelStack[top++]=suffix[code];
                    code=prefix[code];
                }
                first=((int) suffix[code])&0xff;
                // Add a new string to the string table,
                if (available>=MaxStackSize) {
                    break;
                }
                pixelStack[top++]=(byte) first;
                prefix[available]=(short) old_code;
                suffix[available]=(byte) first;
                available++;
                if (((available&code_mask)==0)
                        &&(available<MaxStackSize)) {
                    code_size++;
                    code_mask+=available;
                }
                old_code=in_code;
            }

            // Pop a pixel off the pixel stack.
            top--;
            pixels[pi++]=pixelStack[top];
            i++;
        }
        for (i=pi; i<npix; i++) {
            pixels[i]=0; // clear missing pixels
        }
    }

    private boolean err() {
        return status!=STATUS_PARSING;
    }

    private void init() {
        status=STATUS_PARSING;
        frameCount=0;
        //gifFrame=null;
        gct=null;
        lct=null;
        System.gc();
    }

    private int read() {
        //Log.d(TAG,"read.");
        int curByte=0;
        try {

            curByte=in.read();
        } catch (Exception e) {
            status=STATUS_FORMAT_ERROR;
        }
        return curByte;
    }

    private int readBlock() {
        //Log.d(TAG,"readBlock.");
        blockSize=read();
        int n=0;
        if (blockSize>0) {
            try {
                int count=0;
                while (n<blockSize) {
                    count=in.read(block, n, blockSize-n);
                    if (count==-1) {
                        break;
                    }
                    n+=count;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (n<blockSize) {
                status=STATUS_FORMAT_ERROR;
            }
        }
        return n;
    }

    private int[] readColorTable(int ncolors) {
        //Log.d(TAG, "readColorTable.");
        int nbytes=3*ncolors;
        int[] tab=null;
        byte[] c=new byte[nbytes];
        int n=0;
        try {
            n=in.read(c);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (n<nbytes) {
            status=STATUS_FORMAT_ERROR;
        } else {
            tab=new int[256]; // max size to avoid bounds checks
            int i=0;
            int j=0;
            while (i<ncolors) {
                int r=((int) c[j++])&0xff;
                int g=((int) c[j++])&0xff;
                int b=((int) c[j++])&0xff;
                tab[i++]=0xff000000|(r<<16)|(g<<8)|b;
            }
        }
        return tab;
    }

    private void readContents() {
        Log.d(TAG, "readContents.");
        // read GIF file content blocks
        boolean done=false;
        while (!(done||err())) {
            int code=read();
            switch (code) {
                case 0x2C: // image separator
                    readImage();
                    break;
                case 0x21: // extension
                    code=read();
                    switch (code) {
                        case 0xf9: // graphics control extension
                            readGraphicControlExt();
                            break;
                        case 0xff: // application extension
                            readBlock();
                            String app="";
                            for (int i=0; i<11; i++) {
                                app+=(char) block[i];
                            }
                            if (app.equals("NETSCAPE2.0")) {
                                readNetscapeExt();
                            } else {
                                skip(); // don't care
                            }
                            break;
                        default: // uninteresting extension
                            skip();
                    }
                    break;
                case 0x3b: // terminator
                    done=true;
                    break;
                case 0x00: // bad byte, but keep going and see what happens
                    break;
                default:
                    status=STATUS_FORMAT_ERROR;
            }
        }
    }

    private void readGraphicControlExt() {
        //Log.d(TAG, "readGraphicControlExt.");
        read(); // block size
        int packed=read(); // packed fields
        dispose=(packed&0x1c)>>2; // disposal method
        if (dispose==0) {
            dispose=1; // elect to keep old image if discretionary
        }
        transparency=(packed&1)!=0;
        delay=readShort()*10; // delay in milliseconds
        if(delay<11){
            delay=70;
        }
        transIndex=read(); // transparent color index
        read(); // block terminator
    }

    private void readHeader() {
        Log.d(TAG, "readHeader.");
        String id="";
        for (int i=0; i<6; i++) {
            id+=(char) read();
        }
        if (!id.startsWith("GIF")) {
            status=STATUS_FORMAT_ERROR;
            return;
        }
        readLSD();
        if (gctFlag&&!err()) {
            gct=readColorTable(gctSize);
            bgColor=gct[bgIndex];
        }
    }

    private void readImage() {
        //Log.d(TAG, "readImage.");
        ix=readShort(); // (sub)image position & size
        iy=readShort();
        iw=readShort();
        ih=readShort();
        int packed=read();
        lctFlag=(packed&0x80)!=0; // 1 - local color table flag
        interlace=(packed&0x40)!=0; // 2 - interlace flag
        // 3 - sort flag
        // 4-5 - reserved
        lctSize=2<<(packed&7); // 6-8 - local color table size
        if (lctFlag) {
            lct=readColorTable(lctSize); // read table
            act=lct; // make local table active
        } else {
            act=gct; // make global table active
            if (bgIndex==transIndex) {
                bgColor=0;
            }
        }
        int save=0;
        if (transparency) {
            save=act[transIndex];
            act[transIndex]=0; // set transparent color if specified
        }
        if (act==null) {
            status=STATUS_FORMAT_ERROR; // no color table defined
        }
        if (err()) {
            return;
        }
        decodeImageData(); // decode pixel data
        skip();
        if (err()) {
            return;
        }

        setPixels(); // transfer pixel data to image
        frameCount++;

        // list
        if (transparency) {
            act[transIndex]=save;
        }
        resetFrame();
        //action.parseOk(true,frameCount);
        //Log.d(TAG, "解析一帧：现在总帧数："+colorsArr.size());
        if(is_show_one){
            status=STATUS_FINISH;
        }
    }

    private void readLSD() {
        Log.d(TAG, "readLSD.");
        // logical screen size
        width=readShort();
        height=readShort();
        // packed fields
        int packed=read();
        gctFlag=(packed&0x80)!=0; // 1 : global color table flag
        // 2-4 : color resolution
        // 5 : gct sort flag
        gctSize=2<<(packed&7); // 6-8 : gct size
        bgIndex=read(); // background color index
        pixelAspect=read(); // pixel aspect ratio
    }

    private void readNetscapeExt() {
        Log.d(TAG, "readNetscapeExt.");
        do {
            readBlock();
            if (block[0]==1) {
                // loop count sub-block
                int b1=((int) block[1])&0xff;
                int b2=((int) block[2])&0xff;
                loopCount=(b2<<8)|b1;
            }
        } while ((blockSize>0)&&!err());
    }

    private int readShort() {
        // read 16-bit value, LSB first
        return read()|(read()<<8);
    }

    private void resetFrame() {
        lastDispose=dispose;
        lrx=ix;
        lry=iy;
        lrw=iw;
        lrh=ih;
        //lastImage=image;
        lastBgColor=bgColor;
        dispose=0;
        transparency=false;
        delay=0;
        lct=null;

        //gct=null;
        act=null;
        prefix=null;
        suffix=null;
        pixelStack=null;
        pixels=null;
        //lastColors=null;
    }

    /**
     * Skips variable length blocks up to and including next zero length block.
     */
    private void skip() {
        do {
            readBlock();
        } while ((blockSize>0)&&!err());
    }
}
