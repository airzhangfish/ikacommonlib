package com.ikags.ikalib.util.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
/**
 * 封装了数据相关的操作方法,处理了异常,建议直接使用
 * @author zhangxiasheng
 *
 */
public class IProviderManager {
	public static final String TAG = "IProviderManager";
	Context context;
	private static IProviderManager instance = null;
/**
 * 数据库操作类,协助操作数据库
 * @param mcontext
 * @return
 */
	public static IProviderManager getInstance(Context mcontext) {
		if (instance == null) {
			instance = new IProviderManager(mcontext);
		}
		return instance;
	}

	public IProviderManager(Context mcontext) {
		context = mcontext.getApplicationContext();
	}
	


/**
 *  * 查询数据库,利用ContentValues cv=new ContentValues()以及cv.put来添加数据.
 * @param uri 要查询的表
 * @param cv
 * @return
 */
	public Uri insertValues(Uri uri, ContentValues cv) {
		Uri muri=context.getContentResolver().insert(uri, cv);
	return muri;
	}

	/**
 * 更新数据库,
 * @param uri 要查询的表
 * @param cv
 * @param where 要查询的条件 写为 "_id=? AND _type=?"
 * @param selectionArgs 要查询的数据,写为new String[]{_id,_type}
	 * @return 删除了多少条
	 */
	public int updateValues(Uri uri, ContentValues cv, String where, String[] selectionArgs) {
			return context.getContentResolver().update(uri, cv, where, selectionArgs);
	}

	/**
	 * 查询数据库
	 * 
	 * @param uri 要查询的表
	 * @param projection 要查询的字段,写为new String[]{_id,_type},返回全部则设置为null
	 * @param selection 要查询的条件 写为 "_id=? AND _type=?"
	 * @param selectionArgs 要查询的数据,写为new String[]{_id,_type}
	 * @param sortOrder 排序和限制,写为_id ASC/DESC LIMIT 0,10
	 * @return
	 */
	public Cursor queryValues(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
		Cursor cursor = null;
		try {
			cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, sortOrder);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return cursor;
	}
	/**
	 * 删除数据库条目
	 * 
	 * @param uri 要查询的表
	 * @param where 要删除的条件 写为 "_id=? AND _type=?"
	 * @param selectionArgs 要删除的数据,写为new String[]{_id,_type}
	 * @return 删除的数据条数
	 */
	public int deleteValues(Uri uri, String where, String[] selectionArgs) {
		int resultcode = 0;
		try {
			resultcode = context.getContentResolver().delete(uri, where, selectionArgs);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return resultcode;
	}

}
