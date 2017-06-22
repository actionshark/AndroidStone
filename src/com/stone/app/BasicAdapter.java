package com.stone.app;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public abstract class BasicAdapter extends BaseAdapter {
	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public final View getView(int position, View view, ViewGroup parent) {
		if (view == null) {
			view = createView(position);
		}
		
		updateView(position, view);
		
		return view;
	}
	
	protected abstract View createView(int position);
	protected abstract void updateView(int position, View view);
}
