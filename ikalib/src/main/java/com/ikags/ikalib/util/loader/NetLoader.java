package com.ikags.ikalib.util.loader;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.ikags.ikalib.util.IKALog;
import com.ikags.ikalib.util.StringUtil;
import com.ikags.ikalib.util.cache.CacheInfo;
import com.ikags.ikalib.util.cache.CachedUrlManager;

/**
 * 处理网络连接以及相关的解析
 *
 * @author zhangxiasheng
 */
public class NetLoader {

    public final static String TAG = "NetLoader";
    private static NetLoader mSingleInstance = null;
    public Context mContext;
    public List<UrlLoadThread> mThreadList = null;
    private static int maxRetryTime = 3;
    private List<HttpCookie> listcookie = null;

    public NetLoader(Context contex) {
        mContext = contex.getApplicationContext();
        mThreadList = new ArrayList<UrlLoadThread>();
        listcookie = new ArrayList<HttpCookie>();
        CachedUrlManager.getDefault(contex);
    }

    public static NetLoader getDefault(Context context) {
        if (mSingleInstance == null) {
            mSingleInstance = new NetLoader(context);
        }
        return mSingleInstance;
    }

    /**
     * 主入口，读取数据,postdata只能是key,value的方式
     *
     * @param url
     * @param postdata
     * @param mkHttpHead
     * @param parser
     */
    public void loadUrl(String url, String postdata, IMakeHttpHead mkHttpHead, IBaseParser parser, String itemtag, boolean readCache) {
        try {
            if (isOnReady(url, null, postdata)) {
                return;
            }
            url = trimURL(url);
            UrlLoadThread thread = new UrlLoadThread();
            thread.mItem = new UrlLoadItem(url, postdata, mkHttpHead, parser, itemtag, readCache);
            mThreadList.add(thread);
            thread.start();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    private boolean isOnReady(String url, byte[] hpEntity, String postdata) {
        try {
            if (hpEntity != null || postdata != null) {
                return false;
            }
            // 如果队列中有的话，可以暂时不加入队列
            for (int i = 0; i < mThreadList.size(); i++) {
                UrlLoadThread ult = mThreadList.get(i);
                if (ult.mItem.mUrl.equals(url) && ult.mItem.mEntity == null && ult.mItem.mPostData == null) {
                    return true;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    /**
     * 合法化url地址
     *
     * @param url
     * @return
     */
    public static String trimURL(String url) {
        if (url != null) {
            url = url.trim();
        } else {
            url = "";
        }
        return url;
    }

    /**
     * 取消现在所有的网络请求线程
     */
    public void cancelAll() {
        try {
            for (int i = 0; i < mThreadList.size(); i++) {
                Thread thread = mThreadList.get(i);
                if (thread != null) {
                    thread.interrupt();
                }
            }
            mThreadList.clear();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public class UrlLoadItem {

        public String itemtag;
        public String mUrl;
        public String mPostData;
        public byte[] mEntity;
        IMakeHttpHead mHttpHead;
        IBaseParser mParser;
        public boolean mReadCache;

        public UrlLoadItem(String url, String postdata, IMakeHttpHead httpHead, IBaseParser parser, String tag, boolean readCache) {
            mUrl = url;
            mPostData = postdata;
            mEntity = null;
            mHttpHead = httpHead;
            mParser = parser;
            itemtag = tag;
            mReadCache = readCache;
        }

        public UrlLoadItem(String url, byte[] hpEntity, IMakeHttpHead httpHead, IBaseParser parser, String tag, boolean readCache) {
            mUrl = url;
            mPostData = null;
            mEntity = hpEntity;
            mHttpHead = httpHead;
            mParser = parser;
            itemtag = tag;
            mReadCache = readCache;
        }

        public void reset() {
            mUrl = null;
            mPostData = null;
            mEntity = null;
            mHttpHead = null;
            mParser = null;
            itemtag = null;
            mReadCache = true;
        }
    }

    class UrlLoadThread extends Thread {

        UrlLoadItem mItem = null;

        public void run() {
            try {
                try {
                    if (mItem != null) {
                        CacheInfo ci = null;
                        if (mItem.mReadCache == true) {
                            ci = loadCacheData();
                        }
                        if (ci == null) {
                            IKALog.v(TAG, mItem.mUrl + ",no cache,getonline");
                            getNetData();
                        } else {
                            boolean isreadcache = doParseCache(ci);
                            IKALog.v(TAG, mItem.mUrl + ",read cache=" + isreadcache);
                            if (isreadcache == false) {
                                getNetData();
                            }
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                mThreadList.remove(this);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        //新增方法,用于单线程本地读取数据
        public InputStream getMainNetDateInputStream() {
            InputStream is = null;
            try {
                if (mItem != null) {
                    CacheInfo ci = null;
                    if (mItem.mReadCache == true) {
                        ci = loadCacheData();
                    }
                    if (ci != null) {
                        try {
                            File file = new File(ci.mFileName);
                            if (file.exists()) {
                                is = new FileInputStream(file);
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }


                    if (is == null) {
                        addCookies();
                        HttpURLConnection response = downloadFromNet();
                        getCookiesAndSet();
                        try {
                            is = response.getInputStream();
                            String headEncodingvalue=response.getHeaderField("Content-Encoding");
                            if (headEncodingvalue != null && headEncodingvalue.indexOf("gzip") >= 0) {
                                is = new GZIPInputStream(is);
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }
                mThreadList.remove(this);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return is;
        }

        public String getMainNetDataString(String encode) {
            String data = null;
            try {
                InputStream is = getMainNetDateInputStream();
                if (encode == null) {
                    data = StringUtil.getInputStreamText(is, "UTF-8");
                } else {
                    data = StringUtil.getInputStreamText(is, encode);
                }
                if (data != null) {
                    data = data.trim();
                }
                if (is != null) {
                    is.close();
                    is = null;
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return data;
        }

        public Bitmap getMainNetDataBitmap() {
            Bitmap bitmap = null;
            try {
                InputStream is = getMainNetDateInputStream();
                bitmap = BitmapFactory.decodeStream(is);
                if (is != null) {
                    is.close();
                    is = null;
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return bitmap;
        }

        public byte[] getMainNetDataBytes() {
            byte[] bytes = null;
            try {
                InputStream is = getMainNetDateInputStream();
                bytes = StringUtil.getInputStreamBytes(is);
                if (is != null) {
                    is.close();
                    is = null;
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return bytes;
        }


        public void getNetData() {
            addCookies();
            HttpURLConnection response = downloadFromNet();
            getCookiesAndSet();
            doParse(response);
        }

        public void addCookies() {
//            if (listcookie != null && listcookie.size() > 0) {
//                for (int i = 0; i < listcookie.size(); i++) {
//                    mHttpClient.getCookieStore().addCookie(listcookie.get(i));
//                }
//            }
        }

        public void getCookiesAndSet() {
//            List<Cookie> tmplist = mHttpClient.getCookieStore().getCookies();
//            if (tmplist.size() > listcookie.size()) {
//                listcookie = tmplist;
//            }
        }

        /**
         * 从缓存中查找数据,查找到为true,查找不到为false
         *
         * @return
         */
        private CacheInfo loadCacheData() {
            // 数据库能够查到
            CacheInfo ci = null;
            if (mItem.mPostData == null && mItem.mEntity == null) {
                ci = CachedUrlManager.getDefault(mContext).findUrl(mItem.mUrl);
            }
            return ci;
        }

        /**
         * 从网络下载
         *
         * @return
         */
        private HttpURLConnection downloadFromNet() {
            HttpURLConnection response = null;
            try {
                response = doHttpConnect();
                if (response != null) {
                    IKALog.v(TAG, "response code=" + response.getResponseCode());
                } else {
                    IKALog.v(TAG, "response is null");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return response;
        }

        /**
         * 下载完毕解析
         *
         * @return
         */
        private boolean doParse(HttpURLConnection response) {
            if (mItem.mParser != null) {
                InputStream is = null;
                try {
                    is = response.getInputStream();
                    String headEncodingvalue=response.getHeaderField("Content-Encoding");
                    if (headEncodingvalue != null && headEncodingvalue.indexOf("gzip") >= 0) {
                        is = new GZIPInputStream(is);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                mItem.mParser.doParse(mItem.mUrl, response, is, mItem.mPostData, mItem.itemtag);
                if (is != null) {
                    try {
                        is.close();
                        is = null;
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
            return true;
        }

        private boolean doParseCache(CacheInfo ci) {
            if (mItem.mParser != null) {
                InputStream is = null;
                try {
                    File file = new File(ci.mFileName);
                    if (file.exists()) {
                        is = new FileInputStream(file);
                    } else {
                        return false;
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    return false;
                }
                mItem.mParser.doParse(mItem.mUrl, null, is, mItem.mPostData, mItem.itemtag);
                if (is != null) {
                    try {
                        is.close();
                        is = null;
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
                return true;
            }
            return false;
        }

        private HttpURLConnection doHttpConnect() {
            HttpURLConnection connection = null;
            try {
                URL url = new URL(mItem.mUrl);
                //TODO 完整流程
                connection = (HttpURLConnection) url.openConnection();
                connection.setConnectTimeout(30 * 1000); //30秒
                //URL url = new URL("http://apia.lohasor.com/shuer/utils/httpheadtest.php");
//				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                connection.setDoOutput(true);
                connection.setUseCaches(false);
                connection.setRequestProperty("Accept-Encoding", "gzip, deflate");
                connection.setRequestProperty("Connection", "Keep-Alive");
                connection.setRequestProperty("Content-Type", "text/xml; charset=UTF-8");
//				connection.getOutputStream().write("HEHE".getBytes());
//				connection.getOutputStream().flush();
//				connection.getOutputStream().close();
//				connection.connect();
//				int code = connection.getResponseCode();
//                InputStream is = connection.getInputStream();
//                String result = getInputStreamText(is, "UTF8");
//				connection.disconnect();

                if (mItem.mPostData != null || mItem.mEntity != null) {
                    IKALog.v(TAG, "doHttpConnect_POST=" + mItem.mUrl + ",postdata=" + mItem.mPostData + "," + mItem.mEntity);
                    connection.setRequestMethod("POST");
                    if (mItem.mPostData != null) {
                        connection.getOutputStream().write(mItem.mPostData.getBytes());
                        connection.getOutputStream().flush();
                        connection.getOutputStream().close();
                    } else if (mItem.mEntity != null) {
                        connection.getOutputStream().write(mItem.mEntity);
                        connection.getOutputStream().flush();
                        connection.getOutputStream().close();
                    }
                } else {
                    IKALog.v(TAG, "doHttpConnect_GET=" + mItem.mUrl);
                    connection.setRequestMethod("GET");

                }
                if (mItem.mHttpHead != null) {
                    mItem.mHttpHead.makeHttpHead(connection, true);
                }
                connection.connect();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return connection;
        }
    };


}
