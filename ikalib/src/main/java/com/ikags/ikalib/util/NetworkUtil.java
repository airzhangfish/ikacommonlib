package com.ikags.ikalib.util;

import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.provider.Settings;
import android.telephony.TelephonyManager;

/**
 * 这个类主要把多APN部分的功能抽取出来，多APN版本直接替换该类
 * */
public class NetworkUtil {
	public static final String TAG = "NetworkManager";

	/** 网络类型 **/
	// public static final String NETWORK_AUTHOR_CMMM_CMWAP = "1";
	// public static final String NETWORK_AUTHOR_WIFI_CMNET = "2";
	// public static final String NETWORK_UNAUTHOR = "3";

	public static final String AUTHOR_NETWORK = "0"; // 授信网络(含NETWORK_AUTHOR_CMMM_CMWAP和NETWORK_AUTHOR_WIFI_CMNET)
	public static final String UNAUTHOR_NETWORK = "3"; // 非授信网络(含NETWORK_UNAUTHOR)

	/** APN类型 **/
	public static final int APN_TYPE_CMMM = 1;// cmmm通道
	public static final int APN_TYPE_CMNET = 2;// cmnet
	public static final int APN_TYPE_CMWAP = 3;// cmwap
	public static final int APN_TYPE_WIFI = 4;// wifi
	public static final int APN_TYPE_WIFI_UNAUTHOR = 5;// 非授信
	public static final int APN_TYPE_UNKNOW = 6;
	public static final String APN_NAME_CMMM = "cmmm";
	public static final String APN_NAME_CMWAP = "cmwap";
	public static final String APN_NAME_CMNET = "cmnet";
	public static final String APN_NAME_UNWAP = "uniwap";
	public static final String APN_NAME_UNNET = "uninet";

	private static boolean gQueryedDualMode = false; // 是否查询过双模,只查一次即可
	private static boolean gIsDualMode = false; // 是否为双模

	//
	public static final int LOGIN_TYPE_HIDE = 1;
	public static final int LOGIN_TYPE_SHOW = 2;
	/** 默认手机imsi号 */
	public static final String NO_SIM_IMSI = "1234567890";
	/** 默认IMEI号 **/
	public static final String NO_DEVICE_IMEI = "000000000000000";
	// 获取手机飞行模式是否已打开
	public static boolean isAirplaneModeEnabled(Context context) {
		int mode = Settings.System.getInt(context.getContentResolver(), Settings.System.AIRPLANE_MODE_ON, 0);
		IKALog.v(TAG, "isAirplaneModeEnabled:mode = " + mode);
		return mode == 1;
	}

	/**
	 * 
	 * @return 返回是否双模
	 */
	public static boolean isDualMode() {
		if (gQueryedDualMode)
			return gIsDualMode;
		Object obj1 = ReflectHelper.callStaticMethod("android.os.ServiceManager", "getService", new Class<?>[]{String.class}, new Object[]{"phone"});
		Object obj2 = ReflectHelper.callStaticMethod("android.os.ServiceManager", "getService", new Class<?>[]{String.class}, new Object[]{"phone2"});
		gIsDualMode = obj1 != null && obj2 != null;
		if (!gIsDualMode) {
			isDualMode2();
		}
		gQueryedDualMode = true;
		return gIsDualMode;
	}

	private static boolean isDualMode2() {
		if (gQueryedDualMode)
			return gIsDualMode;
		Object obj1 = ReflectHelper.callStaticMethod("android.os.ServiceManager", "getService", new Class<?>[]{String.class}, new Object[]{"iphonesubinfo"});
		Object obj2 = ReflectHelper.callStaticMethod("android.os.ServiceManager", "getService", new Class<?>[]{String.class}, new Object[]{"iphonesubinfo2"});
		gIsDualMode = obj1 != null && obj2 != null;
		gQueryedDualMode = true;
		return gIsDualMode;
	}

	public static int getFirstSimCardState() {
		String prop = (String) ReflectHelper.callStaticMethod("android.os.SystemProperties", "get", new Class<?>[]{String.class}, new Object[]{"gsm.sim.state"});

		if ("ABSENT".equals(prop)) {
			return TelephonyManager.SIM_STATE_ABSENT;
		} else if ("PIN_REQUIRED".equals(prop)) {
			return TelephonyManager.SIM_STATE_PIN_REQUIRED;
		} else if ("PUK_REQUIRED".equals(prop)) {
			return TelephonyManager.SIM_STATE_PUK_REQUIRED;
		} else if ("NETWORK_LOCKED".equals(prop)) {
			return TelephonyManager.SIM_STATE_NETWORK_LOCKED;
		} else if ("READY".equals(prop)) {
			return TelephonyManager.SIM_STATE_READY;
		} else {
			return TelephonyManager.SIM_STATE_UNKNOWN;
		}
	}

	public static int getSecondSimCardState() {
		String prop = (String) ReflectHelper.callStaticMethod("android.os.SystemProperties", "get", new Class<?>[]{String.class}, new Object[]{"gsm.sim.state_2"});// 适用于：ZTE-T U960

		if ("ABSENT".equals(prop)) {
			return TelephonyManager.SIM_STATE_ABSENT;
		} else if ("PIN_REQUIRED".equals(prop)) {
			return TelephonyManager.SIM_STATE_PIN_REQUIRED;
		} else if ("PUK_REQUIRED".equals(prop)) {
			return TelephonyManager.SIM_STATE_PUK_REQUIRED;
		} else if ("NETWORK_LOCKED".equals(prop)) {
			return TelephonyManager.SIM_STATE_NETWORK_LOCKED;
		} else if ("READY".equals(prop)) {
			return TelephonyManager.SIM_STATE_READY;
		} else {
			return TelephonyManager.SIM_STATE_UNKNOWN;
		}
	}

	/**
	 * 
	 * @param secondSIMCard
	 *            :true 读取第二张SIM CARD的IMSI码
	 * @return 返回SIM的IMSI码
	 */
	public static String getSubscriberId(boolean secondSIMCard) {
		Object phoneSubObj = ReflectHelper.callStaticMethod("android.os.ServiceManager", "getService", new Class<?>[]{String.class}, new Object[]{secondSIMCard ? "iphonesubinfo2" : "iphonesubinfo1"});
		// 三星B9062的第一个服务为iphonesubinfo1，若指定为iphonesubinfo的话，它会自动选择iphonesubinfo1或iphonesubinfo2
		// 其他双模或单模机的第一个服务为iphonesubinfo，
		if (!secondSIMCard && phoneSubObj == null) {
			phoneSubObj = ReflectHelper.callStaticMethod("android.os.ServiceManager", "getService", new Class<?>[]{String.class}, new Object[]{"iphonesubinfo"});
		}
		if (phoneSubObj == null && secondSIMCard) {
			return getSubscriberId(false); // 不是双模
		}
		if (phoneSubObj == null)
			return "";
		Object subinfoObj = ReflectHelper.callStaticMethod("com.android.internal.telephony.IPhoneSubInfo$Stub", "asInterface", new Class<?>[]{IBinder.class}, new Object[]{phoneSubObj});
		if (subinfoObj == null)
			return "";
		String val = (String) ReflectHelper.callMethod(subinfoObj, "getSubscriberId", null, null);
		IKALog.w(TAG, "getSubscriberId=" + val);
		return val;
	}

	/**
	 * 根据imsi值获取卡槽位置
	 * 
	 * @param imsi
	 * @return
	 */
	public static int getImsiIndex(String imsi) {

		if (isDualMode()) {// 双卡
			String subscriberid1 = NetworkUtil.getSubscriberId(false);
			IKALog.w(TAG, "getImsiIndex1 imsi=" + imsi + ",sub=" + subscriberid1);
			if (subscriberid1 != null && subscriberid1.equals(imsi)) {
				return 0;
			} else {
				String subscriberid2 = NetworkUtil.getSubscriberId(true);
				IKALog.w(TAG, "getImsiIndex2 imsi=" + imsi + ",sub=" + subscriberid2);
				if (subscriberid2 != null && subscriberid2.equals(imsi)) {
					return 1;
				} else {
					return 2;// -1;//这样情况怎么处理？暂时wei2
				}
			}
		} else {
			IKALog.w(TAG, "getImsiIndex3 imsi=" + imsi);
			if (imsi.equals(NetworkUtil.NO_SIM_IMSI)) {// 无sim卡手机或PAD情况无IMSI信息
				return 2;
			}
			return 0;// 单卡
		}
	}

	/**
	 * 
	 * @return 双模卡中为中移动卡的IMSI号
	 */
	public static String getChinaMobileSubscriberId() {
		String imsi = getSubscriberId(true);
		if (!isChinaMobileNet(imsi)) {
			imsi = getSubscriberId(false);
		}
		if (!isChinaMobileNet(imsi)) {
			imsi = "";
		}
		IKALog.v(TAG, "getChinaMobileSubscriberId imsi=" + imsi);
		return imsi;
	}

	/**
	 * 
	 * @return 返回找到的第一个移动卡的槽位号,0表示第一张，1表示第2张,-1表示未找到
	 */
	public static int getChinaMobileCardIndex() {
		String imsi = getSubscriberId(false); // 检查第一个槽位是不是装了移动卡
		if (isChinaMobileNet(imsi))
			return 0;
		imsi = getSubscriberId(true); // 检查第二个槽位是不是装了移动卡
		if (isChinaMobileNet(imsi))
			return 1;
		else
			return -1;
	}

	/**
	 * 
	 * @param secondICC
	 *            :true 读取第二个SIM ICC
	 * @return
	 */
	public static String getICCOperator(boolean secondICC) {
		String val = getSubscriberId(secondICC);
		if (val == null)
			return "";
		int end = 5;// 46003
		if (end > val.length())
			end = val.length();
		val = val.substring(0, end);
		IKALog.v(TAG, "getICCOperator ICC=" + val);
		return val;
	}

	public static String getICCOperator(Context context) {
		TelephonyManager telmanager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		String iccopr = null;
		if (isDualMode()) {
			iccopr = getICCOperator(true);
			if (!isChinaMobileNet(iccopr))
				iccopr = getICCOperator(false);
		} else {
			iccopr = telmanager.getSimOperator();
		}
		IKALog.v(TAG, "getICCOperator: " + iccopr);
		return iccopr;
	}

	public static boolean isUnicomNet(Context context) {
		return isUnicomNet(context, getICCOperator(context));
	}

	// /**
	// * 是否非授信网络：电信或联通
	// * @param context
	// * @return
	// */
	// public static boolean isUnAuthNet(Context context) {
	// return !isChinaMobileNet(context, getICCOperator(context));
	// }

	public static boolean isUnicomNet(Context context, String iccopr) {
		String mcc = "";
		String mnc = "";
		if (iccopr != null && iccopr.length() > 4) {
			mcc = iccopr.substring(0, 3);
			mnc = iccopr.substring(3, 5);
		}
		if (mcc.equals("460") && (mnc.equals("01"))) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean isChinaMobileNet(Context context) {
		if (isDualMode())
			return isChinaMobileNet(getChinaMobileSubscriberId());
		else
			return isChinaMobileNet(getICCOperator(context));
		// return false;
	}

	public static boolean isChinaMobileNet(String iccopr) {
		String mcc = "";
		String mnc = "";
		if (iccopr != null && iccopr.length() > 4) {
			mcc = iccopr.substring(0, 3);
			mnc = iccopr.substring(3, 5);
		}
		if (mcc.equals("460") && (mnc.equals("00") || mnc.equals("02") || mnc.equals("07"))) { // 07-TD-SCDMA
			return true;
		} else {
			return false;
		}
	}

	public static boolean isCMMMNetwork(Context context) {
		NetworkInfo ni = getActiveNetworkInfo(context);
		return isCMMMNetwork(context, ni);
	}

	public static boolean isCMMMNetwork(Context context, NetworkInfo ni) {
		if (ni == null) {
			return false;
		}
		if (!isChinaMobileNet(context)) {
			return false;
		}
		return isCMMMNetwork(ni);
	}

	public static boolean isCMMMNetwork(NetworkInfo ni) {
		if (ni == null)
			return false;
		String typename = ni.getTypeName();
		String extraInfo = ni.getExtraInfo();
		if (typename == null)
			typename = "";
		if (extraInfo == null)
			extraInfo = "";
		if (typename.toLowerCase().contains(APN_NAME_CMMM))
			return true;
		if (extraInfo.toLowerCase().contains(APN_NAME_CMMM))
			return true;
		else
			return false;
	}

	public static boolean isCMWAPNetwork(Context context) {
		NetworkInfo ni = getActiveNetworkInfo(context);
		return isCMWAPNetwork(context, ni);
	}

	public static boolean isCMWAPNetwork(Context context, NetworkInfo ni) {
		if (ni == null) {
			return false;
		}
		if (!isChinaMobileNet(context)) {
			return false;
		}
		return isCMWAPNetwork(ni);
	}

	public static boolean isCMWAPNetwork(NetworkInfo ni) {
		if (ni == null)
			return false;
		String typename = ni.getTypeName();
		String extraInfo = ni.getExtraInfo();
		if (typename == null)
			typename = "";
		if (extraInfo == null)
			extraInfo = "";
		if (typename.toLowerCase().contains(APN_NAME_CMWAP))
			return true;
		if (extraInfo.toLowerCase().contains(APN_NAME_CMWAP))
			return true;
		else
			return false;
	}

	public static boolean isUNIWAPNetwork(NetworkInfo ni) {
		if (ni == null)
			return false;
		String typename = ni.getTypeName();
		String extraInfo = ni.getExtraInfo();
		if (typename == null)
			typename = "";
		if (extraInfo == null)
			extraInfo = "";
		if (typename.toLowerCase().contains(APN_NAME_UNWAP))
			return true;
		if (extraInfo.toLowerCase().contains(APN_NAME_UNWAP))
			return true;
		else
			return false;
	}

	public static boolean isUNINETNetwork(NetworkInfo ni) {
		if (ni == null)
			return false;
		String typename = ni.getTypeName();
		String extraInfo = ni.getExtraInfo();
		if (typename == null)
			typename = "";
		if (extraInfo == null)
			extraInfo = "";
		if (typename.toLowerCase().contains(APN_NAME_UNNET))
			return true;
		if (extraInfo.toLowerCase().contains(APN_NAME_UNNET))
			return true;
		else
			return false;
	}

	public static boolean isOtherWAPNetwork(Context context) {
		NetworkInfo ni = getActiveNetworkInfo(context);
		return isOtherWAPNetwork(context, ni);
	}

	public static boolean isOtherWAPNetwork(Context context, NetworkInfo ni) {
		if (ni == null) {
			return false;
		}
		if (isChinaMobileNet(context)) {
			return false;
		}
		return isOtherWAPNetwork(ni);
	}

	public static boolean isOtherWAPNetwork(NetworkInfo ni) {
		if (ni == null)
			return false;
		String typename = ni.getTypeName();
		String extraInfo = ni.getExtraInfo();
		if (typename == null)
			typename = "";
		if (extraInfo == null)
			extraInfo = "";
		if (typename.toLowerCase().contains("wap"))
			return true;
		if (extraInfo.toLowerCase().contains("wap"))
			return true;
		else
			return false;
	}

	public static boolean isCMNETNetwork(Context context) {
		NetworkInfo ni = getActiveNetworkInfo(context);
		return isCMNETNetwork(context, ni);
	}

	public static boolean isCMNETNetwork(Context context, NetworkInfo ni) {
		if (ni == null) {
			return false;
		}
		if (!isChinaMobileNet(context)) {
			return false;
		}
		return isCMNETNetwork(ni);
	}

	public static boolean isCMNETNetwork(NetworkInfo ni) {
		if (ni == null)
			return false;
		String typename = ni.getTypeName();
		String extraInfo = ni.getExtraInfo();
		if (typename == null)
			typename = "";
		if (extraInfo == null)
			extraInfo = "";
		if (typename.toLowerCase().contains(APN_NAME_CMNET))
			return true;
		if (extraInfo.toLowerCase().contains(APN_NAME_CMNET))
			return true;
		else
			return false;
	}

	public static boolean isEmulatornetwork(Context context) {
		NetworkInfo ni = getActiveNetworkInfo(context);
		return isEmulatornetwork(context, ni);
	}

	public static boolean isEmulatornetwork(Context context, NetworkInfo ni) {
		if (ni == null) {
			return false;
		}
		String extraInfo = ni.getExtraInfo();
		if (extraInfo == null) {
			return false;
		}
		if (extraInfo.contains("internet") || extraInfo.equalsIgnoreCase("internet")) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean isCMCCNetwork(Context context) {
		NetworkInfo ni = getActiveNetworkInfo(context);
		return isCMCCNetwork(context, ni);
	}

	public static boolean isCMCCNetwork(Context context, NetworkInfo ni) {
		if (!isWLANNetwork(context, ni)) {
			return false;
		}
		if (isCMCCNetwork(getWifiSsid(context))) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean isCMCCNetwork(String ssid) {
		if (ssid != null && ssid.equalsIgnoreCase("cmcc"))// cmcc
		{
			return true;
		} else {
			return false;
		}
	}

	public static String getWifiSsid(Context context) {
		WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		String ssid = null;
		if (wifiManager != null) {
//			WifiInfo wifiInfo = wifiManager.getConnectionInfo();
//			ssid = wifiInfo.getSSID();
		}
		IKALog.v(TAG, "getWifiSsid: " + ssid);
		return ssid;
	}

	public static boolean isWLANNetwork(Context context) {
		NetworkInfo ni = getActiveNetworkInfo(context);
		return isWLANNetwork(context, ni);
	}

	public static boolean isWLANNetwork(Context context, NetworkInfo ni) {
		if (ni == null || ni.getType() != ConnectivityManager.TYPE_WIFI) {
			return false;
		} else {
			return true;
		}
	}

	public static boolean isMobileNetwork(Context context) {
		return isMobileNetwork(context, getActiveNetworkInfo(context));
	}

	public static boolean isMobileNetwork(Context context, NetworkInfo ni) {
		// mul_apn_modify
		// TODO
		if (ni != null) {
			if (ni.getType() == ConnectivityManager.TYPE_MOBILE) {
				return true;
			} else if (isCMMMNetwork(ni) || isCMWAPNetwork(ni) || isCMNETNetwork(ni)) {
				return true;
			}
		}
		return false;
	}

	public static boolean isAuthenticNetwork(Context context) {
		// zwj：以前是移动卡下面cmmm和cmwap为授信。现在只要移动卡（增加cmnet和wifo通过短信方式）就是授信
		return isChinaMobileNet(context);
	}

	public static boolean isAuthenticNetwork(Context context, NetworkInfo ni) {
		if (ni == null) {
			return false;
		}
		return isChinaMobileNet(context);
	}

	public static NetworkInfo getActiveNetworkInfo(Context context) {
		final ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = null;
		int preferenceNetwork = cm.getNetworkPreference();
		for (NetworkInfo nwi : cm.getAllNetworkInfo()) {
			if (nwi.getType() == preferenceNetwork && nwi.isConnected()) {
				ni = nwi;
				break;
			}
		}
		if (ni == null) {
			ni = cm.getActiveNetworkInfo();
		}
		IKALog.v(TAG, "getActiveNetworkInfo: " + ni);
		return ni;
	}

	public static boolean isNetworkAvailable(Context context) {
		if (context == null) {
			return false;
		}
		ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity == null) {
			return false;
		} else {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		}
		return false;
	}

	public static NetworkInfo[] getActiveNetworkInfos(Context context) {
		final ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		ArrayList<NetworkInfo> infos = new ArrayList<NetworkInfo>();
		for (NetworkInfo ni : cm.getAllNetworkInfo()) {
			if (ni.isConnected()) {
				infos.add(ni);
				IKALog.d(TAG, "getActiveNetworkInfos: " + ni);
			}
		}
		IKALog.v(TAG, "getActiveNetworkInfos: size = " + infos.size());
		if (infos.size() == 0)
			return null;
		NetworkInfo[] nis = new NetworkInfo[infos.size()];
		infos.toArray(nis);
		return nis;
	}

	// mul_apn_modify
	// TODO
	// private static boolean ensureRouteToHost(Context context, String apnType,
	// String ipAddress) {
	// IKALog.v(TAG, "ensureRouteToHost apn = " + apnType + " ip = " +
	// ipAddress);
	// String config[] = { apnType, ipAddress, };
	// ConnectivityManager cm = (ConnectivityManager)
	// context.getSystemService(Context.CONNECTIVITY_SERVICE);
	// boolean bConfigNw = cm.configureNetwork(ConnectivityManager.TYPE_MOBILE,
	// config);
	// IKALog.v(TAG, "configureNetwork return " + bConfigNw);
	// return bConfigNw;
	// }

	/**
	 * 多APN版本调用的方法，标准版里只保留空方法
	 * */
	public static boolean ensureRouteToHost(Context context, String url) {
		// mul_apn_modify
		// TODO

		return false;
	}

	/***
	 * 用于判断apn是否属于授信网络类型,此方法主要用在数据库操作上 将APN类型转化为相应的字段，将注册登录的结果，写到相应的记录中
	 *
	 *            网络类型 APN_TYPE_CMMM, APN_TYPE_CMWAP, APN_TYPE_WIFI, APN_TYPE_CMNET都属于授信网络
	 * @return 授信/非授信
	 */
	public static String getAuthNetWork(Context context) {
		/*
		 * switch (apnType) { case APN_TYPE_CMMM: case APN_TYPE_CMWAP: case APN_TYPE_WIFI: case APN_TYPE_CMNET: return AUTHOR_NETWORK; case APN_TYPE_UNKNOW: case APN_TYPE_WIFI_UNAUTHOR: return UNAUTHOR_NETWORK; } return UNAUTHOR_NETWORK;
		 */
		if (!isChinaMobileNet(context)) { // 非授信
			return UNAUTHOR_NETWORK;
		} else {
			return AUTHOR_NETWORK;
		}
	}

	public static int getCurrentNetworkType(Context context) {
		NetworkInfo ni = getActiveNetworkInfo(context);
		return getCurrentNetworkType(context, ni);
	}

	public static int getCurrentNetworkType(Context context, NetworkInfo ni) {
		if (ni == null) {
			return APN_TYPE_UNKNOW;
		}
		if (!isChinaMobileNet(context)) {
			return APN_TYPE_WIFI_UNAUTHOR;
		} else {
			if (ni.getType() == ConnectivityManager.TYPE_WIFI) {
				return APN_TYPE_WIFI;
			} else {// if (isMobileNetwork(context, ni)) {
				if (isCMMMNetwork(ni)) {
					return APN_TYPE_CMMM;
				} else if (isCMWAPNetwork(ni)) {
					return APN_TYPE_CMWAP;
				} else if (isCMNETNetwork(ni)) {
					return APN_TYPE_CMNET;
				} else {
					return APN_TYPE_UNKNOW;
				}
			}
			// else {
			// return APN_TYPE_UNKNOW;
			// }
		}
	}

	public static String getAPNName(NetworkInfo ni) {
		if (ni == null)
			return "";
		if (isCMMMNetwork(ni)) {
			return "cmmm";
		} else if (isCMWAPNetwork(ni)) {
			return "cmwap";
		} else if (isCMNETNetwork(ni)) {
			return "cmnet";
		} else {
			return ni.getExtraInfo();
		}
	}

	/**
	 * APN字符串类型定义：CMMM；CMWAP；CMNET；WLAN；UNWAP；UNNET；
	 * 
	 * @param context
	 * @param ni
	 * @return
	 */
	public static String getActiveAPNName(Context context, NetworkInfo ni) {
		if (ni == null)
			return "OTHER";
		if (!isChinaMobileNet(context)) {// 非移动卡
			if (ni.getType() == ConnectivityManager.TYPE_WIFI) {// WLAN
				return "WLAN";
			} else {
				if (isUNIWAPNetwork(ni)) {
					return "UNWAP";
				} else if (isUNINETNetwork(ni)) {
					return "UNNET";
				} else {
					IKALog.i(TAG, "getActiveAPNName apnName = " + ni.getExtraInfo());
					return "OTHER";
				}
			}
		} else {// 移动卡
			if (ni.getType() == ConnectivityManager.TYPE_WIFI) {// WLAN
				return "WLAN";
			} else {
				if (isCMMMNetwork(ni)) {
					return "CMMM";
				} else if (isCMWAPNetwork(ni)) {
					return "CMWAP";
				} else if (isCMNETNetwork(ni)) {
					return "CMNET";
				} else {
					IKALog.i(TAG, "getActiveAPNName apnName = " + ni.getExtraInfo());
					return "OTHER";
				}
			}
		}
	}

	/**
	 * 判断当前网络是否需要CA认证
	 * 
	 * @param context
	 * @return
	 */
	public static boolean currentNetworkNeedCA(Context context) {
		return isChinaMobileNet(context) && !isCMMMNetwork(context) && !isCMWAPNetwork(context);
	}

	// /**
	// * 是否需要验证的授信网络，即中国移动卡+wifi/cmnet通道
	// *
	// * @return
	// */
	// public static boolean isWifiAuthor(Context context) {
	//
	// // return isWifiAuthor(context, getActiveNetworkInfo(context));
	// return isChinaMobileNet(context)&&isWLANNetwork(context,
	// getActiveNetworkInfo(context));
	// }

	// public static boolean isWifiAuthor(Context context, NetworkInfo ni) {
	// if (isEmulatornetwork(context, ni))// 给模拟器用的，模拟器走授信的登录流程
	// {
	// return false;
	// }
	// int netType = getCurrentNetworkType(context, ni);
	// if (netType == APN_TYPE_WIFI || netType == APN_TYPE_CMNET || netType ==
	// APN_TYPE_WIFI_UNAUTHOR
	// || netType == APN_TYPE_UNKNOW) {
	// return true;
	// } else {
	// return false;
	// }
	// }

	public static boolean isSimCardPresent(Context context) {// 检测SIM是否安装
		TelephonyManager t = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		String si = t.getSubscriberId();
		if (si == null) {
			return false;// 未安装
		} else {
			return true;
		}
	}

	/**
	 * 根据前后网络变化判断是否需要重新登录
	 * 
	 * @param context
	 * @param prevNetworkInfo
	 * @param curNetworkInfo
	 * @param curlogged
	 * @return true:需要重新登录，false不需要重新登
	 */
	public static boolean needRelogin(Context context, NetworkInfo prevNetworkInfo, NetworkInfo curNetworkInfo, boolean curlogged) {
		// Michael Liu:2011.10.17 仅限于CMWAP与WLAN之间切换需要重新登录
		// Michael Liu:测试发现，网络切换不需要重新登录，只需要更新id-token即可.
		/*
		 * if (NetworkManager.isCMWAPNetwork(context, curNetworkInfo) && NetworkManager.isWLANNetwork(context,prevNetworkInfo)){ IKALog.w(TAG, "need Relogin: prev network is WLAN,current network is cmwap"); return true; }else if (NetworkManager.isWLANNetwork(context, curNetworkInfo) &&
		 * NetworkManager.isCMWAPNetwork(context,prevNetworkInfo)){ IKALog.w(TAG, "need Relogin: prev network is cmwap,current network is WLAN"); return true; }else
		 */if (curNetworkInfo != null && curNetworkInfo.isConnected() && !curlogged) {
			IKALog.w(TAG, "need Relogin: current is not logged and connection is connected");
			return true; // Michael Liu:2011.10.17 没登录过时也需要登录
		} else {
			IKALog.w(TAG, "needn't Relogin: ");
			return false;
		}
		// boolean prevcmwaporwlan = false;
		// boolean curcmwaporwlan = false;
		// if (prevNetworkInfo != null){
		// prevcmwaporwlan = NetworkManager.isCMWAPNetwork(context,
		// prevNetworkInfo)||
		// NetworkManager.isWLANNetwork(context, prevNetworkInfo);
		// }
		// if (curNetworkInfo != null){
		// curcmwaporwlan = NetworkManager.isCMWAPNetwork(context,
		// curNetworkInfo)||
		// NetworkManager.isWLANNetwork(context, curNetworkInfo);
		// }
		// if ((prevcmwaporwlan && curcmwaporwlan) || !curlogged)
		// return true;
		// else
		// return (curNetworkInfo != null && curNetworkInfo.isConnected());
	}

	public static boolean needUpdateIdToken(Context context, NetworkInfo curNetworkInfo, boolean curlogged, String idtoken) {
		if (idtoken == null)
			idtoken = "";
		else
			idtoken = idtoken.trim();
		IKALog.i(TAG, "needUpdateIdToken iscmmmnetwork=" + NetworkUtil.isCMMMNetwork(context, curNetworkInfo) + ",iscmwap=" + NetworkUtil.isCMWAPNetwork(context, curNetworkInfo) + ",islogged=" + curlogged + ",idtoken=" + idtoken);
		if (!NetworkUtil.isCMMMNetwork(context, curNetworkInfo) && !NetworkUtil.isCMWAPNetwork(context, curNetworkInfo) && curlogged && idtoken.length() == 0) {
			return true;
		} else { // CMMM和CMWAP下不需要id-token
			return false;
		}
	}

	public enum State {
		UNKNOWN,

		/** This state is returned if there is connectivity to any network **/
		CONNECTED,
		/**
		 * This state is returned if there is no connectivity to any network. This is set to true under two circumstances:
		 * <ul>
		 * <li>When connectivity is lost to one network, and there is no other available network to attempt to switch to.</li>
		 * <li>When connectivity is lost to one network, and the attempt to switch to another network fails.</li>
		 */
		NOT_CONNECTED
	}

	/** 获取手机IMEI号 */
	public static String getIMEI(Context context) {
		TelephonyManager telmanager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		String imei = telmanager.getDeviceId();
		imei = (imei == null ? NO_DEVICE_IMEI : imei);
		IKALog.w(TAG, "imei:" + imei);
		return imei;
	}

	/**
	 * 获取MAC地址
	 * 
	 * @param context
	 * @return
	 */
	public static String getMACaddress(Context context) {
		String macAddress = null;
		WifiManager wifiMgr = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		//WifiInfo info = (null == wifiMgr ? null : wifiMgr.getConnectionInfo());
		WifiInfo info=null;
		if (null != info) {
			macAddress = info.getMacAddress();
			// ip = int2ip(info.getIpAddress());
		}
		if (null != macAddress) {
			macAddress = macAddress.replace(":", "");// mac地址把冒号符号去掉
		}
		IKALog.w(TAG, "mac:" + macAddress);
		return macAddress;
	}

	/**
	 * 
	 * <p>
	 * Title: getRandomIMEIorIMSI
	 * </p>
	 * <p>
	 * Description:生成15位随机数
	 * </p>
	 *
	 * @return
	 */
	public static String getRandomIMEIorIMSI() {

		long time = System.currentTimeMillis();
		StringBuilder sb = new StringBuilder();

		Random r = new Random(time);

		for (int i = 0; i < 10; i++) {
			long t = r.nextInt(10);
			sb.append(t);
		}
		String suffix = String.valueOf(time).substring(8);
		sb.append(suffix);

		return sb.toString();
	}

	public static String getUUID() {
		UUID uuid = UUID.randomUUID();

		return uuid.toString();
	}
	
    /**
     * 取得IMEI 国际移动装备辨识码
     */
    public static String getLocalIMEI (Context context){
        String imei = ((TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
        
        if (imei == null ||imei.trim().equals("")) imei = getRandomIMEIorIMSI() ;
        
        return imei ;
        
    }
    
    /**
     * 取得 IMSI  国际移动用户识别码
     */
    public static String getLocalIMSI (Context context){
        String imsi = ((TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE)).getSubscriberId ();
        if (imsi == null ||imsi.trim().equals("")) imsi = getRandomIMEIorIMSI() ;
        return imsi ;
    }
    
    /**
     * 获得客户端唯一ID
     * @param mContext
     * @return
     */
    public static String getAppId(Context mContext) {  
        String data="";
        //mac
        String macad=getMACaddress(mContext);
        if(macad!=null){
          macad=macad.replace(":","");
        }
        String imei=getIMEI(mContext);
        if(imei==null&&imei==null){
          data="AABBCCDDEE_00000000000";
        }else{
          data=macad+"_"+imei;
        }
        return data;
      }
    
    /**
     * 判断是否是3G,wifi等... 直接判断是否是慢速网络/快速网络的快捷方法
     * @param type
     * @param subType
     * @return
     */
    public static boolean isConnectionFast(int type,int subType){
        try{
//        ConnectivityManager connec =  (ConnectivityManager)acti.getSystemService(Context.CONNECTIVITY_SERVICE);   
//        NetworkInfo info = connec.getActiveNetworkInfo();  
//        connec.getAllNetworkInfo();
//        int type=info.getType();
//        int subType=info.getSubtype();
        
        if(type==ConnectivityManager.TYPE_WIFI){  
            return true;  
        }else if(type==ConnectivityManager.TYPE_MOBILE){  
          
          if(subType==TelephonyManager.NETWORK_TYPE_GPRS){
            return false;
          }else if(subType==TelephonyManager.NETWORK_TYPE_EDGE){
            return false;
          }else{
            return true;
            
          }
        }else{  
            return true;  
        }
        }catch(Exception ex){
          ex.printStackTrace();
        }
        return true;
    }  
    
}
