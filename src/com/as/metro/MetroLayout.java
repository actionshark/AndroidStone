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
	protected final RectF mRect = new RectF();
	protected float mRoundX = 10;
	protected float mRoundY = 10;
	
	protected final Paint mPaint = new Paint();
	protected int mColor = 0xff000000;
	protected int mColorPressed = mColor;
	protected boolean mColorHasSet = false;
	
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
		TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.stone);
		
		float rx = ta.getDimension(R.styleable.stone_round_x, mRoundX);
		float ry = ta.getDimension(R.styleable.stone_round_y, mRoundY);
		setRoundSize(rx, ry);
		
		mColor = ta.getColor(R.styleable.stone_color, mColor);
		mColorPressed = ta.getColor(R.styleable.stone_color_pressed, mColor);
		if (ta.hasValue(R.styleable.stone_color)) {
			mColorHasSet = true;
		}
		
		ta.recycle();
		
		mPaint.setAntiAlias(true);
		
		setWillNotDraw(false);
	}
	
	public void setRoundSize(float rx, float ry) {
		mRoundX = rx;
		mRoundY = ry;
		
		setPadding((int)rx, (int)ry, (int)rx, (int)ry);
		
		postInvalidate();
	}
	
	public void setColor(int color) {
		mColor = color;
		mColorHasSet = true;
		
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
	
	public boolean hasColorSet() {
		return mColorHasSet;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		mRect.set(getPaddingLeft(), getPaddingTop(),
				getWidth() - getPaddingRight(),
				getHeight() - getPaddingBottom());
		
		mPaint.setColor(mIsPressed ? mColorPressed : mColor);
		
		canvas.drawRoundRect(mRect, mRoundX, mRoundY, mPaint);
		
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
