package com.as.metro;

import java.util.concurrent.atomic.AtomicInteger;

import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.ScaleAnimation;

public class MetroAnimation {
	public static interface IAnimationListener {
		public void onFinish();
	}
	
	protected IAnimationListener mListener;
	
	protected long mDuration = 200l;
	
	public void setDuration(long duration) {
		mDuration = duration;
	}
	
	public void setListener(IAnimationListener listener) {
		mListener = listener;
	}
	
	public void runStarting(ViewGroup root) {
		runStarting(root, new AtomicInteger(0));
	}
	
	protected void runStarting(ViewGroup root, final AtomicInteger index) {
		int count = root.getChildCount();
		
		for (int i = 0; i < count; i++) {
			View child = root.getChildAt(i);
			if (child instanceof ViewGroup == false) {
				continue;
			}
			
			if (child instanceof MetroLayout) {
				final int idx = index.incrementAndGet();
				
				Animation anim = createAnimation();
				anim.setDuration(mDuration);
				anim.setStartOffset(mDuration * idx);
				anim.setAnimationListener(new AnimationListener() {
					@Override
					public void onAnimationStart(Animation animation) {
					}
					
					@Override
					public void onAnimationRepeat(Animation animation) {
					}
					
					@Override
					public void onAnimationEnd(Animation animation) {
						if (index.get() == idx && mListener != null) {
							mListener.onFinish();
						}
					}
				});
				
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
