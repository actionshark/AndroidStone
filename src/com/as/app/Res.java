package com.as.app;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.WindowManager;

public class Res {
	private static float sDensity;

	private static int sScreenWidth;
	private static int sScreenHeight;
	private static int sStatusBarHeight = 0;
	
	static {
		Context context = App.getContext();
				
		WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics metrics = new DisplayMetrics();
		manager.getDefaultDisplay().getMetrics(metrics);
		
		sDensity = metrics.density;

		sScreenWidth = metrics.widthPixels;
		sScreenHeight = metrics.heightPixels;

		Resources res = context.getResources();
		int id = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
		if (id > 0) {
			sStatusBarHeight = res.getDimensionPixelSize(id);
		}
	}
	
	public static int getResId(String type, String name) {
		return App.getResources().getIdentifier(name, type, App.getPackageName());
	}
	
	public static int getStringId(String name) {
		return getResId("string", name);
	}
	
	public static int getLayoutId(String name) {
		return getResId("layout", name);
	}
	
	public static int getColorId(String name) {
		return getResId("color", name);
	}
	
	public static int getDimenId(String name) {
		return getResId("dimen", name);
	}
	
	public static int getDrawableId(String name) {
		return getResId("drawable", name);
	}
	
	public static int getRawId(String name) {
		return getResId("raw", name);
	}
	
	public static int getIdId(String name) {
		return getResId("id", name);
	}
	
	public static int getPixcel(float dp) {
		return (int) (dp * sDensity);
	}

	public static int getScreenWidth() {
		return sScreenWidth;
	}

	public static int getScreenHeight() {
		return sScreenHeight - sStatusBarHeight;
	}

	public static int getStatusBarHeight() {
		return sStatusBarHeight;
	}
}
