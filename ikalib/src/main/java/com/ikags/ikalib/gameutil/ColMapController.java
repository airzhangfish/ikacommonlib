package com.ikags.ikalib.gameutil;

import java.util.Vector;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;

import com.ikags.ikalib.util.IKALog;
import com.ikags.ikalib.util.StringUtil;
public class ColMapController {
	public static final String TAG = "ColMapController";
	public int[][] mapcollist = null;
	public float mStartX = 0;
	public float mStartY = 0;
	public Bitmap mDisplaymap = null;
	public static boolean debugMode=false;

	public void loadCharMapFile(Context context, String bigmappath, String txtpath) {
		try {
			IKALog.v(TAG, "loadColMapFile_bigmappath=" + bigmappath+ ",txtpath=" + txtpath);

			// 图片读取数据
			mDisplaymap = BitmapFactory.decodeStream(context.getAssets().open(bigmappath));
			// txt读取数据
			Vector vec = StringUtil.getInputstreamVector(context.getAssets().open(txtpath), "UTF-8");
			if(vec.size()>0){
				mapcollist=new int[vec.size()][4];
			}
			IKALog.v(TAG, "loadColMapFile_colsize=" + vec.size() + ",mapsize=" + mDisplaymap.getWidth() + "," + mDisplaymap.getHeight());

			if(mapcollist!=null){
				for(int i=0;i<mapcollist.length;i++){
					String data=(String) vec.elementAt(i);
					IKALog.v(TAG, i+"data="+data);
					String[] strs=data.split(",");
					mapcollist[i][0]=StringUtil.getStringToInt(strs[0]);
					mapcollist[i][1]=StringUtil.getStringToInt(strs[1]);
					mapcollist[i][2]=StringUtil.getStringToInt(strs[2]);
					mapcollist[i][3]=StringUtil.getStringToInt(strs[3]);
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	Rect testRect = new Rect();

	public void drawMaps(Canvas canvas, Paint paint, float startX, float startY) {
		mStartX = startX;
		mStartY = startY;


		if (mDisplaymap != null) {
			canvas.drawBitmap(mDisplaymap, mStartX, mStartY, paint);
		}

		//测试用绘制测试方块
		if(debugMode==true){
			paint.setStyle(Style.FILL);
			if (mapcollist != null) {
				for(int i=0;i<mapcollist.length;i++){
					paint.setColor(0x99ffff00);
					testRect.left = (int) (mStartX + mapcollist[i][0]);
					testRect.right = (int) ( testRect.left + mapcollist[i][2]);
					testRect.top = (int) (mStartY + mapcollist[i][1]);
					testRect.bottom = (int)  (testRect.top + mapcollist[i][3]);
					canvas.drawRect(testRect, paint);
					paint.setColor(0xffff0000);
					canvas.drawText("" + i, testRect.left + (mapcollist[i][2]/2), testRect.top + (mapcollist[i][3]/2), paint);
				}
			}
		}


	}


	/**
	 * 判断是否碰撞,用于判断平台跳跃类的,
	 * @param mX
	 * @param mY
	 * @return
	 */
	public float checkCol(float mX, float mY) {
		if (mapcollist != null) {
			for (int i = 0; i < mapcollist.length; i++) {
				// 判断坐标
				int left = (int) (mStartX + mapcollist[i][0]);
				int right = (int) (left + mapcollist[i][2]);
				if (mX >= left && mX <= right) {// 左右范围
					int top = (int) (mStartY + mapcollist[i][1]);
					int bottom = (int) (top + mapcollist[i][3]);
					if (mY >= top && mY <= bottom) {// 左右范围
						// 返回高度,用于重新设定
						return top;
					}
				}
			}
		}
		return -1;
	}

}
