package com.as.app;

import java.util.UUID;

import com.as.log.LogCat;
import com.js.log.Level;
import com.js.log.Logger;
import com.js.thread.IExecutor;
import com.js.thread.ThreadUtil;

import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;

public class App {
	public static final String TAG = App.class.getSimpleName();
	
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

	public static String getDeviceId() {
		String di = "";
		String ssn = "";
		String ai = "";
		
		try {
			TelephonyManager tm = (TelephonyManager) sContext.getSystemService(
				Context.TELEPHONY_SERVICE);
			
			try {
				di = String.valueOf(tm.getDeviceId());
			} catch (Exception e) {
				Logger.getInstance().print(TAG, Level.E, e);
			}
			
			try {
				ssn = String.valueOf(tm.getSimSerialNumber());
			} catch (Exception e) {
				Logger.getInstance().print(TAG, Level.E, e);
			}
		} catch (Exception e) {
			Logger.getInstance().print(TAG, Level.E, e);
		}
		
		try {
			ai = String.valueOf(Secure.getString(sContext.getContentResolver(),
				android.provider.Settings.Secure.ANDROID_ID));
		} catch (Exception e) {
			Logger.getInstance().print(TAG, Level.E, e);
		}
		
		UUID uuid = new UUID(ai.hashCode(), ((long) di.hashCode() << 32)
			| ssn.hashCode());
		
		return uuid.toString();
	}

	public static void exitApp() {
		System.exit(0);
	}
}
