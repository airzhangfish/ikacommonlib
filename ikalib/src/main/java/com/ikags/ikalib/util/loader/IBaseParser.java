package com.ikags.ikalib.util.loader;

import java.io.InputStream;
import java.net.HttpURLConnection;

/**
 * 解析器抽象类
 * @author zhangxiasheng
 *
 */
public abstract class IBaseParser {

	public void doParse(String url, HttpURLConnection httpresp, InputStream is, String postData, String tag) {}
}
