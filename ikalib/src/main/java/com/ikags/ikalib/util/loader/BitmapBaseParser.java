package com.ikags.ikalib.util.loader;

import java.io.InputStream;
import java.net.HttpURLConnection;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.ikags.ikalib.util.BitmapUtils;
import com.ikags.ikalib.util.CommonDef;
import com.ikags.ikalib.util.FileUtil;
import com.ikags.ikalib.util.IKALog;
import com.ikags.ikalib.util.cache.CachedUrlManager;
/**
 * 网络图片处理解析器
 * @author zhangxiasheng
 *
 */
public abstract class BitmapBaseParser extends IBaseParser {

	private static final String TAG = "BitmapBaseParser";

	@Override

	public void doParse(String url, HttpURLConnection httpresp, InputStream is, String postData, String tag) {
		Bitmap bitmap = null;
		try {
			BitmapFactory.Options options = new BitmapFactory.Options();  
			options.inPreferredConfig=Bitmap.Config.RGB_565;
			options.inPurgeable = true;  
			options.inInputShareable = true; 
			bitmap = BitmapFactory.decodeStream(is,null,options);
			System.gc();
			IKALog.v(TAG, "end load bitmap ok=" + url);
		} catch (Exception ex) {
			IKALog.v(TAG, "end load bitmap error=" + url);
			ex.printStackTrace();
		}
		// 加载onBitmapLoad
		if (httpresp != null) {
			onBitmapLoad(url, bitmap, tag, true);
		} else {
			onBitmapLoad(url, bitmap, tag, false);
		}

		try {
			// 缓存图片
			if (httpresp != null && httpresp.getResponseCode() == 200&& bitmap != null) {
				String filepath = CommonDef.getCachePath();
				String filename =FileUtil.getRandomName() + ".ikacbmp";
				BitmapUtils.saveBitmapFile(bitmap, filepath,filename);
				CachedUrlManager.getDefault(null).updateCacheAndFile(url, filepath+"/"+filename, CommonDef.CACHE_MAXTIME_IMAGE);
			}
		} catch (Exception ex) {
			IKALog.v(TAG, "cache bitmap file error=" + url);
			ex.printStackTrace();
		}
	}

	public abstract void onBitmapLoad(String url, Bitmap bitmap, String tag, boolean isNetData);
}
