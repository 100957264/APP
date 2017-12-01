package com.android.fisewatchlauncher.function.weather;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Join;
import android.util.AttributeSet;

import com.android.fisewatchlauncher.R;

/**
 * @author mare
 * @Description:TODO 雾霾
 * @csdnblog http://blog.csdn.net/mare_blue
 * @date 2017/9/10 0010
 * @time 23:15
 */
public class HazeView extends BaseView {
	private static final String TAG = "DrawHaze";
	private Paint mPaint;
	private int mScreenWidth, mScreenHight, mCenterX, mCenterY;

	private boolean isAdd = false;

	/** 线条总长 中间长度 最短的长度 */
	private float overall_length = 0, middle_length = 0, low_length = 0;

	/** 横向两条线之间的间隔 */
	private int interval = 0;
	/** 竖直方向两条线之间的间隔 */
	private float verticalSpace = 0;

	/** 动画总共移动的距离 */
	private int movieDistance = 0;

	/** 当前动画移动的距离 */
	private float curMovie = 0;

	/** 起点间距 */
	private float startSpea = 0;

	public HazeView(Context context) {
		this(context, null);
	}

	public HazeView(Context context, AttributeSet attrs) {
		super(context);
		init();
	}

	public void init() {
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setColor(getResources().getColor(R.color.xiangyabai));
		mPaint.setStrokeJoin(Join.ROUND);//
		mPaint.setStrokeCap(Cap.ROUND);
		mPaint.setStyle(Paint.Style.STROKE);// 设置空心
		mPaint.setStrokeWidth(30);
	}
	
	
	@Override
	protected void onAttachedToWindow() {
		// TODO Auto-generated method stub
		super.onAttachedToWindow();
		this.setBackgroundDrawable(getResources().getDrawable(R.color.weather_color));
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		// TODO Auto-generated method stub
		super.onLayout(changed, left, top, right, bottom);
		if (curMovie != 0) {
			return;
		}
		mScreenWidth = getWidth();
		mScreenHight = getHeight();
		mCenterX = mScreenWidth / 2;
		mCenterY = mScreenHight / 2;
		overall_length = mScreenWidth / 1.66f;
		middle_length = mScreenWidth / 2.75f;
		low_length = mScreenWidth / 7.27f;
		interval = mScreenWidth / 10;
		verticalSpace = mScreenWidth / 9.41f;
		movieDistance = mScreenWidth / 10;
		startSpea = mScreenHight / 26.66f;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		leftMovieLine(canvas);
		rightMovieLine(canvas);
	}

	private void leftMovieLine(Canvas canvas) {
		float startX = mCenterX - overall_length / 2 + curMovie;
		float startY = mCenterY;
		drawline1(canvas, startX + startSpea, startY - verticalSpace * 2);
		drawline(canvas, startX);
		drawline1(canvas, startX - startSpea, startY + verticalSpace * 2);
	}

	private int mAlpha = 255;
	
	private void rightMovieLine(Canvas canvas) {
		float startX = mCenterX - overall_length / 2 - curMovie;
		float startY = mCenterY;
		drawline2(canvas, startX - startSpea, startY - verticalSpace);
		drawline2(canvas, startX + startSpea, startY + verticalSpace);
		if (isAdd) {
			curMovie += 0.5f;
			mAlpha -= 1;
			if (curMovie >= movieDistance) {
				isAdd = false;
			}
		} else {
			curMovie -= 0.5f;
			mAlpha += 1;
			if (curMovie <= 0) {
				isAdd = true;
			}
		}
		mPaint.setAlpha(mAlpha);
		invalidate();
	}
	
	/**
	 * 中间的一条直线
	 * @param canvas
	 * @param startX
	 */
	private void drawline(Canvas canvas, float startX) {
		canvas.drawLine(startX, mCenterY, startX + overall_length, mCenterY,
				mPaint);
	}
	

	/**
	 * 画一条前长后短的直线
	 * 
	 * @param canvas
	 * @param startX
	 * @param startY
	 */
	private void drawline1(Canvas canvas, float startX, float startY) {
		canvas.drawLine(startX, startY, startX + middle_length, startY, mPaint);
		float secondStartX = startX + middle_length + interval;
		canvas.drawLine(secondStartX, startY, secondStartX + low_length,
				startY, mPaint);
	}
	
	/**
	 * 画一条前长后短的直线
	 * 
	 * @param canvas
	 * @param startX
	 * @param startY
	 */
	private void drawline2(Canvas canvas, float startX, float startY) {
		canvas.drawLine(startX, startY, startX + low_length, startY, mPaint);
		float secondStartX = startX + low_length + interval;
		canvas.drawLine(secondStartX, startY, secondStartX + middle_length,
				startY, mPaint);
	}

}
