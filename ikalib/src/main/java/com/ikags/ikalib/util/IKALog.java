package com.ikags.ikalib.util;
import java.io.File;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.Date;
import android.os.Environment;
import android.view.View;
import android.view.ViewGroup;

/**
 * 日志打印类
 * 
 * @author airzhangfish
 */
public class IKALog {

	public final static String TAG = "IKALog";

	public static boolean isPrintLog = true;
	public static boolean isWriteToFile = false;
	private static String isPrintLogfilepath = "/sdcard/IKAlog.print.txt";
	private static String isSaveLogfilepath = "/sdcard/IKAlog.save.txt";
	
	private static boolean isFirst = true;
	private final static String LOG_FILEPATH = "/sdcard/ikalog/";
	private final static String LOG_FILENAME = "ikalog";
	private final static String LOG_FILEEXT = ".txt";
	private static File mLogFile;
	private final static long LOGFILE_LIMIT = 1000000L;
	private final static SimpleDateFormat DATEFORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private final static SimpleDateFormat DATEFORMAT1 = new SimpleDateFormat("yyyyMMddHHmmss");

	private static void checkLog() {
		if (isFirst == true) {
			File file1 = new File(isPrintLogfilepath);
			if (file1.exists()) {
				isPrintLog = true;
			}
			File file2 = new File(isSaveLogfilepath);
			if (file2.exists()) {
				isWriteToFile = true;
			}
			isFirst = false;
		}
		createLogFile();
	}

	public static void print(String msg) {
		checkLog();
		if (isPrintLog) {
			System.out.print(msg == null ? "" : msg);
		}
		writeLogFile("", "", msg);
	}

	private static void createLogFile() {
		if (isWriteToFile) {
			synchronized (LOG_FILENAME) {
				if (mLogFile == null) {
					try {
						if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
							return;
						}
						File logpath = new File(LOG_FILEPATH);
						if (!logpath.exists()) {
							logpath.mkdir();
						}
						mLogFile = new File(LOG_FILEPATH + LOG_FILENAME + LOG_FILEEXT);
						if (!mLogFile.exists()) {
							IKALog.d(TAG, "Create the file:" + LOG_FILENAME);
							mLogFile.createNewFile();
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					if (mLogFile.isFile()) {
						if (mLogFile.length() > LOGFILE_LIMIT) {
							StringBuffer sb = new StringBuffer("");
							sb.append(LOG_FILEPATH);
							sb.append(LOG_FILENAME);
							sb.append(DATEFORMAT1.format(new Date()));
							sb.append(LOG_FILEEXT);
							mLogFile.renameTo(new File(sb.toString()));
							sb = null;
							sb = new StringBuffer("");
							sb.append(LOG_FILEPATH);
							sb.append(LOG_FILENAME);
							sb.append(LOG_FILEEXT);
							mLogFile = new File(sb.toString());
							sb = null;
							if (!mLogFile.exists()) {
								IKALog.d(TAG, "Create the file:" + LOG_FILENAME + LOG_FILEEXT);
								try {
									mLogFile.createNewFile();
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						}
					}
				}
			}
		}
	}

	private static void writeLogFile(String level, String tag, String msg) {
		if (isWriteToFile) {
			synchronized (LOG_FILENAME) {
				if (mLogFile != null) {
					StringBuffer sb = new StringBuffer();
					sb.append(DATEFORMAT.format(new Date()));
					sb.append(": ");
					sb.append(level);
					sb.append(": ");
					sb.append(tag);
					sb.append(": ");
					sb.append(msg);
					sb.append("\n");
					RandomAccessFile raf = null;
					try {
						raf = new RandomAccessFile(mLogFile, "rw");
						raf.seek(mLogFile.length());
						raf.write(sb.toString().getBytes("UTF-8"));
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						sb = null;
						if (raf != null) {
							try {
								raf.close();
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
				}
			}
		}
	}

	public static void println(String msg) {
		checkLog();
		if (isPrintLog) {
			System.out.println(msg == null ? "" : msg);
		}
		writeLogFile("", "", msg);
	}

	public static void i(String tag, String msg) {
		checkLog();
		if (isPrintLog) {
			android.util.Log.i(tag, msg == null ? "" : msg);
		}
		writeLogFile("INFO", tag, msg);
	}

	public static void i(String tag, String msg, Throwable tr) {
		checkLog();
		if (isPrintLog) {
			android.util.Log.i(tag, msg == null ? "" : msg, tr);
		}
		writeLogFile("INFO", tag, msg);
	}

	public static void d(String tag, String msg) {
		checkLog();
		if (isPrintLog) {
			android.util.Log.d(tag, msg == null ? "" : msg);
		}
		writeLogFile("DEBUG", tag, msg);
	}

	public static void d(String tag, String msg, Throwable tr) {
		checkLog();
		if (isPrintLog) {
			android.util.Log.d(tag, msg == null ? "" : msg, tr);
		}
		writeLogFile("DEBUG", tag, msg);
	}

	public static void e(String tag, String msg) {
		checkLog();
		if (isPrintLog) {
			android.util.Log.e(tag, msg == null ? "" : msg);
		}
		writeLogFile("ERROR", tag, msg);
	}

	public static void e(String tag, String msg, Throwable tr) {
		checkLog();
		if (isPrintLog) {
			android.util.Log.e(tag, msg == null ? "" : msg, tr);
		}
		writeLogFile("ERROR", tag, msg);
	}

	public static void v(String tag, String msg) {
		checkLog();
		if (isPrintLog) {
			android.util.Log.v(tag, msg == null ? "" : msg);
		}
		writeLogFile("VERBOSE", tag, msg);
	}

	public static void v(String tag, String msg, Throwable tr) {
		checkLog();
		if (isPrintLog) {
			android.util.Log.v(tag, msg == null ? "" : msg, tr);
		}
		writeLogFile("VERBOSE", tag, msg);
	}

	public static void w(String tag, String msg) {
		checkLog();
		if (isPrintLog) {
			android.util.Log.w(tag, msg == null ? "" : msg);
		}
		writeLogFile("WARN", tag, msg);
	}

	public static void w(String tag, String msg, Throwable tr) {
		checkLog();
		if (isPrintLog) {
			android.util.Log.w(tag, msg == null ? "" : msg, tr);
		}
		writeLogFile("WARN", tag, msg);
	}

	public static void vLongLog(String TAG, String str) {
		if (isPrintLog) {
			if (str == null) {
				return;
			}
			try {
				int presize = 3 * 1024;
				for (int i = 0; i < (str.length() / presize) + 1; i++) {
					int startpos = presize * i;
					int endpos = Math.min(startpos + presize, str.length());
					v(TAG, str.substring(startpos, endpos));
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}
	
	

	  public static void logViewTree(String TAG,View view,int space){
	    String data="";
	    for(int j=0;j<space;j++){
	      data=data+"--";
	    }
	    if(view instanceof ViewGroup){
	      ViewGroup vg=(ViewGroup)view;
	      v(TAG, data+vg.getClass().getSimpleName());
	      for(int i=0;i<vg.getChildCount();i++){
	        logViewTree(TAG,vg.getChildAt(i),space+1);
	      }
	    }else if(view!=null){
	      v(TAG, data+view.getClass().getSimpleName());
	    }else{
	      v(TAG,"view is null");
	    }
	    
	  }
	  

}
