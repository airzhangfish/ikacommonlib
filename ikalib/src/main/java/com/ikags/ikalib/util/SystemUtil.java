package com.ikags.ikalib.util;
import java.util.List;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;


/**
 * 一些检测用的系统工具
 * @author zhangxiasheng
 *
 */
public class SystemUtil {

    /**
     * 用来判断服务是否运行.
     * @param context
     * @param className 判断的服务名字：包名+类名
     * @return true 在运行, false 不在运行
     */
      
    public static boolean isServiceRunning(Context context,String className) {        
        boolean isRunning = false;
        ActivityManager activityManager = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE); 
        List<ActivityManager.RunningServiceInfo> serviceList= activityManager.getRunningServices(Integer.MAX_VALUE);
        if (!(serviceList.size()>0)) {
            return false;
        }
        for (int i=0; i<serviceList.size(); i++) {
            if (serviceList.get(i).service.getClassName().equals(className) == true) {
                isRunning = true;
                break;
            }
        }
        return isRunning;
    }



     public static int getAppVersionCode(Context con) {
             try {
                     PackageManager manager = con.getPackageManager();
                     PackageInfo info = manager.getPackageInfo(con.getPackageName(), 0);
                     return info.versionCode;
               } catch (Exception e) {
                     e.printStackTrace();
                     return 0;
                 }
         }
	
	
}
