package com.stone.thread;

public class ThreadParams {
	public boolean inMainThread = false;
	public Runnable runnable;
	
	public long firstDelay = 0;
	public long repeatDelay = 0;
	
	public long repeatTimes = 1;
	public boolean fixedInterval = true;
}
