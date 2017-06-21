package com.ikags.ikalib.util.cache;

import java.io.File;
import java.util.Vector;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.ikags.ikalib.util.FileUtil;
import com.ikags.ikalib.util.IKALog;
/**
 * 缓存模块总控制类
 * 
 * @author zhangxiasheng
 * 
 */
public class CachedUrlManager {
	public static final String TAG = "CachedUrlManager";
	private static CachedUrlManager mSingleInstance;
	public static boolean isCache = true; // 缓存全局开关
	private Context mContext;

	static public CachedUrlManager getDefault(Context context) {
		if (mSingleInstance == null) {
			mSingleInstance = new CachedUrlManager(context);
		}
		return mSingleInstance;
	}

	public CachedUrlManager(Context context) {
		mContext = context.getApplicationContext();
	}

	/**
	 * 更新缓存的文本(负责文件的文本保存,并且加入数据库记录)
	 * 
	 * @param url
	 * @param existtime
	 * @param data
	 */
	public void updateCacheAndText(String url, long existtime, String cachepath,String data) {
		if (!isCache) {
			return;
		}
		url = url.trim();
		CacheInfo item = findUrl(url);
		if (item != null) {
			File file = new File(item.mFileName);
			if (file.exists()) {
				file.delete();
			}
			Vector<String> vec = delCache(url);
			if (vec != null) {
				for (String s : vec) {
					FileUtil.deleteFile(s);
				}
			}
		}
		try {
			updateInThread(url, existtime, cachepath,data);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	/**
	 * 更新缓存的文件(不负责缓存文件的保存,只在数据库加入记录)
	 * 
	 * @param url
	 * @param localfile
	 * @param existtime
	 */
	public void updateCacheAndFile(String url, String localfile, long existtime) {
		if (!isCache){
			return;
		}
		url = url.trim();
		CacheInfo item = findUrl(url);
		if (item != null) {
			File file = new File(item.mFileName);
			if (file.exists()) {
				file.delete();
			}
			Vector<String> vec = delCache(url);
			if (vec != null) {
				for (String s : vec) {
					if (!s.equals(localfile)) {
						FileUtil.deleteFile(s);
					}
				}
			}
		}
		try {
			addCache(url, existtime * 1000 + System.currentTimeMillis(), localfile);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 查找缓存(不检测文件是否存在)
	 * 
	 * @param url
	 * @return
	 */
	public CacheInfo findUrlNotCheckExist(String url) {
		if (!isCache) {
			return null;
		}
		String[] info = getCache(url);
		if (info != null) {
			CacheInfo cui = new CacheInfo();
			cui.mUrl = info[0];
			cui.mExpiredTime = Long.parseLong(info[1]);
			cui.mFileName = info[2];
			IKALog.v(TAG, "findUrlNotCheckExist="+cui.mUrl+",ExpiredTime="+cui.mExpiredTime+",mName="+cui.mFileName);
			if (cui.mExpiredTime>0&&cui.mExpiredTime < System.currentTimeMillis()){
				return null;
				}
			return cui;
		} else {
			return null;
		}
	}

	/**
	 * 查找缓存(检测文件是否存在)
	 * 
	 * @param url
	 * @return
	 */
	public CacheInfo findUrl(String url) {
		CacheInfo cui = findUrlNotCheckExist(url);
		if (cui != null) {
			File file = new File(cui.mFileName);
			if (!file.exists() || file.length() == 0)
				return null;
		}
		return cui;
	}

	/**
	 * 删除全部缓存
	 */
	public void resetAllCacheFilesBackground(final String cachepath) {
		synchronized (this) {
			resetCache();
       	Thread thread = new Thread() {
			@Override
			public void run() {
		           FileUtil.deleteDirs(cachepath);
			}
		};
		thread.start();
		}
	}

	/**
	 * 删除过期文件
	 */
	public void resetExpiredFiles() {
		synchronized (this) {
			Vector<String> vec = resetCache(System.currentTimeMillis());
			deleteFilesBackground(vec);
		}
	}
	
	/**
	 * 指定某个时间点,删除过期文件
	 * @param time
	 */
	public void resetExpiredFiles(long time) {
		synchronized (this) {
			Vector<String> vec = resetCache(time);
			deleteFilesBackground(vec);
		}
	}

	public void delCacheAndFile(String url) {
		synchronized (this) {
			Vector<String> vec = delCache(url);
			deleteFilesBackground(vec);
		}
	}

	public void delExpiredCacheAndFile(String url) {
		synchronized (this) {
			Vector<String> vec = delExpiredCache(url);
			deleteFilesBackground(vec);
		}
	}

	private void deleteFilesBackground(Vector<String> _files) {
		int size = _files != null ? _files.size() : 0;
		if (size == 0)
			return;
		final Vector<String> files = _files;
		Thread thread = new Thread() {
			@Override
			public void run() {
				String filename = null;
				int size = files.size();
				for (int i = 0; i < size; i++) {
					filename = files.get(i);
					FileUtil.deleteFile(filename);
				}
			}
		};
		thread.start();
	}

	private void updateInThread(final String url, final long existtime, final String cachepath,final String cachedata) {
		try {
			if (url == null || cachedata == null) {
				return;
			}
			Runnable r = new Runnable() {

				public void run() {
					synchronized (CachedUrlManager.this) {
						try {
							String filename = cachepath;
							String[] info = getCache(url);
							if (info != null && info[2] != null) {
								filename = info[2];
							}
							FileUtil.saveFile("", filename, cachedata);
							addCache(url, existtime * 1000 + System.currentTimeMillis(), filename);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			};
			Thread t = new Thread(r);
			t.setPriority(Thread.MIN_PRIORITY);
			t.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// TODO 移动整合provider过来的

	private void addCache(String url, long time, String filename) {
		Vector<String> vec = delCache(url);
		if (vec != null) {
			for (String s : vec) {
				if (!s.equals(filename)) {
					FileUtil.deleteFile(s);
				}
			}
		}
		ContentValues cv = new ContentValues();
		cv.put(CacheProvider.mUrl, url);
		cv.put(CacheProvider.mFileName, filename);
		String timestr = String.valueOf(time);
		cv.put(CacheProvider.mExpiredTime, timestr);
		mContext.getContentResolver().insert(CacheProvider.URI_URLCACHE, cv);
	}

	private Vector<String> delCache(String url) {
		Vector<String> vec = null;
		if (url != null) {
			Cursor cursor = null;

			try {
				cursor = mContext.getContentResolver().query(CacheProvider.URI_URLCACHE, null, CacheProvider.mUrl + "=?", new String[]{url}, null);
				if (cursor != null && cursor.getCount() > 0) {
					cursor.moveToFirst();
					vec = new Vector<String>();
					vec.add(cursor.getString(cursor.getColumnIndex(CacheProvider.mFileName)));

					mContext.getContentResolver().delete(CacheProvider.URI_URLCACHE, CacheProvider.mUrl + "=?", new String[]{url});

				}
			} catch (Exception ex) {
				ex.printStackTrace();
			} finally {
				if (cursor != null) {
					cursor.close();
				}
			}
		}
		return vec;
	}

	private Vector<String> delExpiredCache(String url) {
		Vector<String> vec = null;
		if (url != null) {
			Cursor cursor = null;
			try {
				cursor = mContext.getContentResolver().query(CacheProvider.URI_URLCACHE, null, CacheProvider.mUrl + "=?", new String[]{url}, null);
				if (cursor != null && cursor.getCount() > 0) {
					cursor.moveToFirst();
					String extime = cursor.getString(cursor.getColumnIndex(CacheProvider.mExpiredTime));
					long exptime = Long.parseLong(extime);
					if (System.currentTimeMillis() > exptime) {
						vec = new Vector<String>();
						vec.add(cursor.getString(cursor.getColumnIndex(CacheProvider.mFileName)));
						mContext.getContentResolver().delete(CacheProvider.URI_URLCACHE, CacheProvider.mUrl + "=?", new String[]{url});
					}
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			} finally {
				if (cursor != null) {
					cursor.close();
				}
			}
		}
		return vec;
	}

	private Vector<String> resetCache(long time) {
		Vector<String> vec = new Vector<String>();
		Cursor cursor = null;
		cursor = mContext.getContentResolver().query(CacheProvider.URI_URLCACHE, null, null, null, null);
		if (cursor != null && cursor.getCount() > 0) {
			int size = cursor.getCount();
			for (int i = 0; i < size; i++) {
				cursor.moveToPosition(i);
				try {
					String extime = cursor.getString(cursor.getColumnIndex(CacheProvider.mExpiredTime));
					long exptime = Long.parseLong(extime);
					if (time > exptime) {
						vec.add(cursor.getString(cursor.getColumnIndex(CacheProvider.mFileName)));
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}
		if (cursor != null) {
			cursor.close();
		}
		int vecsize = vec.size();
		for (int i = 0; i < vecsize; i++) {
			String filename = vec.get(i);
			mContext.getContentResolver().delete(CacheProvider.URI_URLCACHE, CacheProvider.mFileName + "=?", new String[]{filename});
		}
		return vec;
	}

	private void resetCache() {
		mContext.getContentResolver().delete(CacheProvider.URI_URLCACHE, null, null);
	}

	private String[] getCache(String url) {
		String[] result = null;
		Cursor cursor = null;
		try {
			cursor = mContext.getContentResolver().query(CacheProvider.URI_URLCACHE, null, CacheProvider.mUrl + "=?", new String[]{url}, null);
			if (cursor != null && cursor.getCount() > 0) {
				result = new String[3];
				cursor.moveToFirst();
				result[0] = cursor.getString(cursor.getColumnIndex(CacheProvider.mUrl));
				result[1] = cursor.getString(cursor.getColumnIndex(CacheProvider.mExpiredTime));
				result[2] = cursor.getString(cursor.getColumnIndex(CacheProvider.mFileName));
			}
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
		return result;
	}

}
