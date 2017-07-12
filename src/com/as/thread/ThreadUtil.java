package com.as.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.as.log.Logger;

import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;

public class ThreadUtil {
	public static final String TAG = "ThreadUtil";
	
	private static Handler sMainService;
	
	private static ExecutorService sNewService;
	
	static {
		sMainService = new Handler(Looper.getMainLooper());
		
		sNewService = Executors.newCachedThreadPool();
	}
	
	public static boolean isMainThread() {
	    return Looper.getMainLooper().getThread().getId() == Thread.currentThread().getId();
	}
	
	public static ThreadHandler run(final ThreadParams params) {
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
	
	private static void callRunnable(ThreadParams params) {
		try {
			params.runnable.run();
		} catch (Exception e) {
			Logger.print(TAG, e);
		}
	}
	
	private static boolean countTimes(ThreadParams params) {
		if (params.repeatTimes == 0 || params.repeatTimes == 1) {
			return false;
		} else {
			params.repeatTimes--;
			return true;
		}
	}
}
