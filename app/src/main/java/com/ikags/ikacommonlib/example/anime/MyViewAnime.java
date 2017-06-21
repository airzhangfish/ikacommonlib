package com.ikags.ikacommonlib.example.anime;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

import com.ikags.ikalib.gameutil.anime.AnimeEngine;
import com.ikags.ikalib.gameutil.anime.AnimeSprite;
import com.ikags.ikalib.gameutil.anime.GSprite;
import com.ikags.ikalib.util.IKALog;

public class MyViewAnime extends SurfaceView implements Runnable, Callback
{

    Paint paint = new Paint();
    SurfaceHolder mSurfaceHolder = null;
    Context mcontext = null;

    Bitmap myBmp;
    int dog_x = 0;
    int dog_y = 0;
    int count = 0;

    GSprite gsprite=null;
    GSprite gsprite2=null;
    public static String text = "moving text";

    public MyViewAnime(Context context)
    {
        super(context);
        mSurfaceHolder = getHolder();
        mSurfaceHolder.addCallback(this);
        mcontext = context;



        AnimeEngine.getInstance(context);
//  AnimeEngine.getInstance().imgSprite_pool[0]=new AnimeSprite("anime_simple.ika","anime_imgpak.bin");
        AnimeEngine.getInstance().imgSprite_pool[0]=new AnimeSprite("anime_hero_fashi.ika","anime_hero_fashi_res.bin");
        gsprite=new GSprite(x1,y1,0,0,true);
        gsprite2=new GSprite(x2,y2,0,0,false);
    }

    int x1=100;
    int y1=300;
    int x2=600;
    int y2=500;
    int gostate=0;


    public void draw(Canvas mCanvas)
    {
        try{
            mCanvas.drawColor(0xff000000);
            paint.setColor(0xffffffff);
            mCanvas.drawText("毫秒:"+spendtime+",帧数"+1000/(spendtime)+",测试显示:"+count, 30, 50, paint);
            count++;

            if(gsprite!=null){
                gsprite.frameScale=1f;
                if(gostate==0){ //走向敌人
                    gsprite.isFaceRight=true;
                    gsprite.x=x1+(((x2-x1)*count)/50);
                    gsprite.y=y1+(((y2-y1)*count)/50);
                    if(gsprite.x==x2){
                        gostate=1;
                        count=0;
                    }
                }else{//走回地方
                    gsprite.isFaceRight=false;
                    gsprite.x=x2-(((x2-x1)*count)/50);
                    gsprite.y=y2-(((y2-y1)*count)/50);
                    if(gsprite.x==x1){
                        count=0;
                        gostate=0;
                    }
                }
                gsprite.paint(mCanvas);
            }


            if(gsprite2!=null){
                gsprite2.frameScale=1f;
                gsprite2.paint(mCanvas);
            }

        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    public void logic()
    {
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                dog_x = ( int ) event.getX();
                dog_y = ( int ) event.getY();
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return true;
    }

    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        return true;

    }



    long starttime=0;
    long endtime=0;
    long spendtime=1;
    public void run()
    {
        while (mIsRunning)
        {
            try
            {

                Thread.sleep(80);
                starttime=System.currentTimeMillis();
                logic();
                Canvas mCanvas = mSurfaceHolder.lockCanvas();
                draw(mCanvas);
                mSurfaceHolder.unlockCanvasAndPost(mCanvas);
                endtime=System.currentTimeMillis();
                spendtime=endtime-starttime;
                if(spendtime>=40){
                    Thread.sleep(5);
                }else{
                    Thread.sleep(40-spendtime);
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
    {
        IKALog.v("MyViewAnime", "width="+width+",height="+height);

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder)
    {
        mIsRunning = true;
        mThread = new Thread(this);
        if (!mThread.isAlive())
        {
            mThread.start();
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder)
    {
        mIsRunning = false;
    }

    boolean mIsRunning = true;
    Thread mThread = null;
}
