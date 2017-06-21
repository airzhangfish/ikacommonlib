package com.ikags.ikalib.model;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;

/**
 * Trial Version通过android makret官方升级Full Version的模块<br>
 * 弹出对话框，显示信息，点击确认之后跳入am或者取消对话框。连接至google play的页面
 * @author zhangxiasheng
 *
 */
public class TrialModule
{

  private String fullversionpackagename=null;
  private String intromessage=null;
  Context mcontext=null;

  /**
   *
   * @param context
   * @param fullversionpackagename 完全版的包名
   * @param msg 完全版的介绍信息
   */
  public TrialModule(Context context,String fullversionpackagename,String msg){
    mcontext=context;
    this.fullversionpackagename=fullversionpackagename;
    intromessage=msg;

  };

  /**
   * 弹出提示升级的对话框
   */
  public void getFullVersionDialog(){
    try{
      AlertDialog.Builder builder = new AlertDialog.Builder(mcontext);
      builder.setTitle("THE FULL VERSION.");
      builder.setMessage(intromessage);
      builder.setPositiveButton("Get the full Version", new DialogInterface.OnClickListener()
      {
        public void onClick(DialogInterface dialog, int id)
        {

          dialog.cancel();
          getFullVersion();
        }
      });

      builder.setNegativeButton("No.Thanks", new DialogInterface.OnClickListener()
      {
        public void onClick(DialogInterface dialog, int id)
        {

          dialog.cancel();
        }
      });

      builder.setCancelable(true);
      AlertDialog alertSize = builder.create();
      alertSize.show();
    }catch(Exception ex){
      ex.printStackTrace();
    }
  }


  private void getFullVersion(){
    try{
      Uri uri = Uri.parse("market://search?q=pname:"+fullversionpackagename);
      Intent it = new Intent(Intent.ACTION_VIEW, uri);
      mcontext.startActivity(it);
    }catch(Exception ex){
      ex.printStackTrace();
    }
  }


}