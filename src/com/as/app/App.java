package com.as.app;

import android.content.Context;
import android.content.res.Resources;

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
	
	private App() {
	}
	
	public void onApplicationCreate(Context context) {
		mContext = context;
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
