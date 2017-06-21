package com.ikags.ikalib.gameutil.anime;

import java.io.InputStream;
import java.util.HashMap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.ikags.ikalib.gameutil.anime.info.AnimeInfo;
import com.ikags.ikalib.gameutil.anime.info.FrameInfo;
import com.ikags.ikalib.gameutil.anime.info.ModInfo;
import com.ikags.ikalib.util.IKALog;

/**
 * I.K.A Engine<BR>
 * AnimeEngine类，动画功能类，和动编联动。<BR>
 * 使用方法:<BR>
 * 创建方法:<BR>
 * AnimeEngine.imgSprite_pool[0]=new AnimeSprite("/logo.ika","/logo.bin");<BR>
 * GSprite gsprite=new GSprite(104,136,0,0,true);<BR>
 * 使用方法:<BR>
 * gsprite.paint(g);<BR>
 * 清理内存方法:<BR>
 * gsprite=null;<BR>
 * AnimeEngine.imgSprite_pool[0].setNull();<BR>
 * AnimeEngine.imgSprite_pool[0]=null;<BR>
 * System.gc();<BR>
 * @author http://airzhangfish.spaces.live.com
 * @since 2005.11.15 最后更新 2009.5.20
 * @version 0.6
 */
public class AnimeEngine {
    Context mContext = null;
    private static AnimeEngine ae = null;

    public static AnimeEngine getInstance() {
        if (ae == null) {
            ae = new AnimeEngine();
        }
        return ae;
    }


    public static AnimeEngine getInstance(Context context) {
        if (ae == null) {
            ae = new AnimeEngine(context);
        }
        return ae;
    }


    public AnimeEngine(Context context) {
        mContext = context;
    }

    /**
     * 动画池,用于储存动画,默认9组空间
     */
    public AnimeSprite[] imgSprite_pool = new AnimeSprite[9];

    /**
     * 初始化
     */
    public AnimeEngine() {
    }

    /**
     * 动画图片读取
     *
     * @param path       图片bin文件路径
     * @param img_sprite 动画精灵名
     */
    public void load_Image(String path, AnimeSprite img_sprite) { // 动画图片读取
        // 读取图片

        try {
            InputStream is = mContext.getAssets().open(path);
            byte[] piece = new byte[4];
            piece[0] = (byte) is.read();
            piece[1] = (byte) is.read();
            piece[2] = (byte) is.read();
            piece[3] = (byte) is.read();
            img_sprite.anime_Piece = bytesToInt(piece);
            System.out.println("total PNG:" + img_sprite.anime_Piece);
            img_sprite.AnimeImg = new Bitmap[img_sprite.anime_Piece];
            for (int i = 0; i < img_sprite.anime_Piece; i++) {
                byte[] image_matrix;
                byte[] bys = new byte[4];
                bys[0] = (byte) is.read();
                bys[1] = (byte) is.read();
                bys[2] = (byte) is.read();
                bys[3] = (byte) is.read();
                int image_length = bytesToInt(bys);
                System.out.println(i + " png size=" + image_length);
                image_matrix = new byte[image_length];
                is.read(image_matrix, 0, image_length);
//                img_sprite.AnimeImg[i] = Image.createImage(image_matrix, 0, image_length);
                img_sprite.AnimeImg[i] = BitmapFactory.decodeByteArray(image_matrix, 0, image_length);
            }
        } catch (Exception e) {
            System.out.println("images read error");
        }
    }

    /**
     * 动画数据文件读取
     *
     * @param path       图片bin文件路径
     * @param img_sprite 动画精灵名
     */
    public void load_Anime(String path, AnimeSprite img_sprite) {
        short[] inputintdata;
        try {
//            InputStream fo = "i".getClass().getResourceAsStream(path);
            InputStream fo = mContext.getAssets().open(path);
            IKALog.v("anime_read", "START-1=" + fo.available());
            //读取bytes
            inputintdata = new short[(fo.available() - 3) / 2];
            //清空三个头字段
            fo.read();
            fo.read();
            fo.read();
            for (int i = 0; i < inputintdata.length; i++) {
                inputintdata[i] = getShort(fo);
            }
            fo.close();

            //读取frame
            IKALog.v("anime_read", "START-2");
            int pos = 0;
            int framelength = inputintdata[pos];
            pos++;
            img_sprite.mFramelist = new FrameInfo[framelength];
            IKALog.v("anime_read", "START-framelength," + framelength);
            for (int i = 0; i < framelength; i++) {

                img_sprite.mFramelist[i] = new FrameInfo();
                int modlistlength = inputintdata[pos];
                IKALog.v("anime_read", "START-3," + i + "," + modlistlength);
                pos++;
                img_sprite.mFramelist[i].mModlist = new ModInfo[modlistlength];
                for (int j = 0; j < modlistlength; j++) {
                    int modlength = inputintdata[pos];
                    pos++;
                    img_sprite.mFramelist[i].mModlist[j] = new ModInfo();
                    img_sprite.mFramelist[i].mModlist[j].mImageModID = inputintdata[pos];
                    pos++;
                    img_sprite.mFramelist[i].mModlist[j].mX = inputintdata[pos];
                    pos++;
                    img_sprite.mFramelist[i].mModlist[j].mY = inputintdata[pos];
                    pos++;
                    img_sprite.mFramelist[i].mModlist[j].mRotate = inputintdata[pos];
                    pos++;
                    img_sprite.mFramelist[i].mModlist[j].mScale = ((float) inputintdata[pos]) / 10;
                    pos++;
                    img_sprite.mFramelist[i].mModlist[j].mRotateType = inputintdata[pos];
                    pos++;
                }
            }

            //读取anime
            int animelistlength = inputintdata[pos];
            pos++;
            IKALog.v("ANIME", "animelistlength=" + animelistlength);
            img_sprite.mAnimelist = new AnimeInfo[animelistlength];
            for (int i = 0; i < animelistlength; i++) {
                img_sprite.mAnimelist[i] = new AnimeInfo();
                int frameidlistlength = inputintdata[pos];
                pos++;
                img_sprite.mAnimelist[i].mFrameIDlist = new int[frameidlistlength];
                for (int j = 0; j < frameidlistlength; j++) {
//            		 int framepiecelength = inputintdata[pos];
//                     pos++;
                    img_sprite.mAnimelist[i].mFrameIDlist[j] = inputintdata[pos];
                    pos++;
                }
                IKALog.v("ANIME", i + ",animesize=" + frameidlistlength);
                img_sprite.mAnimelist[i].printList();

            }

            System.out.println("read ika over");
        } catch (Exception ex) {
            System.out.println("read ika error");
        }
        inputintdata = null;
    }


    public short getShort(InputStream fo) {
        short tmpshort = 0;
        byte[] bytes = new byte[2];
        try {
            bytes[0] = (byte) fo.read();
            bytes[1] = (byte) fo.read();
            tmpshort = bytesToShort(bytes);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return tmpshort;
    }


    /**
     * 真实绘画的帧
     */
    private int frame_select;
    /**
     * 动画序列ID
     */
    public int ActSecletNumber = 0;
    /**
     * 当前帧
     */
    public int act_frame = 0;
    /**
     * 向左向右
     */
    public boolean isleft = true;
    /**
     * 整体缩放
     */
    public float frame_scale = 1f;

    /**
     * 绘画函数
     *
     * @param g
     * @param x          坐标
     * @param y          坐标
     * @param img_sprite 动画精灵名
     */
    public void paint(Canvas g, float x, float y, AnimeSprite img_sprite, HashMap<Integer, Bitmap> mReplaceMap) {
        if (ActSecletNumber >= 0) {

            AnimeInfo mAnime = img_sprite.mAnimelist[ActSecletNumber];
            frame_select = mAnime.mFrameIDlist[act_frame % mAnime.mFrameIDlist.length];
            FrameInfo mFrame = img_sprite.mFramelist[frame_select];
            for (int i = 0; i < mFrame.mModlist.length; i++) {
                ModInfo mod = mFrame.mModlist[i];
                if (mReplaceMap == null) {
                    drawAni(img_sprite.AnimeImg[mod.mImageModID], x, mod.mX, y, mod.mY, mod.mRotate, mod.mScale, frame_scale, isleft, g);
                } else {
                    //替换模式
                    Bitmap bitmap = mReplaceMap.get(mod.mImageModID);
                    if (bitmap != null) {
                        drawAni(bitmap, x, mod.mX, y, mod.mY, mod.mRotate, mod.mScale, frame_scale, isleft, g);
                    } else {
                        drawAni(img_sprite.AnimeImg[mod.mImageModID], x, mod.mX, y, mod.mY, mod.mRotate, mod.mScale, frame_scale, isleft, g);
                    }
                }
            }
            act_frame++;
        }
    }


    Paint mPaint = new Paint();

    /**
     * 动画用的描绘方法 MIDP2.0使用.(nokia api版本因通用性,暂时屏蔽)
     */
    private void drawAni(Bitmap img, float x, float bx, float y, float by, float rot, float scale, float framescale, boolean isleft, Canvas g) {
        DrawRegionAndroid.drawRegion(img, x, y, bx, by, rot, scale, framescale, isleft, g, mPaint);
    }


    private static int bytesToInt(byte[] bytes) {
        int num = bytes[0] & 0xFF;
        num |= ((bytes[1] << 8) & 0xFF00);
        num |= ((bytes[2] << 16) & 0xFF0000);
        num |= ((bytes[3] << 24) & 0xFF000000);
        return num;
    }


    private static short bytesToShort(byte[] bytes) {
        int num = bytes[0] & 0xFF;
        num |= ((bytes[1] << 8) & 0xFF00);
        return (short) num;
    }
}