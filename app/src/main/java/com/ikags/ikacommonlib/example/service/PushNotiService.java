package com.ikags.ikacommonlib.example.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import com.ikags.ikalib.util.IKALog;
import com.ikags.ikalib.util.SharedPreferencesManager;
/**
 * 后台推送服务,需要常驻内存.
 * @author zhangxiasheng
 *
 */
public class PushNotiService extends Service {
	private static final String TAG = "PushNotiService";

	private PushNotiReceiver notiReceiver = null;
	private PushNotiThread notiThread = null;
	private static long alarmTime = 3600 * 1000;// 每小时检测
	@Override
	public void onCreate() {
		IKALog.v(TAG, "onCreate");
		super.onCreate();
	}

	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		if(intent!=null){
			IKALog.v(TAG, "onStart=" + intent.getAction()+","+startId);
		}else{
			IKALog.v(TAG, "onStart=,"+startId);
		}
		startNotiService();
		regAlarm();
	}

	public void startNotiService() {
		try {
			boolean isThreadRuning = (notiThread != null) && notiThread.isAlive() && (notiThread.isInterrupted() == false);
			// 线程不用重启
			if (!isThreadRuning) {
				notiThread = new PushNotiThread(this){
					public void regAlarm() {
						long lasttime = SharedPreferencesManager.getInstance(PushNotiService.this).readData(TAG, "alarmchecktime", 0l);
						if (System.currentTimeMillis() > lasttime + alarmTime) {
							addAlarm();
							SharedPreferencesManager.getInstance(PushNotiService.this).saveData(TAG, "alarmchecktime", System.currentTimeMillis());
						}
					}
				};
				notiThread.start();
			}

			boolean isReceiverRunning = false;
			if (notiReceiver == null) {
				isReceiverRunning = false;
				notiReceiver = new PushNotiReceiver(this);
				registerReceiver(notiReceiver, notiReceiver.getIntentFilter());
			}else{
				isReceiverRunning = true;
			}
			IKALog.v(TAG, "isThreadRuning=" + isThreadRuning + ",notiReceiver=" + isReceiverRunning + ",time=" + System.currentTimeMillis());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * 根据时间间隔启动程序
	 */
	public void regAlarm() {
		long lasttime = SharedPreferencesManager.getInstance(this).readData(TAG, "alarmchecktime", 0l);
		if (System.currentTimeMillis() > lasttime + alarmTime) {
			addAlarm();
			SharedPreferencesManager.getInstance(this).saveData(TAG, "alarmchecktime", System.currentTimeMillis());
		}
	}

	/**
	 * 添加定时检测的闹钟
	 */
	private void addAlarm() {
		IKALog.v(TAG, "regAlarm=" + System.currentTimeMillis());
		AlarmManager am = ((AlarmManager) getSystemService(Context.ALARM_SERVICE));
		try {
			PendingIntent pi = PendingIntent.getService(this, 0, new Intent(this, PushNotiService.class), PendingIntent.FLAG_CANCEL_CURRENT);
			am.cancel(pi);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		try {
			Intent sIntent = new Intent(this, PushNotiService.class);
			PendingIntent pi = PendingIntent.getService(this, 0, sIntent, PendingIntent.FLAG_CANCEL_CURRENT);
			am.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + alarmTime, pi);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (notiReceiver != null) {
			unregisterReceiver(notiReceiver);
			notiReceiver = null;
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

}