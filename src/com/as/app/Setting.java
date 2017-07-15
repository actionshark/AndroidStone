package com.as.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class Setting {
	private static Setting sInstance;
	
	public static synchronized void setInstance(String name) {
		sInstance = new Setting(App.getContext(), name);
	}
	
	public static synchronized Setting getInstance() {
		return sInstance;
	}
	
	///////////////////////////////////////////////////////////
	
	private final SharedPreferences mPreferences;
	
	public Setting(Context context, String name) {
		mPreferences = context.getSharedPreferences(name, Context.MODE_PRIVATE);
	}
	
	public boolean contains(String key) {
		return mPreferences.contains(key);
	}
	
	///////////////////////////////////////////////////////////
	
	public void setBoolean(String key, boolean value) {
		Editor editor = mPreferences.edit();
		editor.putBoolean(key, value);
		editor.commit();
	}
	
	public boolean getBoolean(String key) {
		return getBoolean(key, false);
	}
	
	public boolean getBoolean(String key, boolean def) {
		return mPreferences.getBoolean(key, def);
	}
	
	///////////////////////////////////////////////////////////
	
	public void setInt(String key, int value) {
		Editor editor = mPreferences.edit();
		editor.putInt(key, value);
		editor.commit();
	}
	
	public int getInt(String key) {
		return getInt(key, 0);
	}
	
	public int getInt(String key, int def) {
		return mPreferences.getInt(key, def);
	}
	
	///////////////////////////////////////////////////////////
	
	public void setLong(String key, long value) {
		Editor editor = mPreferences.edit();
		editor.putLong(key, value);
		editor.commit();
	}
	
	public long getLong(String key) {
		return getLong(key, 0);
	}
	
	public long getLong(String key, long def) {
		return mPreferences.getLong(key, def);
	}
	
	///////////////////////////////////////////////////////////
	
	public void setFloat(String key, float value) {
		Editor editor = mPreferences.edit();
		editor.putFloat(key, value);
		editor.commit();
	}
	
	public float getFloat(String key) {
		return getInt(key, 0);
	}
	
	public float getFloat(String key, float def) {
		return mPreferences.getFloat(key, def);
	}
	
	///////////////////////////////////////////////////////////
	
	public void setString(String key, String value) {
		Editor editor = mPreferences.edit();
		editor.putString(key, value);
		editor.commit();
	}
	
	public String getString(String key) {
		return getString(key, null);
	}
	
	public String getString(String key, String def) {
		return mPreferences.getString(key, def);
	}
}
