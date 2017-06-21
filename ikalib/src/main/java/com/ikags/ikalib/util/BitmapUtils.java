package com.ikags.ikalib.util;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.IntBuffer;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.media.ExifInterface;
import android.os.Environment;
import android.util.Log;
import android.view.View;

/**
 * 所有图片读取和图片操作的类
 *
 * @author zhangxiasheng
 */
public class BitmapUtils {
    private static final String TAG = "BitmapUtils";

    public static Bitmap addBitmap(Bitmap src, Bitmap watermark, float left, float top) {
        int w = src.getWidth();
        int h = src.getHeight();
        Bitmap dst = Bitmap.createBitmap(w, h, Config.ARGB_8888);
        Canvas canvas = new Canvas(dst);
        canvas.drawBitmap(BitmapUtils.setAlpha(src, 50), 0, 0, null);
        canvas.drawBitmap(BitmapUtils.scale(watermark, src.getWidth(), (int) (src.getHeight() / 3.0f)), left, top, null);
        canvas.save(Canvas.ALL_SAVE_FLAG);
        canvas.restore();
        return dst;

    }

    public static Bitmap addProgress(Bitmap src, Bitmap pro, int progress) {
        int w = pro.getWidth();
        int h = pro.getHeight();
        Bitmap dst = Bitmap.createBitmap(w, h, Config.ARGB_8888);
        Canvas canvas = new Canvas(dst);
        canvas.drawBitmap(src, 0, 0, null);
        canvas.drawBitmap(pro, 0, pro.getHeight() * (100 - progress) / 100.0f, null);
        canvas.save(Canvas.ALL_SAVE_FLAG);
        canvas.restore();
        return dst;
    }

    public static Bitmap setAlpha(Bitmap sourceImg, int number) {
        int[] argb = new int[sourceImg.getWidth() * sourceImg.getHeight()];
        sourceImg.getPixels(argb, 0, sourceImg.getWidth(), 0, 0, sourceImg
                .getWidth(), sourceImg.getHeight());
        number = number * 255 / 100;
        for (int i = 0; i < argb.length; i++) {
            argb[i] = (number << 24) | (argb[i] & 0x00FFFFFF);
        }
        sourceImg = Bitmap.createBitmap(argb, sourceImg.getWidth(), sourceImg
                .getHeight(), Config.ARGB_8888);

        return sourceImg;
    }

    public static Bitmap addWatermark(Bitmap src, float left, float top, String content, int alpha) {
        int w = src.getWidth();
        int h = src.getHeight();
        Bitmap dst = Bitmap.createBitmap(w, h, Config.ARGB_8888);
        Canvas canvas = new Canvas(dst);
        Paint p = new Paint();
        String fontName = "sans";
        Typeface font = Typeface.create(fontName, Typeface.BOLD);
        p.setColor(Color.RED);
        p.setTypeface(font);
        p.setTextSize(22);
        p.setAlpha(alpha * 255 / 100);
        canvas.drawText(content, left, top, p);
        canvas.save(Canvas.ALL_SAVE_FLAG);
        canvas.restore();
        return dst;
    }

    public static Bitmap addWatermark(Bitmap src, float left, float top, Bitmap img, int alpha) {
        int w = src.getWidth();
        int h = src.getHeight();
        Bitmap dst = Bitmap.createBitmap(w, h, Config.ARGB_8888);
        Canvas canvas = new Canvas(dst);
        Paint p = new Paint();
        canvas.drawBitmap(src, 0, 0, p);
        p.setAlpha(alpha * 255 / 100);
        canvas.drawBitmap(img, left, top, p);
        canvas.save(Canvas.ALL_SAVE_FLAG);
        canvas.restore();
        return dst;
    }

    public static Bitmap scale(Bitmap bitmap, float scaleWidth, float scaleHeight) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        return Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
    }

    public static Bitmap scale(Bitmap bitmap, int newWidth, int newHeight) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        return Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
    }

    public static Bitmap decodeOptionsFile(File f) {
        try {
            // decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(f), null, o);

            // Find the correct scale value. It should be the power of 2.
            final int REQUIRED_SIZE = 70;
            int width_tmp = o.outWidth, height_tmp = o.outHeight;
            int scale = 1;
            while (true) {
                if (width_tmp / 2 < REQUIRED_SIZE
                        || height_tmp / 2 < REQUIRED_SIZE)
                    break;
                width_tmp /= 2;
                height_tmp /= 2;
                scale += 0.5;
            }

            // decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
        } catch (FileNotFoundException e) {
//			if(DEBUG) 
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Bitmap decodeFile(File f) {
        Bitmap bitmap = null;
        try {
            IKALog.v(TAG, "decodeFile=" + f.getName());
            bitmap = BitmapFactory.decodeFile(f.getName());
        } catch (OutOfMemoryError e) {
            IKALog.v(TAG, "Bitmap decodeFile error=" + e.getMessage());
            e.printStackTrace();
        } catch (Exception ex) {
            IKALog.v(TAG, "Bitmap decodeFile error=" + ex.getMessage());
            ex.printStackTrace();
        }
        return bitmap;
    }


    public static Bitmap decodeFile(String path) {
        Bitmap bitmap = null;
        try {
            IKALog.v(TAG, "decodeFile=" + path);
            bitmap = BitmapFactory.decodeFile(path);
        } catch (OutOfMemoryError e) {
            IKALog.v(TAG, "Bitmap decodeFile error=" + e.getMessage());
            e.printStackTrace();
        } catch (Exception ex) {
            IKALog.v(TAG, "Bitmap decodeFile error=" + ex.getMessage());
            ex.printStackTrace();
        }
        return bitmap;
    }


    public static void saveBitmapFile(Bitmap mBitmap, String pathpath, String pathname) {
        try {
            IKALog.v(TAG, "pathname=" + pathname);
            File bitmappath = new File(pathpath);
            bitmappath.mkdirs();
            bitmappath = null;
            File bitmapfile = new File(pathpath + "/" + pathname);
            if (bitmapfile.exists()) {
                bitmapfile.delete();
            }
            bitmapfile.createNewFile();
            FileOutputStream fOut = new FileOutputStream(bitmapfile);
            mBitmap.compress(Bitmap.CompressFormat.PNG, 95, fOut);
            fOut.flush();
            fOut.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void saveBitmapFileType(Bitmap mBitmap, String pathname, boolean isJpg) {
        try {
            File bitmapfile = new File(pathname);
            if (bitmapfile.exists()) {
                bitmapfile.delete();
            }
            bitmapfile.createNewFile();
            FileOutputStream fOut = new FileOutputStream(bitmapfile);
            if (isJpg) {
                mBitmap.compress(Bitmap.CompressFormat.JPEG, 80, fOut);
            } else {
                mBitmap.compress(Bitmap.CompressFormat.PNG, 95, fOut);
            }
            fOut.flush();
            fOut.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 截图,截屏
     *
     * @param view
     * @return
     */
    public static Bitmap takeScreenshot(View view) {
        Bitmap bitmap = null;
        try {
            if (view != null && view.getWidth() > 0 && view.getHeight() > 0) {
                Bitmap.Config config = Bitmap.Config.ARGB_8888;
                bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), config);
                Canvas canvas = new Canvas(bitmap);
                view.draw(canvas);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return bitmap;
    }


    /**
     * openGL截图
     *
     * @param x
     * @param y
     * @param w
     * @param h
     * @param gl
     * @return
     */
    public static Bitmap SaveOpenGLPixels(int x, int y, int w, int h, GL10 gl) {
        int b[] = new int[w * h];
        int bt[] = new int[w * h];
        IntBuffer ib = IntBuffer.wrap(b);
        ib.position(0);
        gl.glReadPixels(x, y, w, h, GL10.GL_RGBA, GL10.GL_UNSIGNED_BYTE, ib);
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                int pix = b[i * w + j];
                int pb = (pix >> 16) & 0xff;
                int pr = (pix << 16) & 0x00ff0000;
                int pix1 = (pix & 0xff00ff00) | pr | pb;
                bt[(h - i - 1) * w + j] = pix1;
            }
        }
        Bitmap sb = Bitmap.createBitmap(bt, w, h, Bitmap.Config.ARGB_8888);
        return sb;

    }


    /**
     * 截图,截屏
     *
     * @param view
     * @return
     */
    public static Bitmap takeScreenshotMix(View view, Bitmap bitmap) {
        Bitmap dst = null;
        try {
            if (view != null && view.getWidth() > 0 && view.getHeight() > 0 && bitmap != null) {
//				Canvas canvas = new Canvas(bitmap);
//				view.draw(canvas);
//				canvas.save(Canvas.ALL_SAVE_FLAG);
//				canvas.restore();

                dst = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Config.ARGB_8888);
                Canvas canvas = new Canvas(dst);
                Paint p = new Paint();
                canvas.drawBitmap(bitmap, 0, 0, p);
                view.draw(canvas);
                canvas.save(Canvas.ALL_SAVE_FLAG);
                canvas.restore();

            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return dst;
    }


    public static float decodeOptionsBitmapScale(String localurl, int REQUIRED_SIZE) {
        try {
            // decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(localurl, o);
            // Find the correct scale value. It should be the power of 2.
//			final int REQUIRED_SIZE = 70;
            int width_tmp = o.outWidth, height_tmp = o.outHeight;
            float scale = 1;
            while (true) {
                if (width_tmp / 2 < REQUIRED_SIZE && height_tmp / 2 < REQUIRED_SIZE) {
                    break;
                }
                width_tmp = width_tmp / 2;
                height_tmp = height_tmp / 2;
                scale = scale + 1;
            }
            Log.v(TAG,"decodeOptionsBitmapScale size="+(1/scale)+",url="+localurl);
            return (1/scale);
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0.5f;
    }


    public static Bitmap decodeOptionsBitmap(String localurl, int REQUIRED_SIZE) {
        try {
            // decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(localurl, o);
            // Find the correct scale value. It should be the power of 2.
//			final int REQUIRED_SIZE = 70;
            int width_tmp = o.outWidth, height_tmp = o.outHeight;
            int scale = 1;
            while (true) {
                if (width_tmp / 2 < REQUIRED_SIZE && height_tmp / 2 < REQUIRED_SIZE) {
                    break;
                }
                width_tmp = width_tmp / 2;
                height_tmp = height_tmp / 2;
                scale = scale + 1;
            }

            // decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            o2.inScaled=true;
            Log.v(TAG,"decodeOptionsBitmap_final size="+width_tmp+"x"+height_tmp+"_"+scale);

            return BitmapFactory.decodeFile(localurl, o2);
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public static boolean decodeOptionsBitmapFile(String localurl, int REQUIRED_SIZE, String newpath,int degree) {
        boolean isSaveFile=false;
        try {
            Bitmap bitmap = decodeOptionsBitmap(localurl, REQUIRED_SIZE);
            if(degree>0){
                bitmap=rotaingImageView(degree,bitmap);
            }
            if (bitmap != null) {
                saveBitmapFileType(bitmap, newpath, true);
                isSaveFile = true;
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return isSaveFile;
    }


    public static String getDiskCacheDir(Context context) {
        String cachePath = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            cachePath = context.getExternalCacheDir().getPath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }
        return cachePath;
    }



    public static int readPictureDegree(String path) {
        int degree  = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }



    public static Bitmap rotaingImageView(int angle , Bitmap bitmap) {
        //旋转图片 动作
        Bitmap resizedBitmap=null;
        try {
            Matrix matrix = new Matrix();
            matrix.postRotate(angle);
            System.out.println("angle2=" + angle);
            // 创建新的图片
            resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
                    bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        }catch(Exception ex){
            ex.printStackTrace();
        }
        if(resizedBitmap==null){
            resizedBitmap=bitmap;
        }
        return resizedBitmap;
    }

}
