package com.ikags.ikalib.util.loader;

import com.ikags.ikalib.util.IKALog;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;
/**
 * 图片view解析器,可以直接读取网络图片先是到View上
 * @author zhangxiasheng
 *
 */
public class ImageViewBaseParser extends BitmapBaseParser {

	public static final String TAG = "ImageViewParser";
	public ImageView iview = null;

	public  ImageViewBaseParser(ImageView imageview){
		iview=imageview;
	}
	
	
	public Handler mImageViewhander = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			setImageViewBitmap(msg);
		}
	};

	public void setImageViewBitmap(Message msg) {
		if(iview!=null){
			 iview.setImageBitmap((Bitmap)msg.obj);
			 iview.postInvalidate();	
		}
	}

	private void updateView(Bitmap bitmap) {
		Message msg = new Message();
		msg.obj = bitmap;
		mImageViewhander.sendMessage(msg);
	}

	public void onBitmapLoad(String url, Bitmap bitmap, String tag, boolean isNetData){
		if(bitmap!=null&&iview!=null){
			String urltag=(String)iview.getTag();
			if(!url.equals(urltag)){
				updateView(bitmap);	
				iview.setTag(url);
				IKALog.v(TAG, "updateView work_ok="+url);
			}else{
				IKALog.v(TAG, "updateView allready"+url);	
			}
		}
		
	}
	
}
