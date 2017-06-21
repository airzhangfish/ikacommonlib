package com.ikags.ikacommonlib.example.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
/**
 * 监控系统事件,来启动自己的程序/服务
 * 
 * @author zhangxiasheng
 * 
 */
public class PowerUpReceiver extends BroadcastReceiver {
	public static final String TAG = "PowerUpReceiver";
	@Override
	public void onReceive(Context context, Intent intent) {
		try {
			Log.v(TAG, "PowerUpReceiver_onReceive=" + intent.getAction());

			// TODO 执行自己的操作
			Intent Serviceintent = new Intent(context, PushNotiService.class);
			context.startService(Serviceintent);

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
