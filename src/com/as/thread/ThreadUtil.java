package com.as.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.as.log.LogCat;
import com.js.log.Level;
import com.js.log.Logger;
import com.js.thread.ThreadHandler;

import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;

public class ThreadUtil {
	public static final String TAG = ThreadUtil.class.getSimpleName();
	
	private static Handler sMainService;
	
	static {
		sMainService = new Handler(Looper.getMainLooper());
	}
	
	public static boolean isMainThread() {
	    return Looper.getMainLooper().getThread().getId() == Thread.currentThread().getId();
	}
	
	public static ThreadHandler run(final Runnable runnable, final long firstDelay,
			final long repeatDelay, final int repeatTimes, final boolean fixedInterval) {
		
		final ThreadHandler handler = new ThreadHandler();
		handler.setStatus(ThreadHandler.Status.Running);
		
		if (params.inMainThread) {
			handler.mRunnable = new Runnable() {
				@Override
				public void run() {
					synchronized (handler) {
						if (handler.mTryCancel) {
							handler.setStatus(ThreadHandler.Status.Cancelled);
							return;
						}
					}
					
					if (params.fixedInterval) {
						callRunnable(params);
						
						if (countTimes(params)) {
							sMainService.postDelayed(handler.mRunnable, params.repeatDelay);
						} else {
							handler.setStatus(ThreadHandler.Status.Finished);
						}
					} else {
						boolean goon = countTimes(params);
						
						if (goon) {
							sMainService.postDelayed(handler.mRunnable, params.repeatDelay);
						}
						
						callRunnable(params);
						
						if (!goon) {
							handler.setStatus(ThreadHandler.Status.Finished);
						}
					}
				}
			};
			
			sMainService.postDelayed(handler.mRunnable, params.firstDelay);
		} else {
			sNewService.submit(new Runnable() {
				@Override
				public void run() {
					SystemClock.sleep(params.firstDelay);
					
					do {
						synchronized (handler) {
							if (handler.mTryCancel) {
								handler.setStatus(ThreadHandler.Status.Cancelled);
								return;
							}
						}
						
						long startTime = SystemClock.elapsedRealtime();
						
						callRunnable(params);
						
						if(countTimes(params) == false) {
							handler.setStatus(ThreadHandler.Status.Finished);
							return;
						}
						
						if (params.fixedInterval) {
							SystemClock.sleep(params.repeatDelay);
 						} else {
 							long delay = params.repeatDelay - (SystemClock.elapsedRealtime() - startTime);
 							if (delay > 0) {
 								delay = 0;
 							}
 							
 							SystemClock.sleep(delay);
 						}
					} while (true);
				}
			});
		}
		
		return handler;
	}
	
	private static void callRunnable(Runnable runnable) {
		try {
			runnable.run();
		} catch (Exception e) {
			Logger.getInstance().print(TAG, Level.E, e);
		}
	}
	
	private static boolean countTimes(int repeatTimes) {
		if (repeatTimes == 0 || repeatTimes == 1) {
			return false;
		} else {
			repeatTimes--;
			return true;
		}
	}
}
