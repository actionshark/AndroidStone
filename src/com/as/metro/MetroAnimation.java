package com.as.metro;

import java.util.concurrent.atomic.AtomicInteger;

import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;

public class MetroAnimation {
	protected long mDuration = 200l;
	
	public void setDuration(long duration) {
		mDuration = duration;
	}
	
	public void runStarting(ViewGroup root) {
		runStarting(root, new AtomicInteger(1));
	}
	
	protected void runStarting(ViewGroup root, AtomicInteger index) {
		int count = root.getChildCount();
		
		for (int i = 0; i < count; i++) {
			View child = root.getChildAt(i);
			if (child instanceof ViewGroup == false) {
				continue;
			}
			
			if (child instanceof MetroLayout) {
				Animation anim = createAnimation();
				anim.setDuration(mDuration);
				anim.setStartOffset(mDuration * index.getAndIncrement());
				
				child.startAnimation(anim);			}
			
			runStarting((ViewGroup) child, index);
		}
	}
	
	protected Animation createAnimation() {
		Animation anim = new ScaleAnimation(0f, 1f, 0f, 1f,
			Animation.RELATIVE_TO_SELF, 0.5f,
			Animation.RELATIVE_TO_SELF, 0.5f);
		
		return anim;
	}
}
