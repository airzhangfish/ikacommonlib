package com.ikags.ikalib.util.cache;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

import com.ikags.ikalib.util.db.IBaseProvider;

/**
 * 缓存数据库,缓存url和文件地址(常用)
 * AUTHORITY设置必须为 :包名.urlcache,请在manifest文件上设置
 * @author zhangxiasheng
 * 
 */
public class CacheProvider extends IBaseProvider {
	public final static String DATABASE_NAME = "ikacache.db";
	public final static int DATABASE_VERSION = 10;

	public static String AUTHORITY = "com.ikags.util.cache.urlcache";
	public static final String TABLE_CACHELIST = "urlcache";
	public static final String mUrl = "_url";
	public static final String mFileName = "_fileName";
	public static final String mExpiredTime = "_expiredTime";
	public static Uri URI_URLCACHE = Uri.parse("content://" + AUTHORITY + "/" + TABLE_CACHELIST);

	@Override
	public boolean onCreate() {
		AUTHORITY=this.getContext().getPackageName()+".urlcache";
		URI_URLCACHE = Uri.parse("content://" + AUTHORITY + "/" + TABLE_CACHELIST);
		TAG = this.getClass().getSimpleName();
		mOpenHelper = new CacheDatabaseHelper(getContext());
		return true;
	}

	public class CacheDatabaseHelper extends SQLiteOpenHelper {
		CacheDatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}
		@Override
		public void onCreate(SQLiteDatabase db) {
			String CACHELIST_INFO = mUrl + " TEXT," + mFileName + " TEXT," + mExpiredTime + " TEXT";
			String command = getSQL(TABLE_CACHELIST, CACHELIST_INFO);
			db.execSQL(command);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			if (oldVersion != DATABASE_VERSION) {
				if (oldVersion < DATABASE_VERSION) {
					db.execSQL("DROP TABLE IF EXISTS " + TABLE_CACHELIST);
				}
				onCreate(db);
			}
		}
	}

}
