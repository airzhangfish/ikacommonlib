package com.ikags.ikalib.util.loader;

import java.net.HttpURLConnection;
import java.util.Map;
/**
 * http头部封装类
 * @author zhangxiasheng
 *
 */
public abstract class IMakeHttpHead {

	public void makeHttpHead(HttpURLConnection connection, boolean isgzip) {}

	public Map<String, String> getHeaders(boolean isgzip) {
		return null;
	}
}
