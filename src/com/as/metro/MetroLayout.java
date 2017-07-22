package com.as.metro;

import com.as.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

public class MetroLayout extends LinearLayout {
	protected final Paint mPaint = new Paint();
	
	protected final RectF mBounds = new RectF();
	
	protected int mRoundWidth = 10;
	protected int mRoundHeight = 10;
	
	protected int mColor = 0xff000000;
	protected int mColorPressed = mColor;
	
	protected boolean mIsPressed = false;

	public MetroLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs);
	}

	public MetroLayout(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context, attrs);
	}

	protected void init(Context context, AttributeSet attrs) {
		mPaint.setAntiAlias(true);
		
		TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.stone);
		
		int rw = ta.getDimensionPixelSize(R.styleable.stone_round_width, mRoundWidth);
		int rh = ta.getDimensionPixelSize(R.styleable.stone_round_height, mRoundHeight);
		setRoundSize(rw, rh);
		
		mColor = ta.getColor(R.styleable.stone_color, mColor);
		mColorPressed = ta.getColor(R.styleable.stone_color_pressed, mColor);
		
		ta.recycle();
		
		setWillNotDraw(false);
	}
	
	public void setRoundSize(int width, int height) {
		mRoundWidth = width;
		mRoundHeight = height;
		
		setPadding(mRoundWidth, mRoundHeight, mRoundWidth, mRoundHeight);
		
		postInvalidate();
	}
	
	public void setColor(int color) {
		mColor = color;
		
		if (mIsPressed == false) {
			postInvalidate();
		}
	}
	
	public void setColorPressed(int color) {
		mColorPressed = color;
		
		if (mIsPressed) {
			postInvalidate();
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		mBounds.set(mRoundWidth, mRoundHeight,
			getWidth() - mRoundWidth, getHeight() - mRoundHeight);
		mPaint.setColor(mIsPressed ? mColorPressed : mColor);
		canvas.drawRoundRect(mBounds, mRoundWidth, mRoundHeight, mPaint);
		
		super.onDraw(canvas);
	}
	
	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		boolean ret = super.onTouchEvent(event);
		
		if (ret) {
			int action = event.getActionMasked();
			
			if (action == MotionEvent.ACTION_DOWN) {
				mIsPressed = true;
			} else if (action == MotionEvent.ACTION_UP ||
					action == MotionEvent.ACTION_CANCEL) {
				
				mIsPressed = false;
			}
		} else {
			mIsPressed = false;
		}
		
		postInvalidate();
		
		return ret;
	}
}
