package com.as.app;

import com.as.log.LogCat;
import com.js.log.Logger;
import com.js.thread.IExecutor;
import com.js.thread.ThreadUtil;

import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Looper;

public class App {
	private static Context sContext;
	
	private static Handler sHandler;
	
	public static void onApplicationCreate(Context context) {
		if (sContext != null) {
			return;
		}
		
		sContext = context;
		
		sHandler = new Handler(Looper.getMainLooper());
		
		Logger.setInstance(new LogCat());
		
		ThreadUtil.setMain(new IExecutor() {
			@Override
			public void run(Runnable runnable, long delay) {
				sHandler.postDelayed(runnable, delay);
			}
		});
	}
	
	public static Context getContext() {
		return sContext;
	}
	
	public static Resources getResources() {
		return sContext.getResources();
	}
	
	public static String getPackageName() {
		return sContext.getPackageName();
	}

	public static void exitApp() {
		android.os.Process.killProcess(android.os.Process.myPid());
	}
}
