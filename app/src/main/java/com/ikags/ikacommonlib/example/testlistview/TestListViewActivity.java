package com.ikags.ikacommonlib.example.testlistview;

import java.util.Vector;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ExpandableListView;

import com.ikags.ikacommonlib.R;

public class TestListViewActivity extends Activity {
	

    ExpandableListView expandableListView1=null;
	Button button1=null;
	TestExpandableListAdapter elistAdaper=null;
	Vector<Vector<String>> testData=null;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acti_testlistview);
        expandableListView1=(ExpandableListView)this.findViewById(R.id.expandableListView1);
        button1=(Button)this.findViewById(R.id.button1);
        
      testData=new  Vector<Vector<String>>();
        for(int j=0;j<5;j++){
        	Vector<String> vecstr=new Vector<String>();
        for(int i=0;i<5;i++){
        	vecstr.add(j+"-"+i);
        }
        testData.add(vecstr);
        }
        elistAdaper=new TestExpandableListAdapter(this,testData);
        expandableListView1.setAdapter(elistAdaper);
        
        button1.setOnClickListener(ocl);
    }
    
    OnClickListener ocl=new OnClickListener(){

		@Override
		public void onClick(View v) {
			
	        for(int j=0;j<3;j++){
	        	Vector<String> vecstr=new Vector<String>();
	        for(int i=0;i<3;i++){
	        	vecstr.add(testData.size()+"-"+i);
	        }
	        testData.add(vecstr);
	        }
			
            if(elistAdaper!=null){
            	elistAdaper.notifyDataSetChanged();
            }
		}
    	
    };
}