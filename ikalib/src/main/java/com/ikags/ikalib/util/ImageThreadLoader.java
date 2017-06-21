package com.ikags.ikalib.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.widget.ImageView;



import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by airzhangfish on 2016/9/1.
 */
public class ImageThreadLoader {


    public static final String TAG = "ImageThreadLoader";
    private static ImageThreadLoader mSingleInstance;
    private Context mContext;

    static public ImageThreadLoader getDefault(Context context) {
        if (mSingleInstance == null) {
            mSingleInstance = new ImageThreadLoader(context);
        }
        return mSingleInstance;
    }


    public ImageThreadLoader(Context context) {
        mContext = context.getApplicationContext();
    }

    ExecutorService fixedThreadPool = null;


    ArrayList<ImageLoadItem> mlist = new ArrayList<ImageLoadItem>();

    public void addImageViewLoad(final String localpath, final ImageView imgView, final int maxsize) {
        if (fixedThreadPool == null) {
            fixedThreadPool = Executors.newFixedThreadPool(6);
        }



        for (int i = 0; i < mlist.size(); i++) {
            ImageLoadItem mImageLoadItem = mlist.get(i);
            if (!TextUtils.isEmpty(mImageLoadItem.url) && mImageLoadItem.url.equals(localpath)) {
                if (mImageLoadItem.bitmap != null) {
                    imgView.setImageBitmap(mImageLoadItem.bitmap);
                    return;
                }
            }
        }


        if (mlist.size() > 150) {
            for (int i = 0; i < 75; i++) {
                ImageLoadItem mImageLoadItem = mlist.get(0);
                if (mImageLoadItem.bitmap != null) {
                    mImageLoadItem.bitmap.recycle();
                    mImageLoadItem.bitmap = null;
                }
                mlist.remove(0);
            }
        }

        //TODO imgView.setImageResource(R.drawable.default_photo);
        fixedThreadPool.execute(new Runnable() {
            public void run() {
                try {
                    ImageLoadItem ili = new ImageLoadItem();
                    ili.imageview = imgView;
                    ili.url = localpath;
                    ili.bitmap = BitmapUtils.decodeOptionsBitmap(ili.url, maxsize);
                    Message msg = new Message();
                    msg.obj = ili;
                    mhandler.sendMessage(msg);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


    }


    Handler mhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try {
                ImageLoadItem ili = (ImageLoadItem) msg.obj;
                ili.imageview.setImageBitmap(ili.bitmap);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }
    };


}

