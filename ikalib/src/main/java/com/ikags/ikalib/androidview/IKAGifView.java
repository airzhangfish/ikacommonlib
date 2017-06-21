package com.ikags.ikalib.androidview;

import java.io.InputStream;
import java.util.ArrayList;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.ikags.ikalib.gameutil.gif.GifAction;
import com.ikags.ikalib.gameutil.gif.GifDecoder2;
import com.ikags.ikalib.gameutil.gif.GifDecoder2.ColorsFrame;
/**
 * GifView<br>
 * 本类可以显示一个gif动画，其使用方法和android的其它view（如imageview)一样。<br>
 * 如果要显示的gif太大，会出现OOM的问题。
 * 用法:
 *     InputStream is=this.getAssets().open("background_test.gif");<br/>
 *     gifViewback.setGifImage(is, false);
 * @author liao
 */
public class IKAGifView extends View implements GifAction {

    public static final String TAG="IKAGifView";
    /**
     * gif解码器
     */
    private GifDecoder2 gifDecoder2=null;
    /**
     * 当前要画的帧的图
     */
    private ColorsFrame currentImage=null;

    private boolean isRun=true;
    public boolean isFaceRight=true;

    private boolean pause=false;

    private int showWidth=-1;
    private int showHeight=-1;
    //private Rect rect=null;
    boolean isShowOne=false;

    public void setRun(boolean run) {
        isRun=run;
    }

    public void setPause(boolean pause) {
        this.pause=pause;
    }

    private DrawThread drawThread=null;

    private GifImageType animationType=GifImageType.ANIMATION;
    Paint paint;

    /**
     * 解码过程中，Gif动画显示的方式<br>
     * 如果图片较大，那么解码过程会比较长，这个解码过程中，gif如何显示
     *
     * @author liao
     */
    public enum GifImageType {
        /**
         * 在解码过程中，不显示图片，直到解码全部成功后，再显示，废除
         */
        WAIT_FINISH(0),
        /**
         * 和解码过程同步，解码进行到哪里，图片显示到哪里，废除
         */
        SYNC_DECODER(1),
        /**
         * 只显示第一帧图片
         */
        COVER(2),
        /**
         * 动画显示所有帧
         */
        ANIMATION(3);

        GifImageType(int i) {
            nativeInt=i;
        }

        final int nativeInt;
    }

    public IKAGifView(Context context) {
        super(context);
    }

    public IKAGifView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IKAGifView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        paint=new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    /**
     * 设置图片，并开始解码
     *
     * @param gif 要设置的图片
     */
    @Deprecated
    public void setGifDecoderImage(byte[] gif) {
        if (gifDecoder2!=null) {
            gifDecoder2.setStatus(1);
            gifDecoder2.free();
            gifDecoder2=null;
        }
        gifDecoder2=new GifDecoder2(gif, this,isFaceRight);
        gifDecoder2.start();
    }

    /**
     * 设置图片，开始解码
     *
     * @param is 要设置的图片
     */
    private void setGifDecoderImage(InputStream is, boolean isShowOne,boolean faceright) {
        isFaceRight=faceright;
        Log.d(TAG, "setGifDecoderImage.isShowOne:"+isShowOne);
        if (gifDecoder2!=null) {
            gifDecoder2.setStatus(GifDecoder2.STATUS_FINISH);
            gifDecoder2.free();
            gifDecoder2=null;
        }
        /*try {
            Log.d(TAG, "getNativeHeapSize:"+Debug.getNativeHeapSize()+
            " getNativeHeapAllocatedSize:"+Debug.getNativeHeapAllocatedSize()+
            " getThreadAllocSize:"+Debug.getThreadAllocSize()+
            " getThreadExternalAllocSize:"+Debug.getThreadExternalAllocSize()+
            " size:"+is.available()+" getGlobalExternalAllocSize:"+Debug.getGlobalExternalAllocSize()+
            " getGlobalExternalFreedSize:"+ Debug.getGlobalExternalFreedSize());
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        this.isShowOne=isShowOne;
        gifDecoder2=new GifDecoder2(is, this,isFaceRight);
        gifDecoder2.setIs_show_one(isShowOne);
        gifDecoder2.start();
    }

    /**
     * 以字节数据形式设置gif图片
     *
     * @param gif 图片
     */
    public void setGifImage(byte[] gif) {
        setGifDecoderImage(gif);
    }

    /**
     * 以字节流形式设置gif图片
     *
     * @param is 图片
     */
    public void setGifImage(InputStream is, boolean isShowOne,boolean isfaceright) {
        setGifDecoderImage(is, isShowOne, isfaceright);
    }

    /**
     * 以资源形式设置gif图片
     *
     * @param resId gif图片的资源ID
     */
    public void setGifImage(int resId, boolean isShowOne,boolean isfaceright) {
        Log.d(TAG, "setGifImage.");
        Resources r=this.getResources();
        InputStream is=r.openRawResource(resId);
        setGifDecoderImage(is, isShowOne, isfaceright);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (this.isShowOne) {
        } else {
            if (gifFrames==null||frameLength<1) {
                Log.d(TAG, "gifFrames:"+frameLength);
                return;
            }

            if (currentImage==null) {
                currentImage=gifFrames.get(currImageIdx);
            }
        }

        //Log.d(TAG, "currentImage:"+currentImage);
        if (currentImage==null) {
            return;
        }

        int saveCount=canvas.getSaveCount();
        canvas.save();
        canvas.translate(getPaddingLeft(), getPaddingTop());

        if (showWidth==-1) {
            //canvas.drawBitmap(currentImage, 0, 0, null);
            canvas.drawBitmap(currentImage.colors, 0, currentImage.width, 0, 0, currentImage.width,
                    currentImage.height, currentImage.transparency, null);
        } else {
            //canvas.drawBitmap(currentImage, null, rect, null);
            canvas.drawBitmap(currentImage.colors, 0, currentImage.width, 0, 0, currentImage.width,
                    currentImage.height, currentImage.transparency, null);
        }

        canvas.restoreToCount(saveCount);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //Log.d(TAG, "onMeasure:"+widthMeasureSpec+" height:"+heightMeasureSpec);
        int pleft=getPaddingLeft();
        int pright=getPaddingRight();
        int ptop=getPaddingTop();
        int pbottom=getPaddingBottom();

        int widthSize;
        int heightSize;

        int w;
        int h;

        if (gifDecoder2==null) {
            w=1;
            h=1;
        } else {
            w=gifDecoder2.width;
            h=gifDecoder2.height;
        }

        w+=pleft+pright;
        h+=ptop+pbottom;

        w=Math.max(w, getSuggestedMinimumWidth());
        h=Math.max(h, getSuggestedMinimumHeight());

        widthSize=resolveSize(w, widthMeasureSpec);
        heightSize=resolveSize(h, heightMeasureSpec);

        //Log.d(TAG, "widthSize:"+widthSize+" heightSize:"+heightSize+" w:"+w+" h:"+h);

        setMeasuredDimension(widthSize, heightSize);
    }

    /**
     * 设置gif在解码过程中的显示方式<br>
     * <strong>本方法只能在setGifImage方法之前设置，否则设置无效</strong>
     *
     * @param type 显示方式
     */
    public void setGifImageType(GifImageType type) {
        if (gifDecoder2==null) {
            animationType=type;
        }
    }

    /**
     * 设置要显示的图片的大小<br>
     * 当设置了图片大小 之后，会按照设置的大小来显示gif（按设置后的大小来进行拉伸或压缩）
     *
     * @param width  要显示的图片宽
     * @param height 要显示的图片高
     */
    public void setShowDimension(int width, int height) {
        Log.d(TAG, "setShowDimension.width:"+width+" height:"+height);
        if (width>0&&height>0) {
            showWidth=width;
            showHeight=height;
            /*rect=new Rect();
            rect.left=0;
            rect.top=0;
            rect.right=width;
            rect.bottom=height;*/
            requestLayout();
            invalidate();
        }
    }

    @Override
    public void parseOk(boolean parseStatus, int frameIndex) {
        Log.d(TAG, "parseOk.frameIndex:"+frameIndex);
        decodeFinish(parseStatus, frameIndex);
    }

    private void decodeFinish(boolean parseStatus, int frameIndex) {
        if (!parseStatus&&gifDecoder2.getFrameArrayList().size()<1) {
            Log.d(TAG, "解析失败。");
            /*if (null!=imageLoadCallback) {
                imageLoadCallback.loadError();
            }*/
            return;
        }

        if (gifDecoder2==null) {
            Log.d(TAG, "前一次解析放弃。");
            /*if (null!=imageLoadCallback) {
                   imageLoadCallback.loadError();
               }*/
            return;
        }

        gifFrames=gifDecoder2.getFrameArrayList();
        currImageIdx=0;
        frameLength=gifFrames.size();
        currentImage=gifFrames.get(0);

        //if (rect==null) {
        mHandler.post(new Runnable() {

            @Override
            public void run() {
                /*if (null!=imageLoadCallback) {
                    imageLoadCallback.loadFinish();
                }*/

                //Bitmap bitmap=gifFrames.get(0).image;
                int width=currentImage.width;
                int height=currentImage.height;
                Log.d(TAG, "gif帧间隔为："+currentImage.delay);
                setShowDimension(width, height);
            }
        });
        //}

        //System.gc();
        startAnimate();

        /*gifDecoder2.free();
        gifDecoder2=null;*/
    }

    /*@Override
    public void dispatchWindowFocusChanged(boolean hasFocus){
        Log.d(TAG, "dispatchWindowFocusChanged:"+hasFocus);
    }*/

    //这个方法不一定执行.如果没有销毁资源,会导致cpu与内存占用率很高.
    /*@Override
    public void dispatchWindowVisibilityChanged(int visibility) {
        Log.d(TAG, "dispatchWindowVisibilityChanged:"+visibility);
        if (visibility==GONE||visibility==INVISIBLE) {
            stopAnimate();
        }
    }*/

    public void startAnimate() {
        Log.d(TAG, "startAnimate.animationType:"+animationType);
        switch (animationType) {
            case ANIMATION:
                Log.d(TAG, "ANIMATION.");
                if (frameLength>1) {
                    if (drawThread==null) {
                        drawThread=new DrawThread();
                    } else {
                        drawThread.interrupt();
                        drawThread=new DrawThread();
                    }
                    drawThread.start();
                } else if (frameLength==1) {
                    try {
                        currentImage=gifFrames.get(0);
                    } catch (Exception e) {
                        Log.d(TAG, "没有图片。");
                    }
                    reDraw();
                    reDraw();
                }
                break;

            case COVER:
                Log.d(TAG, "COVER.");

                ColorsFrame frame=gifFrames.get(currImageIdx++);
                if (currImageIdx>=frameLength) {
                    currImageIdx=0;//重新播放。
                }

                currentImage=frame;
                reDraw();
                break;
        }
    }

    /**
     * 停止动画与一切解码相关的操作.
     */
    public void stopAnimate() {
        Log.d(TAG, "stopAnimate.");
        frameLength=0;
        gifFrames.clear();
        isRun=false;
        pause=true;
        if (gifDecoder2!=null) {
            try {
                gifDecoder2.setStatus(GifDecoder2.STATUS_FORMAT_ERROR);
                gifDecoder2.free();
                gifDecoder2=null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.gc();
    }

    private void reDraw() {
        Log.d(TAG, "reDraw.");
        if (mHandler!=null) {
            Message msg=mHandler.obtainMessage();
            mHandler.sendMessage(msg);
        }
    }

    private Handler mHandler=new Handler() {

        @Override
        public void handleMessage(Message msg) {
            invalidate();
        }
    };

    /**
     * 动画线程
     *
     * @author liao
     */
    private class DrawThread extends Thread {

        @Override
        public void run() {
            Log.d(TAG, "DrawThread.run.isRun:"+isRun+" pause:"+pause);
            if (gifFrames==null||frameLength<1) {
                Log.d(TAG, "run.gifFrames is null.");
                return;
            }

            while (isRun) {
                ColorsFrame frame=gifFrames.get(currImageIdx++);
                if (currImageIdx>=frameLength) {
                    currImageIdx=0;//重新播放。
                    //break;
                }

                currentImage=frame;
                if (pause==false) {
                    long delay=frame.delay;
                    //Log.d(TAG, "run.currentImage:"+currentImage+" pause:"+pause+" isRun:"+isRun+" delay:"+delay);
                    Message msg=mHandler.obtainMessage();
                    mHandler.sendMessage(msg);
                    SystemClock.sleep(delay);
                } else {
                    break;
                }
            }

            Log.d(TAG, "finish run.");
        }
    }

    //////----------------------
    ArrayList<GifDecoder2.ColorsFrame> gifFrames=new ArrayList<GifDecoder2.ColorsFrame>();
    //存储帧,当前帧不应该太多,如果一个gif较大,如超过8m会是个问题.
    int currImageIdx=0;//当前显示的解析图片索引
    int frameLength=0; //帧的长度

    //回调方法,通过它可以回调解码失败或成功后的一些操作.
    /*ImageViewerDialog.IImageLoadCallback imageLoadCallback;

    public void setImageLoadCallback(ImageViewerDialog.IImageLoadCallback imageLoadCallback) {
        this.imageLoadCallback=imageLoadCallback;
    }*/
}