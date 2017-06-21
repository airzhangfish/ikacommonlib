package com.ikags.ikalib.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.DecimalFormat;

import android.util.Log;
/**
 * 关于文件操作的工具类
 * 
 * @author airzhangfish
 * 
 */
public class FileUtil {

	public static final String TAG = "FileUtil";

	public static String getRandomName() {
		String longname = String.valueOf(System.currentTimeMillis());
		return longname;
	}

	public static void saveFile(String path, String name, String data) {
		try {
			File file = new File(path + name);
			if (file.exists()) {
				file.delete();
			}
			file.createNewFile();
			// 写文件
			FileOutputStream out;
			out = new FileOutputStream(file);
			out.write(data.getBytes("UTF-8"));
			out.close();
		} catch (Exception e) {
			IKALog.v(TAG, "path="+path+",name="+name+",data="+data);
			e.printStackTrace();
		}
	}

	public static void saveFile(String path, String name, InputStream in) {
		try {
			File file = new File(path + name);
			if (file.exists()) {
				file.delete();
			}
			file.createNewFile();
			// 写文件
			FileOutputStream out;
			out = new FileOutputStream(file);
			byte[] buffer = new byte[50 * 1024];
			try {
				for (int n; (n = in.read(buffer)) != -1;) {
					out.write(buffer, 0, n);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			in.close();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static boolean mkdirs(String mkdirName) {
		try {
			File dirFile = new File(mkdirName);
			boolean bret = false;
			if (!dirFile.exists()) {
				try {
					bret = dirFile.mkdirs();
					return bret;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return false;
		} catch (Exception err) {
			err.printStackTrace();
			return false;
		}
	}

	public static boolean isSdcardExsit() {
		boolean ret = false;
		String sDcString = android.os.Environment.getExternalStorageState();
		if (sDcString.equals(android.os.Environment.MEDIA_MOUNTED)) {
			ret = true;
		}
		return ret;

	}

	/**
	 * 删除文件夹(包含自己文件夹)
	 * 
	 * @param filePathAndName
	 *            String 文件夹路径及名称 如c:/fqf
	 * @param fileContent
	 *            String
	 * @return boolean
	 */
	public static void delFolder(String folderPath) {
		try {
			deleteDirs(folderPath); // 删除完里面所有内容
			File myFilePath = new File(folderPath);
			myFilePath.delete(); // 删除空文件夹

		} catch (Exception e) {
			e.printStackTrace();

		}
	}

	/**
	 * 删除文件夹里面的所有文件(不包括自己)
	 * 
	 * @param path
	 *            String 文件夹路径 如 c:/fqf
	 */
	public static void deleteDirs(String path) {
		File file = new File(path);
		if (!file.exists()) {
			return;
		}
		if (!file.isDirectory()) {
			return;
		}
		String[] tempList = file.list();
		File temp = null;
		for (int i = 0; i < tempList.length; i++) {
			if (path.endsWith(File.separator)) {
				temp = new File(path + tempList[i]);
			} else {
				temp = new File(path + File.separator + tempList[i]);
			}
			if (temp.isFile()) {
				temp.delete();
			}
			if (temp.isDirectory()) {
				deleteDirs(path + "/" + tempList[i]);// 先删除文件夹里面的文件
				delFolder(path + "/" + tempList[i]);// 再删除空文件夹
			}
		}
	}

	public static void deleteFile(String defaultPath, String filename) {
		int index = 0;
		index = filename.indexOf('/');
		File file = null;
		if (defaultPath == null) {
			defaultPath = "";
		}
		try {
			if (index == 0) {// lhy:2011.5.5 解决自定义路径的问题
				file = new File(filename);
			} else {
				file = new File(defaultPath + filename);
			}
			if (file.exists()) {
				file.delete();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void deleteFile(String filename) {
		try {
			File file = new File(filename);
			if (file.exists()) {
				file.delete();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 遍历整个文件夹,显示所有文件(日志显示)
	 * @param mTAG
	 * @param file
	 */
		public static void traveFiles(String mTAG,File file) {
			if (file.exists()) {
				if (file.isDirectory()) {
					File[] filelist = file.listFiles();
					IKALog.v(mTAG, "dirs=" + file.getPath());
					for (int i = 0; i < filelist.length; i++) {
						traveFiles(mTAG,filelist[i]);
					}
				} else {
					IKALog.v(mTAG, "file=" + file.getPath());
				}
			}
		}
		
		
		public static final int SIZETYPE_B = 1;// 获取文件大小单位为B的double值
		public static final int SIZETYPE_KB = 2;// 获取文件大小单位为KB的double值
		public static final int SIZETYPE_MB = 3;// 获取文件大小单位为MB的double值
		public static final int SIZETYPE_GB = 4;// 获取文件大小单位为GB的double值
		/**
		 * 
		 * 获取文件指定文件的指定单位的大小
		 * @param filePath
		 *            文件路径
		 * @param sizeType
		 *            获取大小的类型1为B、2为KB、3为MB、4为GB
		 * @return double值的大小
		 */

		public static double getFileOrFilesSize(String filePath, int sizeType) {
			File file = new File(filePath);
			long blockSize = 0;
			try {
				if (file.isDirectory()) {
					blockSize = getFileSizes(file);
				} else {
					blockSize = getFileSize(file);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return FormetFileSize(blockSize, sizeType);
		}

		/**
		 * 调用此方法自动计算指定文件或指定文件夹的大小
		 * @param filePath
		 *            文件路径
		 * @return 计算好的带B、KB、MB、GB的字符串
		 */
		public static String getAutoFileOrFilesSize(String filePath) {
			File file = new File(filePath);
			long blockSize = 0;
			try {
				if (file.isDirectory()) {
					blockSize = getFileSizes(file);
				} else {
					blockSize = getFileSize(file);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return FormetFileSize(blockSize);
		}

		/**
		 * 获取指定文件大小
		 * @param f
		 * @return
		 * @throws Exception
		 */

		private static long getFileSize(File file) throws Exception

		{

			long size = 0;

			if (file.exists()) {
				FileInputStream fis = null;
				fis = new FileInputStream(file);
				size = fis.available();
			}
			else {
				file.createNewFile();
				IKALog.e("获取文件大小", "文件不存在!");
			}
			return size;
		}
		/**
		 * 
		 * 获取指定文件夹
		 * @param f
		 * @return
		 * @throws Exception
		 */
		private static long getFileSizes(File f) throws Exception
		{
			long size = 0;
			File flist[] = f.listFiles();
			for (int i = 0; i < flist.length; i++) {
				if (flist[i].isDirectory()) {
					size = size + getFileSizes(flist[i]);
				}
				else {
					size = size + getFileSize(flist[i]);
				}
			}
			return size;
		}
		/**
		 * 转换文件大小
		 * @param fileS
		 * @return
		 */
		private static String FormetFileSize(long fileS)
		{
			DecimalFormat df = new DecimalFormat("#.00");
			String fileSizeString = "";
			String wrongSize = "0B";
			if (fileS == 0) {
				return wrongSize;
			}
			if (fileS < 1024) {
				fileSizeString = df.format((double) fileS) + "B";
			}
			else if (fileS < 1048576) {
				fileSizeString = df.format((double) fileS / 1024) + "KB";
			}
			else if (fileS < 1073741824) {
				fileSizeString = df.format((double) fileS / 1048576) + "MB";
			}
			else {
				fileSizeString = df.format((double) fileS / 1073741824) + "GB";
			}
			return fileSizeString;
		}
		/**
		 * 转换文件大小,指定转换的类型
		 * @param fileS
		 * @param sizeType
		 * @return
		 */
		private static double FormetFileSize(long fileS, int sizeType)
		{
			DecimalFormat df = new DecimalFormat("#.00");
			double fileSizeLong = 0;
			switch (sizeType) {
				case SIZETYPE_B :
					fileSizeLong = Double.valueOf(df.format((double) fileS));
					break;
				case SIZETYPE_KB :
					fileSizeLong = Double.valueOf(df.format((double) fileS / 1024));
					break;
				case SIZETYPE_MB :
					fileSizeLong = Double.valueOf(df.format((double) fileS / 1048576));
					break;
				case SIZETYPE_GB :
					fileSizeLong = Double.valueOf(df.format((double) fileS / 1073741824));
					break;
				default :
					break;
			}
			return fileSizeLong;
		}

}
