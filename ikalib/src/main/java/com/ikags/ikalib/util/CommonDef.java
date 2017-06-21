package com.ikags.ikalib.util;

import java.io.File;

import android.os.Environment;

/**
 * 一些静态数据,包括网络数据缓存等
 * 
 * @author zhangxiasheng
 * 
 */
public class CommonDef {
	public static String PATH_CACHE = Environment.getExternalStorageDirectory() + "/ikacommonlib/cache";
	public static final long CACHE_MAXTIME_IMAGE = 3600 * 24 * 15;  //15天
	public static final long CACHE_MAXTIME_TEXT = 3600 * 8;  //8小时
	public static String getCachePath() {
		try {
			File cachedir = new File(PATH_CACHE);
			if (cachedir.isDirectory() == false) {
				cachedir.mkdirs();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return PATH_CACHE;
	}
}
