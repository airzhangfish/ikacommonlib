package com.ikags.ikalib.util.loader;

import java.io.InputStream;
import java.net.HttpURLConnection;


import com.ikags.ikalib.util.CommonDef;
import com.ikags.ikalib.util.FileUtil;
import com.ikags.ikalib.util.StringUtil;
import com.ikags.ikalib.util.cache.CachedUrlManager;
/**
 * 网络文本解析器
 * @author zhangxiasheng
 *
 */
public abstract class TextBaseParser extends IBaseParser {

	public static final String TAG = "TextBaseParser";

	@Override
	public void doParse(String url, HttpURLConnection httpresp, InputStream is, String postData, String itemtag) {
		String data = StringUtil.getInputStreamText(is, "UTF-8");
		if(data!=null){
			data=data.trim();
		}
		//加载解析器
		if(httpresp!=null){
			parsetTextData(url, data, itemtag,true);
		}else{
			parsetTextData(url, data, itemtag,false);	
		}
		
		//处理缓存
		try {
			if (httpresp != null && httpresp.getResponseCode() == 200 && data != null) {
				String filepathname = CommonDef.getCachePath() + "/" + FileUtil.getRandomName() + ".ikactxt";
				CachedUrlManager.getDefault(null).updateCacheAndText(url, CommonDef.CACHE_MAXTIME_TEXT, filepathname, data);
			}
		}catch (Exception ex){ex.printStackTrace();}
	}

	abstract public void parsetTextData(String url, String text, String tag,boolean isNetdata);
}
