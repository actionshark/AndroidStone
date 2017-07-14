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
	private static App sInstance = null;
	
	public static synchronized App getInstance() {
		if (sInstance == null) {
			sInstance = new App();
		}
		
		return sInstance;
	}
	
	//////////////////////////////////////////////////////////////////////////
	
	private Context mContext;
	
	private final Handler mHandler;
	
	private App() {
		mHandler = new Handler(Looper.getMainLooper());
	}
	
	public void onApplicationCreate(Context context) {
		mContext = context;
		
		Logger.setInstance(new LogCat());
		
		ThreadUtil.setMain(new IExecutor() {
			@Override
			public void run(Runnable runnable, long delay) {
				mHandler.postDelayed(runnable, delay);
			}
		});
	}
	
	public Context getContext() {
		return mContext;
	}
	
	public Resources getResources() {
		return mContext.getResources();
	}
	
	public String getPackageName() {
		return mContext.getPackageName();
	}

	public void exitApp() {
		android.os.Process.killProcess(android.os.Process.myPid());
	}
}
