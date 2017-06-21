package com.ikags.ikalib.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
/**
 * 专门处理SharedPreferences的方法.
 * @author zhangxiasheng
 *
 */
public class SharedPreferencesManager
{

  Context context;
  private static SharedPreferencesManager instance = null;

  public static SharedPreferencesManager getInstance(Context mcontext)
  {
    if (instance == null)
    {
      instance = new SharedPreferencesManager(mcontext);
    }
    return instance;
  }

  public SharedPreferencesManager(Context mcontext)
  {
    this.context = mcontext.getApplicationContext();
  }

  public void saveData(String filename, String key, int value)
  {
    SharedPreferences sp = context.getSharedPreferences(filename, Activity.MODE_PRIVATE);
    SharedPreferences.Editor editor = sp.edit();
    editor.putInt(key, value);
    editor.commit();
  }

  public void saveData(String filename, String key, String value)
  {
    SharedPreferences sp = context.getSharedPreferences(filename, Activity.MODE_PRIVATE);
    SharedPreferences.Editor editor = sp.edit();
    editor.putString(key, value);
    editor.commit();
  }

  public void saveData(String filename, String key, boolean value)
  {
    SharedPreferences sp = context.getSharedPreferences(filename, Activity.MODE_PRIVATE);
    SharedPreferences.Editor editor = sp.edit();
    editor.putBoolean(key, value);
    editor.commit();
  }

  public void saveData(String filename, String key, float value)
  {
    SharedPreferences sp = context.getSharedPreferences(filename, Activity.MODE_PRIVATE);
    SharedPreferences.Editor editor = sp.edit();
    editor.putFloat(key, value);
    editor.commit();
  }

  public void saveData(String filename, String key, long value)
  {
    SharedPreferences sp = context.getSharedPreferences(filename, Activity.MODE_PRIVATE);
    SharedPreferences.Editor editor = sp.edit();
    editor.putLong(key, value);
    editor.commit();
  }

  public int readData(String filename, String key, int defValue)
  {
    SharedPreferences mySp = context.getSharedPreferences(filename, Activity.MODE_PRIVATE);
    return mySp.getInt(key, defValue);
  }

  public float readData(String filename, String key, float defValue)
  {
    SharedPreferences mySp = context.getSharedPreferences(filename, Activity.MODE_PRIVATE);
    return mySp.getFloat(key, defValue);
  }

  public String readData(String filename, String key, String defValue)
  {
    SharedPreferences mySp = context.getSharedPreferences(filename, Activity.MODE_PRIVATE);
    return mySp.getString(key, defValue);
  }

  public long readData(String filename, String key, long defValue)
  {
    SharedPreferences mySp = context.getSharedPreferences(filename, Activity.MODE_PRIVATE);
    return mySp.getLong(key, defValue);
  }

  public boolean readData(String filename, String key, boolean defValue)
  {
    SharedPreferences mySp = context.getSharedPreferences(filename, Activity.MODE_PRIVATE);
    return mySp.getBoolean(key, defValue);
  }

  public void removeItem(String filename, String[] key)
  {
    SharedPreferences sp = context.getSharedPreferences(filename, Activity.MODE_PRIVATE);
    SharedPreferences.Editor editor = sp.edit();
    int num = key.length;
    for (int i = 0; i < num; i++)
    {
      editor.remove(key[i]);
      editor.commit();
    }
  }

  public void clearData(String filename)
  {
    SharedPreferences sp = context.getSharedPreferences(filename, Activity.MODE_PRIVATE);
    SharedPreferences.Editor editor = sp.edit();
    editor.clear().commit();
  }
}
