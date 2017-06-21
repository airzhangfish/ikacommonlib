package com.ikags.ikalib.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * 日期帮助类
 * 
 * @author yu.zhang
 */
public class TimeUtil {
	private static String CurrentTime;
	private static String CurrentDate;

	/**
	 * 得到当前的年份 返回格式:yyyy
	 * 
	 * @return String
	 */
	public static String getCurrentYear() {
		Date NowDate = Calendar.getInstance().getTime();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
		return formatter.format(NowDate);
	}
	/**
	 * 得到当前的月份 返回格式:MM
	 * 
	 * @return String
	 */
	public static String getCurrentMonth() {
		Date NowDate = Calendar.getInstance().getTime();
		SimpleDateFormat formatter = new SimpleDateFormat("MM");
		return formatter.format(NowDate);
	}
	/**
	 * 得到当前的日期 返回格式:dd
	 * 
	 * @return String
	 */
	public static String getCurrentDay() {
		Date NowDate = Calendar.getInstance().getTime();
		SimpleDateFormat formatter = new SimpleDateFormat("dd");
		return formatter.format(NowDate);
	}
	/**
	 * 得到当前的时间，精确到毫秒,共14位 返回格式:yyyy-MM-dd HH:mm:ss
	 * 
	 * @return String
	 */
	public static String getCurrentTime() {
		Date NowDate = Calendar.getInstance().getTime();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		CurrentTime = formatter.format(NowDate);
		return CurrentTime;
	}
	public static Date convertToDate(String date) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		try {
			return formatter.parse(date);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	public static String convertToString(Date date) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		try {
			return formatter.format(date);
		} catch (Exception e) {
			// e.printStackTrace();
			return null;
		}
	}
	/**
	 * 得到当前的时间加上输入年后的时间，精确到毫秒,共19位 返回格式:yyyy-MM-dd:HH:mm:ss
	 * 
	 * @return String
	 */
	public static String getCurrentTimeAddYear(int addyear) {
		String currentYear = "";
		Date NowDate = Calendar.getInstance().getTime();
		currentYear = TimeUtil.getCurrentYear();
		currentYear = String.valueOf(Integer.parseInt(TimeUtil.getCurrentYear()) + addyear);

		SimpleDateFormat formatter = new SimpleDateFormat("-MM-dd:HH:mm:ss");
		CurrentTime = formatter.format(NowDate);
		return currentYear + CurrentTime;
	}
	/**
	 * 得到当前的日期,共10位 返回格式：yyyy-MM-dd
	 * 
	 * @return String
	 */
	public static String getCurrentDate() {
		Date NowDate = Calendar.getInstance().getTime();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		CurrentDate = formatter.format(NowDate);
		return CurrentDate;
	}
	/**
	 * 得到当前的日期,共8位 返回格式：yyyyMMdd
	 * 
	 * @return String
	 */
	public static String getDate8Bit() {
		Date NowDate = Calendar.getInstance().getTime();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
		CurrentDate = formatter.format(NowDate);
		return CurrentDate;
	}
	/**
	 * 得到当前日期加上某一个整数的日期，整数代表天数 输入参数：currentdate : String 格式 yyyy-MM-dd add_day : int 返回格式：yyyy-MM-dd
	 */
	public static String addDay(String currentdate, int add_day) {
		GregorianCalendar gc = null;
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		int year, month, day;

		try {
			year = Integer.parseInt(currentdate.substring(0, 4));
			month = Integer.parseInt(currentdate.substring(5, 7)) - 1;
			day = Integer.parseInt(currentdate.substring(8, 10));

			gc = new GregorianCalendar(year, month, day);
			gc.add(GregorianCalendar.DATE, add_day);

			return formatter.format(gc.getTime());
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	/**
	 * 得到当前月份的第一天日期
	 */
	public static String getStartDateInPeriod(String period) {
		StringBuffer str = new StringBuffer(period);
		return str.append("01").toString();

	}
	/**
	 * 得到当前月份的最后一天
	 * 
	 * @param period
	 * @return
	 */
	public static String getEndDateInPeriod(String period) {
		String date = "";
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		int year = Integer.parseInt(period.substring(0, 4));
		int month = Integer.parseInt(period.substring(5, 7));
		System.err.println(month);
		Calendar cl = Calendar.getInstance();
		cl.set(year, month - 1, 1);
		cl.add(Calendar.MONTH, 1);
		cl.add(Calendar.DATE, -1);
		date = df.format(cl.getTime());
		return date;
	}
	/**
	 * 将YYYYMMDD形式改成YYYY-MM-DD
	 * 
	 */
	public static String convertStr(String str1) {
		if (str1 == null || str1.equals("")) {
			return "";
		} else {
			String result = "";
			result += str1.substring(0, 4) + "-";
			result += str1.substring(4, 6) + "-";
			result += str1.substring(6, 8);
			return result;
		}
	}
	/**
	 * 将YYYY-MM-DD形式改成YYYYMMDD
	 * 
	 */
	public static String convert(String str1) {
		if (str1 == null || str1.equals("")) {
			return "";
		} else {
			String temp[] = str1.split("-");
			String result = "";
			for (int i = 0; i < temp.length; i++) {
				result = result + temp[i];
			}
			return result;
		}
	}

	/**
	 * 得到2个时间的小时间隔
	 * 
	 * @param startday
	 * @param endday
	 * @return
	 */
	public static int getIntervalDays(Date startday, Date endday) {
		if (startday.after(endday)) {
			Date cal = startday;
			startday = endday;
			endday = cal;
		}
		long sl = startday.getTime();
		long el = endday.getTime();
		long ei = el - sl;
		return (int) (ei / (1000 * 60 * 60));
	}

	public static String formatData(String data) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		DateFormat df = DateFormat.getDateInstance();
		Date d = null;
		try {
			d = df.parse(data);
		} catch (ParseException ex) {
			ex.printStackTrace();
		}
		return sdf.format(d);
	}

	/**
	 * 转换为微博的时间格式
	 * 
	 * @param time
	 * @return
	 */
	public static String getNewSimpleTime(String time) {
		String simpleTime = null;
		try {
			Date dateNow = Calendar.getInstance().getTime();
			long ssNow = dateNow.getTime();

			Date date = new Date(time);
			long ss = date.getTime();

			long differTime = (ssNow - ss) / 1000;

			if (differTime > 24 * 60 * 60 * 7) {
				String[] strTime = time.split(" ");
				strTime = strTime[3].split(":");
				String hh = strTime[0];
				String mm = strTime[1];
				String ymd = new SimpleDateFormat("yy/MM/dd").format(date);
				simpleTime = ymd + " " + hh + ":" + mm;
			} else if (differTime > 24 * 60 * 60) {
				simpleTime = differTime / (24 * 60 * 60) + "天前";
			} else if (differTime > 60 * 60) {
				simpleTime = differTime / (60 * 60) + "小时前";
			} else if (differTime > 60) {
				simpleTime = differTime / (60) + "分钟前";
			} else {
				simpleTime = "刚刚";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return simpleTime;
	}

	/**
	 * 转换文章的时间格式
	 * 
	 * @param time
	 * @return
	 */
	public static String getArticleimpleTime(String time) {
		String simpleTime = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		try {
			Date dateNow = Calendar.getInstance().getTime();
			long ssNow = dateNow.getTime();
			Date date = sdf.parse(time);
			long ss = date.getTime();

			long differTime = (ssNow - ss) / 1000;

			if (differTime > 24 * 60 * 60 * 7) {// 大于一周
				String[] strTime = time.split(" ");
				strTime = strTime[1].split(":");
				String hh = strTime[0];
				String mm = strTime[1];
				String ymd = new SimpleDateFormat("yy/MM/dd").format(date);
				simpleTime = ymd + " " + hh + ":" + mm;
			} else if (differTime > 24 * 60 * 60) {
				simpleTime = differTime / (24 * 60 * 60) + "天前";
			} else if (differTime > 60 * 60) {
				simpleTime = differTime / (60 * 60) + "小时前";
			} else if (differTime > 60) {
				simpleTime = differTime / (60) + "分钟前";
			} else {
				simpleTime = "刚刚";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return simpleTime;
	}
}
