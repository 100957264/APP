package com.android.fisewatchlauncher.function.weather;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Join;
import android.util.AttributeSet;

import com.android.fisewatchlauncher.R;

/**
 * @author mare
 * @Description:TODO 多云
 * @csdnblog http://blog.csdn.net/mare_blue
 * @date 2017/9/10 0010
 * @time 23:15
 */
public class CloudyView extends BaseView {
	private Context mContext;
	private int viewWidth, viewHight;
	private Paint mPaint;
	private Bitmap cloudy1, cloudy2, cloudy3;
	private float fristCloudyStartX, fristCloudyStartY;
	private float sunStartX, sunStartY;
	private float secondCloudyStartX, secondCloudyStartY;

	public CloudyView(Context context) {
		this(context, null);
	}

	public CloudyView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;
		initPaint();
	}

	public void initPaint() {
		this.setBackgroundDrawable(getResources().getDrawable(R.color.weather_color));
		mPaint = new Paint();
		mPaint.setColor(getResources().getColor(R.color.version_eye_2));
		mPaint.setStrokeWidth(15);
		mPaint.setAntiAlias(true);// 设置画笔的锯齿效果
		mPaint.setStrokeJoin(Join.ROUND);//
		mPaint.setStrokeCap(Cap.ROUND);
		mPaint.setStyle(Paint.Style.FILL);// 实心
		this.setTag(false);
	}

	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
//		this.setBackgroundDrawable(getResources().getDrawable(R.color.weather_color));
		cloudy1 = BitmapFactory.decodeResource(mContext.getResources(),
				R.drawable.bmp_weather_cloudy1);
		cloudy2 = BitmapFactory.decodeResource(mContext.getResources(),
				R.drawable.bmp_weather_bigcloudy);
		cloudy3 = BitmapFactory.decodeResource(mContext.getResources(),
				R.drawable.bmp_weather_sun);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		beginAnimation(canvas);
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		// TODO Auto-generated method stub
		super.onLayout(changed, left, top, right, bottom);
		viewWidth = getWidth();
		viewHight = getHeight();
		fristCloudyStartX = viewWidth / 3.9f;
		fristCloudyStartY = viewHight / 2.66f;
		secondCloudyStartX = viewWidth / 1.93f;
		secondCloudyStartY = viewHight / 2f;
		sunStartX = viewWidth / 2.96f;
		sunStartY = viewHight / 4.57f;
	}

	private float moveDistance = 0;

	private void drawBitmap(Canvas canvas) {
		canvas.drawBitmap(cloudy1, secondCloudyStartX + moveDistance,
				secondCloudyStartY, mPaint);
		drawSun(canvas);
		canvas.drawBitmap(cloudy2, fristCloudyStartX - moveDistance,
				fristCloudyStartY, mPaint);
		
		
	}

	private float mDegrees = 0;

	private void drawSun(Canvas canvas) {
		int width = cloudy3.getWidth();
		canvas.save();
		canvas.rotate(mDegrees, sunStartX + width / 2, sunStartY
				+ width / 2);
		canvas.drawBitmap(cloudy3, sunStartX, sunStartY, mPaint);
		canvas.restore();
	}

	private void beginAnimation(Canvas canvas) {
		mDegrees += 0.5;
		if ((Boolean) this.getTag()) {
			moveDistance -= 0.2;
			if (moveDistance <= 0) {
				this.setTag(false);
			}
		}else{
			moveDistance += 0.2;
			if (moveDistance >= 10) {
				this.setTag(true);
			}
		}
		drawBitmap(canvas);
		invalidate();
	}

	@Override
	protected void onDetachedFromWindow() {
		// TODO Auto-generated method stub
		super.onDetachedFromWindow();
		if (cloudy1 != null) {
			cloudy1.recycle();
			cloudy1 = null;
		}
		if (cloudy2 != null) {
			cloudy2.recycle();
			cloudy2 = null;
		}
		if (cloudy3 != null) {
			cloudy3.recycle();
			cloudy3 = null;
		}
	}

}