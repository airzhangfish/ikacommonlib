package com.ikags.ikacommonlib.example.anime;

import android.app.Activity;
import android.os.Bundle;

public class IKAAnimeActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyViewAnime mv=new MyViewAnime(this);  //测试动画
//        MyViewMap mv=new MyViewMap(this,null);  //测试地图
        setContentView(mv);
    }
}