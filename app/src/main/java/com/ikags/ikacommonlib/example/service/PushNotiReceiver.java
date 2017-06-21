package com.ikags.ikacommonlib.example.service;

import com.ikags.ikacommonlib.libtest.CommonLibActivity;
import com.ikags.ikacommonlib.R;
import com.ikags.ikalib.util.IKALog;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
/**
 * 推送接收器,需要定制
 * @author zhangxiasheng
 *
 */
public class PushNotiReceiver extends BroadcastReceiver {

	private static final String TAG = "PushNotiReceiver";
	private NotificationManager notificationManager;
	public static final String PUSHNOTI_ACTION_OK = "action.pushnoti.ok";
	public static final String PUSHNOTI_ACTION_CANCEL = "action.pushnoti.cancel";
	private Context mContext;
	public PushNotiReceiver(Context context) {
		mContext = context;
	}

	public IntentFilter getIntentFilter() {
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(PUSHNOTI_ACTION_OK);
		intentFilter.addAction(PUSHNOTI_ACTION_CANCEL);
		return intentFilter;
	}

	@Override
	public void onReceive(Context arg0, Intent intent) {

		String action = intent.getAction();
		IKALog.v(TAG, "onReceive_action=" + action);
		if (action.equals(PUSHNOTI_ACTION_OK)) {
			notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);

			// TODO 需要自己编辑内容
			String title = intent.getStringExtra("title");
			String desc = intent.getStringExtra("desc");
			String type = intent.getStringExtra("type");

			Notification notification = new Notification(R.drawable.ic_launcher, "EXAM=" + title, System.currentTimeMillis());
			notification.defaults = Notification.DEFAULT_SOUND;
			notification.flags = Notification.FLAG_AUTO_CANCEL;

			Intent intent2 = new Intent();
			intent2.setClass(mContext, com.ikags.ikacommonlib.libtest.CommonLibActivity.class);
			intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent2.putExtra("title", "" + title);
			intent2.putExtra("desc", "" + desc);
			intent2.putExtra("type", "" + type);

			PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, intent2, 0);
			//notification.setLatestEventInfo(mContext, title, desc, pendingIntent);
			//TODO 测试推送
//			notificationManager.notify(1, notification);
		}

	}

}