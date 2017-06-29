package com.stone.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.stone.log.Logger;
import com.stone.thread.ThreadParams;
import com.stone.thread.ThreadUtil;

public class EventManager {
	private static class ListenerNode {
		public boolean inMainThread;
		public IEventListener listener;
	}
	
	private static EventManager sEventManager = new EventManager();
	
	public static EventManager getInstance() {
		return sEventManager;
	}
	
	private final Map<String, List<Object>> mEvents = new HashMap<String, List<Object>>();
	
	private final Map<String, List<ListenerNode>> mListeners = new HashMap<String, List<ListenerNode>>();
	
	public synchronized void send(String name, Object data) {
		List<Object> list = mEvents.get(name);
		
		if (list == null) {
			list = new ArrayList<Object>();
			mEvents.put(name, list);
		}
		
		list.add(data);
		
		run();
	}
	
	public synchronized void addListener(String name, boolean inMainThread, IEventListener listener) {
		ListenerNode ln = new ListenerNode();
		ln.inMainThread = inMainThread;
		ln.listener = listener;
		
		List<ListenerNode> list = mListeners.get(name);
		
		if (list == null) {
			list = new ArrayList<ListenerNode>();
			mListeners.put(name, list);
		}
		
		list.add(ln);
	}
	
	public synchronized int removeListener(String name, IEventListener listener) {
		List<ListenerNode> list = mListeners.get(name);
		
		if (list == null) {
			return 0;
		}
		
		int count = 0;
		
		for (int i = list.size() - 1; i >= 0; i--) {
			ListenerNode ln = list.get(i);
			
			if (ln.listener == listener) {
				list.remove(i);
				count++;
			}
		}
		
		return count;
	}
	
	private void run() {
		ThreadUtil.run(new ThreadParams(false, new Runnable() {
			@Override
			public void run() {
				synchronized (EventManager.this) {
					for (Entry<String, List<Object>> events : mEvents.entrySet()) {
						final String name = events.getKey();
						List<Object> datas = events.getValue();
						
						List<ListenerNode> listeners = mListeners.get(name);
						if (listeners != null) {
							for (final ListenerNode ln : listeners) {
								for (final Object data : datas) {
									ThreadUtil.run(new ThreadParams(ln.inMainThread, new Runnable() {
										@Override
										public void run() {
											try {
												ln.listener.onEvent(name, data);
											} catch (Exception e) {
												Logger.print(null, e);
											}
										}
									}));
								}
							}
						}
					}
					
					mEvents.clear();
				}
			}
		}));
	}
}
