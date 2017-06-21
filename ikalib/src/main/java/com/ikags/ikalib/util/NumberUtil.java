package com.ikags.ikalib.util;
/**
 * 数字转换相关
 * 
 * @author Apple
 * 
 */
public class NumberUtil {

	public static int getInt(String intstr) {
		int number = getInt(intstr, 0);
		return number;
	}

	public static int getInt(String intstr, int defaultNumber) {
		int intin = defaultNumber;
		try {
			intin = Integer.parseInt(intstr);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return intin;
	}

	public static float getFloat(String intstr) {
		float number = getFloat(intstr, 0);
		return number;
	}

	public static float getFloat(String intstr, float defaultNumber) {
		float intin = defaultNumber;
		try {
			intin = Float.parseFloat(intstr);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return intin;
	}

	public static long getLong(String intstr) {
		long number = getLong(intstr, 0);
		return number;
	}

	public static long getLong(String intstr, long defaultNumber) {
		long intin = defaultNumber;
		try {
			intin = Long.parseLong(intstr);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return intin;
	}
}
