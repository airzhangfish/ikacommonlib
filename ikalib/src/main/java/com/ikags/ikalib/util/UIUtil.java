package com.ikags.ikalib.util;
import android.app.Activity;
import android.content.Context;
import android.provider.Settings;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;
/**
 * 处理UI和显示的相关功能
 * @author zhangxiasheng
 *
 */
public class UIUtil {

	public static int dip2px(Context context, float dipValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		int re = (int) (dipValue * scale + 0.5f);
		IKALog.v("dip2px", "" + re);
		return re;
	}

	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		int re = (int) (pxValue / scale + 0.5f);
		IKALog.v("px2dip", "" + re);
		return re;
	}
/**
 * 设置屏幕亮度
 * @param macti
 * @param brightness
 */
	public static void setBrightness(Activity macti, int brightness) {

		try {
			int brightnessMode = Settings.System.getInt(macti.getContentResolver(), "screen_brightness_mode");
			if (brightnessMode == 1) {
				Settings.System.putInt(macti.getContentResolver(), "screen_brightness_mode", 0);
			}

			if (0 <= brightness && brightness <= 255) {
				WindowManager.LayoutParams layoutParams = macti.getWindow().getAttributes();
				layoutParams.screenBrightness = (float) brightness / 255;
				macti.getWindow().setAttributes(layoutParams);
				Settings.System.putInt(macti.getContentResolver(), "screen_brightness_mode", brightness);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * ListView高度设死的设置.重新计算所有的高度
	 * @param listView
	 */
	public static void setListViewHeightBasedOnChildren(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			return;
		}
		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}
		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		((MarginLayoutParams) params).setMargins(10, 10, 10, 10);
		listView.setLayoutParams(params);
	}
/**
 * GridView高度写死的设置.重新计算所有的高度
 * @param gridView
 * @param columns
 */
	public static void setGridViewHeightBasedOnChildren(GridView gridView, int columns) {
		try {
			ListAdapter listAdapter = gridView.getAdapter();
			if (listAdapter == null) {
				return;
			}
			int totalHeight = 0;
			int totalcount = 0;
			if (listAdapter.getCount() % columns == 0) {
				totalcount = listAdapter.getCount() / columns;
			} else {
				totalcount = (listAdapter.getCount() / columns) + 1;
			}
			for (int i = 0; i < totalcount; i++) {
				int getindex = i * columns;
				View listItem = listAdapter.getView(getindex, null, gridView);
				listItem.measure(0, 0);
				totalHeight += listItem.getMeasuredHeight();
			}
			ViewGroup.LayoutParams params = gridView.getLayoutParams();
			params.height = totalHeight;// + 1 * (totalcount - 1); //gridView.getDividerHeight()
			((MarginLayoutParams) params).setMargins(0, 0, 0, 0);
			gridView.setLayoutParams(params);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	
	/**
	 * textview加粗加下划线
	 * @param title
	 * @return
	 */
	public static Spanned getLHTitle(String title){
		Spanned spd=null;
		try{
			spd =Html.fromHtml("<u><bold>"+title+"</bold></u>");
		}catch(Exception ex){
			ex.printStackTrace();
		}
		 return spd;
	}

}
