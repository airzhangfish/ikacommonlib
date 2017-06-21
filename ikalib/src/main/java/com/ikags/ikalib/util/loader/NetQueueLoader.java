package com.ikags.ikalib.util.loader;


import android.content.Context;
import android.graphics.Bitmap;
/**
 * 排序方式解析网络, 按照正常方式使用.最后调用startQueueLoader()即可一个一个排队解析
 * @author zhangxiasheng
 *
 */
public class NetQueueLoader extends NetLoader {

	private static NetQueueLoader mSingleInstance = null;
	public final static String TAG = "NetQueueLoader";
	public NetQueueLoader(Context contex) {
		super(contex);
	}

	public static NetQueueLoader getDefault(Context context) {
		if (mSingleInstance == null) {
			mSingleInstance = new NetQueueLoader(context);
		}
		return mSingleInstance;
	}

	public void loadUrl(String url, String postdata, IMakeHttpHead mkHttpHead, IBaseParser parser, String itemtag,boolean readCache) {
		try {
			UrlLoadThread thread = new UrlLoadThread();
			url = NetLoader.trimURL(url);
			thread.mItem = new UrlLoadItem(url, postdata, mkHttpHead, parser, itemtag,readCache);
			mThreadList.add(thread);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public void loadUrl(String url, byte[] hpEntity, IMakeHttpHead mkHttpHead, IBaseParser parser, String itemtag,boolean readCache) {
		try {
			UrlLoadThread thread = new UrlLoadThread();
			url = NetLoader.trimURL(url);
			thread.mItem = new UrlLoadItem(url, hpEntity, mkHttpHead, parser, itemtag,readCache);
			mThreadList.add(thread);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	Thread mQueueList = null;
/**
 * 新建一个线程,按照顺序执行下载
 */
	public void startQueueLoader() {
		
		mQueueList=	new Thread() {
			public void run() {
				int size=mThreadList.size();
				for (int i = 0; i < size; i++) {
					try {
						Thread t = mThreadList.get(0);
						if (t != null) {
							t.run();
						}
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
				mThreadList.clear();
			}
		};
		
		mQueueList.setPriority(Thread.MIN_PRIORITY);
			mQueueList.start();
	}
	
	/**
	 * 直接在当前线程进行下载
	 */
	public void startQueueLoaderThisThread() {
				int size=mThreadList.size();
				for (int i = 0; i < size; i++) {
					try {
						Thread t = mThreadList.get(0);
						if (t != null) {
							t.run();
						}
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
				mThreadList.clear();
	}
	
/**
 * 当前线程阻塞式下载数据(字符串)
 * @param url
 * @param hpEntity
 * @param mkHttpHead
 * @param encode
 * @param readCache
 * @return
 */
	public String loadUrlStringData(String url, byte[] hpEntity, IMakeHttpHead mkHttpHead,String encode,boolean readCache) {
		String data=null;
		try {
			UrlLoadThread thread = new UrlLoadThread();
			url = NetLoader.trimURL(url);
			thread.mItem = new UrlLoadItem(url, hpEntity, mkHttpHead, null, null,readCache);
			data=thread.getMainNetDataString(encode);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return data;
	}
	
	/**
	 * 当前线程阻塞式下载数据(图片)
	 * @param url
	 * @param hpEntity
	 * @param mkHttpHead
	 * @param readCache
	 * @return
	 */
	public Bitmap loadUrlBitmapData(String url, byte[] hpEntity, IMakeHttpHead mkHttpHead,boolean readCache) {
		Bitmap data=null;
		try {
			UrlLoadThread thread = new UrlLoadThread();
			url = NetLoader.trimURL(url);
			thread.mItem = new UrlLoadItem(url, hpEntity, mkHttpHead, null, null,readCache);
			data=thread.getMainNetDataBitmap();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return data;
	}
	
	/**
	 * 当前线程阻塞式下载数据(byte数组)
	 * @param url
	 * @param hpEntity
	 * @param mkHttpHead
	 * @param readCache
	 * @return
	 */
	public byte[] loadUrlBytesData(String url, byte[] hpEntity, IMakeHttpHead mkHttpHead,boolean readCache) {
		byte[] data=null;
		try {
			UrlLoadThread thread = new UrlLoadThread();
			url = NetLoader.trimURL(url);
			thread.mItem = new UrlLoadItem(url, hpEntity, mkHttpHead, null, null,readCache);
			data=thread.getMainNetDataBytes();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return data;
	}
	
}
