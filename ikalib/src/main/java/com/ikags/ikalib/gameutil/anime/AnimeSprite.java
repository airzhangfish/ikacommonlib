package com.ikags.ikalib.gameutil.anime;

import android.graphics.Bitmap;

import com.ikags.ikalib.gameutil.anime.info.AnimeInfo;
import com.ikags.ikalib.gameutil.anime.info.FrameInfo;

/**
 * I.K.A Engine<BR>
 * AnimeSprite类，动画精灵类，动画图片，数据资料储存。<BR>
 * 使用方法参见AnimeEngine类说明
 * @author http://airzhangfish.spaces.live.com
 * @since 2005.11.15 最后更新 2009.5.20
 * @version 0.6
 */
public class AnimeSprite
{
    /**
     * frame列表
     */
    public FrameInfo[] mFramelist = null;
    /**
     *
     */
    public AnimeInfo[] mAnimelist = null;

    /**
     * 动画精灵图片
     */
    public Bitmap[] AnimeImg;
    /**
     * 动画精灵物块个数
     */
    public int anime_Piece = 0;

    /**
     * 初始化对象,读取图片和动画文件
     */
    public AnimeSprite(String IKA_path, String BIN_path)
    {
        AnimeEngine.getInstance().load_Anime(IKA_path, this);
        AnimeEngine.getInstance().load_Image(BIN_path, this);
    }

    /**
     * 资源置空，用于销毁精灵
     */
    public void setNull()
    {
        mFramelist = null;
        mAnimelist = null;
        for (int i = 0; i < AnimeImg.length; i++)
        {
            if(AnimeImg[i]!=null){
                AnimeImg[i].recycle();
            }
            AnimeImg[i] = null;
        }
        AnimeImg = null;
        System.gc();
    }
}