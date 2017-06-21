package com.ikags.ikacommonlib.example.nettest;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ikags.ikacommonlib.R;
import com.ikags.ikalib.util.loader.NetLoader;
import com.ikags.ikalib.util.loader.NetQueueLoader;
import com.ikags.ikalib.util.loader.TextBaseParser;

public class NettestActivity extends Activity {



	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.v("SViewsTestActivity", "initview");
		setContentView(R.layout.acti_nettest);
		initLayout();
	}
	Button netbutton1=null;
	Button netbutton2=null;
	Button netbutton3=null;
	TextView netinfo=null;
	public void initLayout(){
		netbutton1=(Button)this.findViewById(R.id.netbutton1);
		netbutton2=(Button)this.findViewById(R.id.netbutton2);
		netbutton3=(Button)this.findViewById(R.id.netbutton3);
		netinfo=(TextView)this.findViewById(R.id.netinfo);
		netinfo.setText("初始化网络请求");
		netbutton1.setOnClickListener(ocl);
		netbutton2.setOnClickListener(ocl);
		netbutton3.setOnClickListener(ocl);
	}

	OnClickListener ocl=new OnClickListener(){

		@Override
		public void onClick(View v) {
			if(v==netbutton1){
				Toast.makeText(NettestActivity.this, "parser网络请求", Toast.LENGTH_SHORT).show();
				NetLoader.getDefault(NettestActivity.this).loadUrl("http://www.ikags.com", (String)null, null,jbparser, "net1",false);
			}
			if(v==netbutton2){
				Toast.makeText(NettestActivity.this, "序列parser网络请求", Toast.LENGTH_SHORT).show();
				NetQueueLoader.getDefault(NettestActivity.this).loadUrl("http://www.ikagame.net", (String)null, null,jbparser, "net2",false);
				NetQueueLoader.getDefault(NettestActivity.this).startQueueLoader();
			}
			if(v==netbutton3){
				Toast.makeText(NettestActivity.this, "当前线程网络请求", Toast.LENGTH_SHORT).show();
				Thread t=new Thread(){
					public void run(){
						String data=	NetQueueLoader.getDefault(NettestActivity.this).loadUrlStringData("http://www.ikags.com", null, null, null, false);
						Message msg=new Message();
						msg.obj="net3"+"http://www.ikags.com"+data;
						mhandler.sendMessage(msg);
					}
				};
				t.start();
			}
		}
	};

	TextBaseParser jbparser=new TextBaseParser(){

		@Override
		public void parsetTextData(String url, String data, String tag,boolean isNetdata) {
			Message msg=new Message();
			msg.obj=tag+url+isNetdata+data;
			mhandler.sendMessage(msg);
		}
	};

	Handler mhandler=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			Toast.makeText(NettestActivity.this, "网络请求结束", Toast.LENGTH_SHORT).show();
			netinfo.setText(""+(String)msg.obj);
			super.handleMessage(msg);
		}
	};
}