package com.ikags.ikacommonlib.example;

import java.util.List;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.ikags.ikacommonlib.R;

public class TestProviderListActivity extends Activity {
	/**
	 * Called when the activity is first created.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
	}


	//获取当前手机所有的provider的方法
	public void testall() {

		final PackageManager pm = this.getPackageManager();
		Thread mthread = new Thread() {

			public void run() {

				List<PackageInfo> pinfolist = pm.getInstalledPackages(PackageManager.GET_PROVIDERS);

				for (int i = 0; i < pinfolist.size(); i++) {
					PackageInfo pinfo = pinfolist.get(i);
					if (pinfo.providers != null) {
						for (int j = 0; j < pinfo.providers.length; j++) {
							Log.v("PROVIDERLIST", pinfo.packageName + "==" + pinfo.providers[j].authority);
							getDateInfo("content://" + pinfo.providers[j].authority);
						}

					}
				}
			}

		};
		mthread.start();

	}


	private String getDateInfo(String uristr) {
		String data = "";
		String idss = "10086";
		try {
			Uri uri = Uri.parse(uristr);
			Cursor cur = this.getContentResolver().query(uri, null, null, null, " _id LIMIT 100");
			if (cur != null) {
				int columnsize = cur.getColumnCount();
				int datasize = cur.getCount();
				for (int j = 0; j < datasize; j++) {
					cur.moveToPosition(j);
					for (int i = 0; i < columnsize; i++) {
						data = cur.getString(i);
						if (data != null) {
							int index = data.indexOf(idss);
							if (index >= 0) {
								Log.v("Datainfo", "uristr=" + uristr);
							}
						}
					}
				}
				cur.close();
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			data = data + ex.getMessage();
		}
		return data;
	}


}