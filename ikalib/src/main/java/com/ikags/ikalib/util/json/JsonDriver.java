package com.ikags.ikalib.util.json;

import java.io.InputStream;

import org.json.JSONArray;
import org.json.JSONObject;

import com.ikags.ikalib.util.StringUtil;
/**
 * JSON处理方法封装
 * @author zhangxiasheng
 *
 */
public class JsonDriver {
	/**
	 * 读取文本方式的JSONObject
	 * 
	 * @param text
	 * @return
	 */
	public static JSONObject loadJSONObject(String text) {
		JSONObject obj = null;
		try {
			obj = new JSONObject(text);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return obj;
	}

	/**
	 * 读取数据流的JSONObject
	 * 
	 * @param text
	 * @return
	 */
	public static JSONObject loadJSONObject(InputStream input) {
		String text = StringUtil.getInputStreamText(input, "UTF-8");
		return loadJSONObject(text);
	}

	/**
	 * 读取文本方式的JSONArray
	 * 
	 * @param text
	 * @return
	 */
	public static JSONArray loadJSONArray(String text) {
		JSONArray array = null;
		try {
			array = new JSONArray(text);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return array;
	}

	/**
	 * 读取数据流的JSONArray
	 * 
	 * @param text
	 * @return
	 */
	public static JSONArray loadJSONArray(InputStream input) {
		String text = StringUtil.getInputStreamText(input, "UTF-8");
		return loadJSONArray(text);
	}

}
