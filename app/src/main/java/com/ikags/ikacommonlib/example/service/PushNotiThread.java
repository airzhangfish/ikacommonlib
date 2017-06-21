package com.ikags.ikacommonlib.example.service;

import java.util.Calendar;

import com.ikags.ikalib.util.IKALog;

import android.content.Context;
import android.content.Intent;

public class PushNotiThread extends Thread {
	private static final String TAG = "PushNotiThread";
	public static final int LISTENER_TIME = 100 * 1000;// 2小时=2 * 3600 * 1000
	public static final int LISTENER_TIME_BUZY = 50 * 1000;// 0.5小时=1800 * 1000 ,10分钟600*1000

	boolean isRunning = true;

	private Context mContext;
	public PushNotiThread(Context context) {
		mContext = context;
	}

	public boolean isBuzyTime() {
		// 判断时间.
		Calendar cal = Calendar.getInstance();
		int hours = cal.get(Calendar.HOUR_OF_DAY);
		if (hours < 7 && hours > 21) { // 7点前,21点后
			return false;
		} else {
			return true;
		}
	}

	@Override
	public void run() {
		while (isRunning) {
			try {
				// TODO 业务处理
				IKALog.v(TAG, "send Thread sendBroadcast.");
				Intent notiintent = new Intent(PushNotiReceiver.PUSHNOTI_ACTION_OK);
				notiintent.putExtra("title", "推送测试");
				String data = Calendar.getInstance().getTime().toString();
				notiintent.putExtra("desc", data);
				notiintent.putExtra("type", "notype");
				mContext.sendBroadcast(notiintent);
				regAlarm();
			} catch (Exception e) {
				e.printStackTrace();
			}

			// 休眠时间
			try {
				if (isBuzyTime()) {
					Thread.sleep(LISTENER_TIME_BUZY);
				} else {
					Thread.sleep(LISTENER_TIME);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}


	public void regAlarm(){

	}

}
