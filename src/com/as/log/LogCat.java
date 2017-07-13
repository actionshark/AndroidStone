package com.as.log;

import com.js.log.Level;
import com.js.log.Logger;

import android.util.Log;

public class LogCat extends Logger {
	@Override
	protected void onPrint(String tag, Level level, String content) throws Exception {
		if (level == Level.V) {
			Log.v(tag, content);
		} else if (level == Level.D) {
			Log.d(tag, content);
		} else if (level == Level.I) {
			Log.i(tag, content);
		} else if (level == Level.W) {
			Log.w(tag, content);
		} else {
			Log.e(tag, content);
		}
	}
}
