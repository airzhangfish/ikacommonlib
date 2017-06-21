package com.ikags.ikalib.util.db;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
/**
 * 抽象数据库类.可以快速快节奏的建立数据库相关内容,可参考demo的写法
 * @author zhangxiasheng
 *
 */
public class IBaseProvider extends ContentProvider {
	public static String TAG = "IBaseProvider";
	public SQLiteOpenHelper mOpenHelper;

	// TODO 必须重载onCreate,生成Helper

	@Override
	public boolean onCreate() {
		// TAG=this.getClass().getSimpleName();
		// mCacheOpenHelper = new CacheDatabaseHelper(getContext());
		return true;
	}

	// TODO 需要初始化一个sqlhelper
	// private static class CacheDatabaseHelper extends SQLiteOpenHelper {
	// CacheDatabaseHelper(Context context) {
	// super(context, CachedUrlManager.DATABASE_NAME, null, CachedUrlManager.DATABASE_VERSION);
	// }
	// @Override
	// public void onCreate(SQLiteDatabase db) {
	// // 创建缓存和插件
	// String command = "CREATE TABLE  IF NOT EXISTS " + CachedUrlManager.TABLE_CACHELIST + " " + "(" + CacheList_DataSheet.mUrl + " TEXT,"
	// + CacheList_DataSheet.mFileName + " TEXT," + CacheList_DataSheet.mExpiredTime + " TEXT);";
	// db.execSQL(command);
	// }
	//
	// @Override
	// public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	// if (oldVersion != CachedUrlManager.DATABASE_VERSION) {
	// if (oldVersion < CachedUrlManager.DATABASE_VERSION ) {
	// db.execSQL("DROP TABLE IF EXISTS " + CachedUrlManager.TABLE_CACHELIST);
	// }
	// onCreate(db);
	// }
	// }
	// }

	@Override
	public String getType(Uri uri) {
		SqlArguments args = new SqlArguments(uri, null, null);
		if (TextUtils.isEmpty(args.where)) {
			return "vnd.android.cursor.dir/" + args.table;
		} else {
			return "vnd.android.cursor.item/" + args.table;
		}
	}

	@Override
	public int bulkInsert(Uri uri, ContentValues[] values) {
		SQLiteDatabase db = null;
		db = mOpenHelper.getWritableDatabase();
		db.beginTransaction();
		int result = super.bulkInsert(uri, values);
		if (result == values.length) {
			db.setTransactionSuccessful();
		}
		db.endTransaction();
		return result;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		SqlArguments args = new SqlArguments(uri);
		SQLiteDatabase db = null;
		db = mOpenHelper.getWritableDatabase();
		final long rowId = db.insert(args.table, null, values);
		if (rowId <= 0)
			return null;

		uri = ContentUris.withAppendedId(uri, rowId);
		getContext().getContentResolver().notifyChange(uri, null);
		return uri;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
		SqlArguments args = new SqlArguments(uri, selection, selectionArgs);
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();;
		qb.setTables(args.table);

		SQLiteDatabase db = null;
		db = mOpenHelper.getReadableDatabase();
		Cursor result = qb.query(db, projection, args.where, args.args, null, null, sortOrder);
		if (result != null)
			result.setNotificationUri(getContext().getContentResolver(), uri);
		return result;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
		SqlArguments args = new SqlArguments(uri, selection, selectionArgs);
		SQLiteDatabase db = null;
		db = mOpenHelper.getWritableDatabase();
		int count = db.update(args.table, values, args.where, args.args);
		if (count > 0) {
			getContext().getContentResolver().notifyChange(uri, null);
		}
		return count;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		SqlArguments args = new SqlArguments(uri, selection, selectionArgs);
		SQLiteDatabase db = null;
		db = mOpenHelper.getWritableDatabase();

		int count = db.delete(args.table, args.where, args.args);
		if (count > 0) {
			getContext().getContentResolver().notifyChange(uri, null);
		}
		return count;
	}

	/**
	 * 拼装SQL用
	 * 
	 * @param table_name
	 * @param table_info
	 * @return
	 */
	public String getSQL(String table_name, String table_info) {
		String sqlcommand = "Create TABLE if not exists " + table_name + " (" + table_info + " )";
		return sqlcommand;
	}

	private static class SqlArguments {
		public final String table;
		public final String where;
		public final String[] args;

		SqlArguments(Uri url, String where, String[] args) {
			if (url.getPathSegments().size() == 1) {
				this.table = url.getPathSegments().get(0);
				this.where = where;
				this.args = args;
			} else if (url.getPathSegments().size() != 2) {
				throw new IllegalArgumentException("Invalid URI: " + url);
			} else if (!TextUtils.isEmpty(where)) {
				throw new UnsupportedOperationException("WHERE clause not supported: " + url);
			} else {
				this.table = url.getPathSegments().get(0);
				this.where = "_id=" + ContentUris.parseId(url);
				this.args = null;
			}
		}

		SqlArguments(Uri url) {
			if (url.getPathSegments().size() == 1) {
				table = url.getPathSegments().get(0);
				where = null;
				args = null;
			} else {
				throw new IllegalArgumentException("Invalid URI: " + url);
			}
		}
	}
}
