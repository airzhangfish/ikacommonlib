package com.ikags.ikalib.util.loader;

import org.json.JSONObject;
/**
 * 网络数据JSON解析器
 * @author zhangxiasheng
 *
 */
public abstract class JsonBaseParser extends TextBaseParser {

	public static final String TAG = "JsonBaseParser";
	@Override
	public void parsetTextData(String url, String text, String tag, boolean isNetdata) {
		JSONObject jsonObject = null;
		try {
			jsonObject = new JSONObject(text.trim());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		//加载解析器
		parseJsonData(url, jsonObject, tag,isNetdata);
	}
	

	abstract public void parseJsonData(String url,  JSONObject jsonObject, String tag,boolean isNetdata);
	
}
