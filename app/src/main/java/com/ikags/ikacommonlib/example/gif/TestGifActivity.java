package com.ikags.ikacommonlib.example.gif;

import java.io.InputStream;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.ikags.ikalib.androidview.IKAGifView;
import com.ikags.ikacommonlib.R;

public class TestGifActivity extends Activity implements OnClickListener {

    private static final String TAG="TestGifActivity";
    private IKAGifView gifViewback;
    private IKAGifView gifViewhero;
    private IKAGifView gifViewhero2;
    private boolean flag=true;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.acti_gif_layout);
      boolean flag=false;
        gifViewback=(IKAGifView) findViewById(R.id.gif_back);
        gifViewback.setVisibility(View.VISIBLE);
        gifViewback.setKeepScreenOn(true);
        try {
           InputStream is=this.getAssets().open("background_test.gif");
            gifViewback.setGifImage(is, flag,true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        
        gifViewhero=(IKAGifView) findViewById(R.id.gif_hero);
        gifViewhero.setVisibility(View.VISIBLE);
        gifViewhero.setKeepScreenOn(true);
        try {
            InputStream is=this.getAssets().open("a53.gif");
            gifViewhero.setGifImage(is, flag,true);  
         } catch (Exception e) {
             e.printStackTrace();
         }
        
        
        gifViewhero2=(IKAGifView) findViewById(R.id.gif_hero2);
        gifViewhero2.setVisibility(View.VISIBLE);
        gifViewhero2.setKeepScreenOn(true);
        try {
            InputStream is=this.getAssets().open("a53.gif");
            gifViewhero2.setGifImage(is, flag,false);  //�泯�෴�ķ���
         } catch (Exception e) {
             e.printStackTrace();
         }
    }

    @Override
    public void onClick(View v) {
        /*if (flag) {
            gifView.showCover();
            flag=false;
        } else {
            gifView.showAnimation();
            flag=true;
        }*/
    }

}
