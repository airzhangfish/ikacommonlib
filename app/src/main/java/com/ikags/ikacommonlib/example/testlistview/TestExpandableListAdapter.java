package com.ikags.ikacommonlib.example.testlistview;

import java.util.Vector;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

public class TestExpandableListAdapter extends BaseExpandableListAdapter{
	Context mContext=null;
	Vector<Vector<String>> mTestData=null;
	public TestExpandableListAdapter(Context con,Vector<Vector<String>> testData){
		mContext=con;
		mTestData=testData;
	}
	
	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return null;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return 0;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
		Vector<String> vecstr=mTestData.elementAt(groupPosition);
		String data=vecstr.elementAt(childPosition);
		TextView tv=new TextView(mContext);
		tv.setTextSize(30);
		tv.setText("["+childPosition+"]child="+data);
		return tv;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		Vector<String> vecstr=mTestData.elementAt(groupPosition);
		return vecstr.size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return null;
	}

	@Override
	public int getGroupCount() {
		return mTestData.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return 0;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
		Vector<String> vecstr=mTestData.elementAt(groupPosition);
		TextView tv=new TextView(mContext);
		tv.setText("group="+groupPosition);
		tv.setTextSize(40);
		return tv;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return false;
	}


}
