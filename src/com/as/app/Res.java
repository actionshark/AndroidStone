package com.as.app;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.WindowManager;

public class Res {
	private static Res sInstance = null;
	
	public static synchronized Res getInstance() {
		if (sInstance == null) {
			sInstance = new Res();
		}
		
		return sInstance;
	}
	
	//////////////////////////////////////////////////////////////////////////
	
	private float mDensity;

	private int mScreenWidth;
	private int mScreenHeight;
	private int mStatusBarHeight = 0;
	
	private Res() {
		App app = App.getInstance();
		Context context = app.getContext();
				
		WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics metrics = new DisplayMetrics();
		manager.getDefaultDisplay().getMetrics(metrics);
		
		mDensity = metrics.density;

		mScreenWidth = metrics.widthPixels;
		mScreenHeight = metrics.heightPixels;

		Resources res = context.getResources();
		int id = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
		if (id > 0) {
			mStatusBarHeight = res.getDimensionPixelSize(id);
		}
	}
	
	public int getResId(String type, String name) {
		App app = App.getInstance();
		return app.getResources().getIdentifier(name, type, app.getPackageName());
	}
	
	public int getStringId(String name) {
		return getResId("string", name);
	}
	
	public int getLayoutId(String name) {
		return getResId("layout", name);
	}
	
	public int getColorId(String name) {
		return getResId("color", name);
	}
	
	public int getDimenId(String name) {
		return getResId("dimen", name);
	}
	
	public int getDrawableId(String name) {
		return getResId("drawable", name);
	}
	
	public int getRawId(String name) {
		return getResId("raw", name);
	}
	
	public int getIdId(String name) {
		return getResId("id", name);
	}
	
	public int getPixcel(float dp) {
		return (int) (dp * mDensity);
	}

	public int getScreenWidth() {
		return mScreenWidth;
	}

	public int getScreenHeight() {
		return mScreenHeight - mStatusBarHeight;
	}

	public int getStatusBarHeight() {
		return mStatusBarHeight;
	}
}
