package com.ikags.ikalib.gameutil.anime;

import java.util.HashMap;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.ikags.ikalib.gameutil.anime.info.AnimeInfo;



/**
 * I.K.A Engine<BR>
 * GSprite类，游戏精灵类，直接调用动画。<BR>
 * 使用方法参见AnimeEngine类说明
 * @author http://airzhangfish.spaces.live.com
 * @since 2005.11.15 最后更新 2009.5.20
 * @version 0.6
 */
public class GSprite
{

    /**
     * 图片替换(替换道具类)
     */
    public HashMap<Integer, Bitmap> mReplaceMap=null;
    public int skipFrame=0; //0为不跳帧
    /**
     * 动画ID
     */
    public int ActSecletNumber = 0;
    /**
     * 帧ID
     */
    public int act_frame = 0;
    private int act_skipcount = 0;
    /**
     * 面左面右
     */
    public boolean isFaceRight = true;
    /**
     * 调用动画ID
     */
    public int ImageSpriteID = 0;
    /**
     * 坐标
     */
    public float x = 0;
    public float y = 0;
    /**
     * 整体缩放
     */
    public float frameScale=1f;
    /**
     * 游戏精灵初始化资源
     */
    public GSprite(float xx, float yy, int ImageSID, int aniselect, boolean left)
    {
        ImageSpriteID = ImageSID;
        ActSecletNumber = aniselect;
        isFaceRight = left;
        x = xx;
        y = yy;
    }
    /**
     * 游戏精灵绘画
     */
    public void paint(Canvas g)
    {
        AnimeEngine.getInstance().ActSecletNumber = ActSecletNumber;
        AnimeEngine.getInstance().act_frame = act_frame;
        AnimeEngine.getInstance().isleft = isFaceRight;
        AnimeEngine.getInstance().frame_scale=frameScale;
        AnimeEngine.getInstance().paint(g, x, y, AnimeEngine.getInstance().imgSprite_pool[ImageSpriteID],mReplaceMap);

        if(act_skipcount<skipFrame){
            act_skipcount++;
        }else{
            act_skipcount=0;
            act_frame = AnimeEngine.getInstance().act_frame;
        }
        //act_frame = AnimeEngine.getInstance().act_frame;
    }

    public int getActFrameCount(){
        int count=0;
        try{
            AnimeInfo mAnime= AnimeEngine.getInstance().imgSprite_pool[ImageSpriteID].mAnimelist[ActSecletNumber];
            count=mAnime.mFrameIDlist.length;
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return count;
    }

    public int getCurrentFrame(){
        int count=getActFrameCount();
        if(count>0){
            return act_frame%count;
        }
        return 0;
    }
}
