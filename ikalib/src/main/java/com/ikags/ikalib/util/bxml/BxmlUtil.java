package com.ikags.ikalib.util.bxml;

import com.ikags.ikalib.util.IKALog;
/**
 * 涉及到xml的一些类型的转换
 * @author zhangxiasheng
 *
 */
public class BxmlUtil {
	public final static String TAG = "BxmlUtil";

	
	/**
	 * 转换string 到int,默认0
	 * @param data
	 * @return
	 */
	public static int getInt(String data) {
		int tmpint = 0;
		try {
			tmpint = Integer.parseInt(data);
		} catch (Exception ex) {
			IKALog.e(TAG, "Exception_getInt=" + data);
		}
		return tmpint;
	}
	
	/**
	 * 转换string 到double,默认0
	 * @param data
	 * @return
	 */
	public static double getDouble(String data) {
		double intdata = 0;
		try {
			intdata = Double.parseDouble(data);
		} catch (Exception ex) {
			IKALog.e(TAG, "Exception_getDouble=" + data);
			// ex.printStackTrace();
		}
		return intdata;
	}
	
	/**
	 * 如果传入的string是null,则转化为""
	 * @param data
	 * @return
	 */
	public static String getStringClearNull(String data) {
		String newdata="";
		if(data==null){
			return newdata;
        }
		try {
			newdata = data;
		} catch (Exception ex) {
			IKALog.e(TAG, "Exception_getString_clearNull=" + data);
		}
		return newdata;
	}

}
