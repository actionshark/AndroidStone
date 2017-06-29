package com.stone.thread;

public class ThreadParams {
	public boolean inMainThread = false;
	public Runnable runnable;
	
	public long firstDelay = 0;
	public long repeatDelay = 0;
	
	public long repeatTimes = 1;
	public boolean fixedInterval = true;
	
	public ThreadParams() {
	}
	
	public ThreadParams(boolean inMainThread, Runnable runnable) {
		this.inMainThread = inMainThread;
		this.runnable = runnable;
	}
	
	public ThreadParams(boolean inMainThread, Runnable runnable, long firstDelay) {
		this.inMainThread = inMainThread;
		this.runnable = runnable;
		this.firstDelay = firstDelay;
	}
	
	public ThreadParams(boolean inMainThread, Runnable runnable, long firstDelay, long repeatDelay) {
		this.inMainThread = inMainThread;
		this.runnable = runnable;
		this.firstDelay = firstDelay;
		this.repeatDelay = repeatDelay;
	}
}
