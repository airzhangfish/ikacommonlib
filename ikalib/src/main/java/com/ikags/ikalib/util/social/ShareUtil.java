package com.ikags.ikalib.util.social;

import java.io.File;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
/**
 * 分享功能合集
 * @author zhangxiasheng
 *
 */
public class ShareUtil {
/**
 * 系统分享文本
 * @param context
 * @param title
 * @param text
 */
	public static void shareTextMessage(Context context, String title, String text) {
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("text/plain"); // 分享发送到数据类型
		intent.putExtra(Intent.EXTRA_SUBJECT, "" + title); // 分享的主题
		intent.putExtra(Intent.EXTRA_TEXT, "" + text); // 分享的内容
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // 允许intent启动新的activity
		context.startActivity(Intent.createChooser(intent, "分享")); // //目标应用选择对话框的标题
	};

	/**
	 * 系统分享图片
	 * @param context
	 * @param title
	 * @param text
	 * @param imagefile
	 */
	public static void shareImageTextMessage(Context context, String title, String text, File imagefile) {
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("image/*");
		intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(imagefile));
		intent.putExtra(Intent.EXTRA_SUBJECT, "" + title); // 分享的主题
		intent.putExtra(Intent.EXTRA_TEXT, "" + text); // 分享的内容
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // 允许intent启动新的activity
		context.startActivity(Intent.createChooser(intent, "分享"));

	};

}
