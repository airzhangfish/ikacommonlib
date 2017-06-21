package com.ikags.ikacommonlib.libtest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.ikags.ikacommonlib.R;
import com.ikags.ikacommonlib.example.anime.IKAAnimeActivity;
import com.ikags.ikacommonlib.example.boxworldtest.Boxworld3DActivity;
import com.ikags.ikacommonlib.example.gif.TestGifActivity;
import com.ikags.ikacommonlib.example.lowviews.SViewsTestActivity;
import com.ikags.ikacommonlib.example.nettest.NettestActivity;
import com.ikags.ikacommonlib.example.opengl3dtest.DEMO_openGL3DActivity;
import com.ikags.ikacommonlib.example.service.PushNotiService;
//import com.ikags.ikacommonlib.util.SystemUtil;

public class CommonLibActivity extends Activity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		initLayout();
	}

	ListView listview1=null;
	public void initLayout(){
		listview1=(ListView)this.findViewById(R.id.listView1);
		ArrayAdapter ar=new ArrayAdapter(this, android.R.layout.simple_list_item_1,demolist_str);
		listview1.setAdapter(ar);
		listview1.setOnItemClickListener(ocl);
		//System.getProperties().list(System.out);
	}





	public String[] demolist_str={"2D画布控件(透明+不透明)","动画演示","openGL 3D","网络模块测试","后台驻留推送服务(暂停)","镜像GIF动画控件","boxword 3d"};
	OnItemClickListener ocl=new OnItemClickListener(){

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
			Toast.makeText(CommonLibActivity.this, demolist_str[arg2], Toast.LENGTH_SHORT).show();
			Intent intent=new Intent();
			switch(arg2){
				case 0: //2D画布控件
					intent.setClass(getApplicationContext(), SViewsTestActivity.class);
					startActivity(intent);
					break;
				case 1://动画演示
					intent.setClass(getApplicationContext(), IKAAnimeActivity.class);
					startActivity(intent);
					break;
				case 2://3D模型/动画
					intent.setClass(getApplicationContext(), DEMO_openGL3DActivity.class);
					startActivity(intent);
					break;
				case 3://网络模块测试
					intent.setClass(getApplicationContext(), NettestActivity.class);
					startActivity(intent);
					break;
				case 4://推送代码
					Toast.makeText(CommonLibActivity.this, "暂时不启动推送服务,请改代码", Toast.LENGTH_SHORT).show();
//					startService();
					break;
				case 5://GIF动画控件
					intent.setClass(getApplicationContext(), TestGifActivity.class);
					startActivity(intent);
					break;
				case 6://GIF动画控件
					intent.setClass(getApplicationContext(), Boxworld3DActivity.class);
					startActivity(intent);
					break;

			}

		}

	};


	public void startService(){
//		boolean isService=SystemUtil.isServiceRunning(this,"com.PushNotiService");
//		if(!isService){
//			Intent intent=new Intent(this, PushNotiService.class);
//			startService(intent);
//		}
	}



}