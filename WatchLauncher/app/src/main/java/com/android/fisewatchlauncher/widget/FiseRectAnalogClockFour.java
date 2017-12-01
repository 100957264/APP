package com.android.fisewatchlauncher.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.android.fisewatchlauncher.R;
import com.android.fisewatchlauncher.utils.DpUtil;

import java.util.Calendar;

public class FiseRectAnalogClockFour extends View {
	private float mRadius; // 外圆半径
	private float mPadding; // 边距
	private float mTextSize; // 文字大小
	private float mHourPointWidth; // 时针宽度
	private float mMinutePointWidth; // 分针宽度
	private float mSecondPointWidth; // 秒针宽度
	private int mPointRadius; // 指针圆角
	private float mPointEndLength; // 指针末尾的长度

	private int mColorLong; // 长线的颜色
	private int mColorShort; // 短线的颜色
	private int mHourPointColor; // 时针的颜色
	private int mMinutePointColor; // 分针的颜色
	private int mSecondPointColor; // 秒针的颜色
    
	private Paint mPaint; // 画笔
    Resources mResources;
    Bitmap mBitmap;
    private Rect mSrcRect,mDestRect;
    Bitmap mBackgroundBmp;
    int mBackgroundWidth,mBackgroundHeight;
    
	public FiseRectAnalogClockFour(Context context) {
		this(context, null);
		// TODO Auto-generated constructor stub
	}

	public FiseRectAnalogClockFour(Context context, AttributeSet attrs) {
		super(context, attrs);
		obtainStyledAttrs(attrs); // 获取自定义的属性
		mResources = getResources();
		 mBackgroundBmp = BitmapFactory.decodeResource(getResources(), R.drawable.clock_analog_bg4);
		 mBackgroundWidth = mBackgroundBmp.getWidth();
		 mBackgroundHeight = mBackgroundBmp.getHeight();
		init();// 初始化画笔
	}


	private void obtainStyledAttrs(AttributeSet attrs) {
		// TODO Auto-generated method stub
		TypedArray array = null;
		try {
			array = getContext().obtainStyledAttributes(attrs,
					R.styleable.WatchBoard);
			mPadding = array.getDimension(R.styleable.WatchBoard_wb_padding,
					DptoPx(10));
			mTextSize = array.getDimension(R.styleable.WatchBoard_wb_text_size,
					SptoPx(10));
			mHourPointWidth = array.getDimension(
					R.styleable.WatchBoard_wb_hour_pointer_width, DptoPx(3));
			mMinutePointWidth = array.getDimension(
					R.styleable.WatchBoard_wb_minute_pointer_width, DptoPx(2));
			mSecondPointWidth = array.getDimension(
					R.styleable.WatchBoard_wb_second_pointer_width, DptoPx(1));
			mPointRadius = (int) array
					.getDimension(
							R.styleable.WatchBoard_wb_pointer_corner_radius,
							DptoPx(8));
			mPointEndLength = array.getDimension(
					R.styleable.WatchBoard_wb_pointer_end_length, DptoPx(8));

			mColorLong = array.getColor(
					R.styleable.WatchBoard_wb_scale_long_color,
					Color.argb(225, 0, 0, 0));
			mColorShort = array.getColor(
					R.styleable.WatchBoard_wb_scale_short_color,
					Color.argb(125, 0, 0, 0));
			mHourPointColor = array.getColor(R.styleable.WatchBoard_wb_hour_pointer_color, Color.BLACK);
			mMinutePointColor = array
					.getColor(R.styleable.WatchBoard_wb_minute_pointer_color,
							Color.BLACK);
			mSecondPointColor = array.getColor(
					R.styleable.WatchBoard_wb_second_pointer_color, Color.RED);
		} catch (Exception e) {
			// 一旦出现错误全部使用默认值
			mPadding = DptoPx(8);
			mTextSize = SptoPx(10);
			mHourPointWidth = DptoPx(3);
			mMinutePointWidth = DptoPx(2);
			mSecondPointWidth = DptoPx(1);
			mPointRadius = (int) DptoPx(10);
			mPointEndLength = DptoPx(10);

			mColorLong = Color.argb(225, 0, 0, 0);
			mColorShort = Color.argb(125, 0, 0, 0);
			mHourPointColor = Color.BLACK;
			mMinutePointColor = Color.BLACK;
			mSecondPointColor = Color.RED;
		} finally {
			if (array != null) {
				array.recycle();
			}
		}
	}

	// 画笔初始化
	private void init() {
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setDither(true);
		mPaint.setFilterBitmap(true);
	
	}

	// Dp转px
	private float DptoPx(int value) {

		return DpUtil.dp2px(getContext(), value);
	}

	// sp转px
	private float SptoPx(int value) {
		return DpUtil.sp2px( value);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int width = 1000; // 设定一个最小值

		int widthSize = MeasureSpec.getSize(widthMeasureSpec);
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		int heightSize = MeasureSpec.getSize(heightMeasureSpec);
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);

		if (widthMode == MeasureSpec.AT_MOST
				|| widthMode == MeasureSpec.UNSPECIFIED
				|| heightMeasureSpec == MeasureSpec.AT_MOST
				|| heightMeasureSpec == MeasureSpec.UNSPECIFIED) {
			try {
				throw new NoDetermineSizeException(
						"宽度高度至少有一个确定的值,不能同时为wrap_content");
			} catch (NoDetermineSizeException e) {
				e.printStackTrace();
			}
		} else { // 至少有一个为确定值,要获取其中的最小值
			if (widthMode == MeasureSpec.EXACTLY) {
				width = Math.min(widthSize, width);
			}
			if (heightMode == MeasureSpec.EXACTLY) {
				width = Math.min(heightSize, width);
			}
		}

		setMeasuredDimension(width, width);
	}
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
    	// TODO Auto-generated method stub
    	super.onSizeChanged(w, h, oldw, oldh);
    	mRadius = (Math.min(w, h) - mPadding) / 2;
        mPointEndLength = mRadius / 6; //尾部指针默认为半径的六分之一
    }
    @Override
    protected void onDraw(Canvas canvas) {
    	// TODO Auto-generated method stub
    	super.onDraw(canvas);
    	canvas.save();
        //canvas.translate(getWidth() / 2, getHeight() / 2);
       
      //绘制外圆背景
      paintCircle(canvas);
      //绘制刻度
        //paintScale(canvas);
        paintScale2(canvas);
        //绘制表盘上的日期
        paintDate(canvas);
        //绘制指针
        canvas.translate(mBackgroundWidth / 2, mBackgroundHeight / 2);
        //canvas.translate(getWidth() / 2, getHeight() / 2);
        paintPointer(canvas);
        canvas.restore();
        //刷新
        postInvalidateDelayed(1000);
    }
   private void paintDate(Canvas canvas) {
		// TODO Auto-generated method stub
		Paint paint = new Paint();  
        paint.setTextSize(16);  
		paint.setAntiAlias(true);
		paint.setDither(true);
		paint.setColor(Color.WHITE);
	   Calendar c = Calendar.getInstance();
	   int year = c.get(Calendar.YEAR);
	   int month =c.get(Calendar.MONTH) + 1;
	   int day = c.get(Calendar.DAY_OF_MONTH);
	   canvas.drawText(year+"", mBackgroundWidth/3-8, mBackgroundHeight/2 + 70, paint);
	   canvas.drawText(month+"", mBackgroundWidth/3+43, mBackgroundHeight/2 + 70, paint);
	   canvas.drawText(day+"", mBackgroundWidth/3+67, mBackgroundHeight/2 + 70, paint);
	}

private void paintScale2(Canvas canvas) {
		// TODO Auto-generated method stub
//		Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.clock_analog_rect_background);
//        int mBitWidth = bmp.getWidth();
//        int mBitHeight = bmp.getHeight();
        mSrcRect = new Rect(0,0,mBackgroundWidth,mBackgroundHeight);
        mDestRect =  new Rect(0,0,mBackgroundWidth,mBackgroundHeight);
        
        canvas.drawBitmap(mBackgroundBmp, mSrcRect, mDestRect, mPaint);
	}

	//绘制外圆背景
    public void paintCircle(Canvas canvas) {
        mPaint.setColor(Color.WHITE);
        mPaint.setStyle(Paint.Style.FILL);
       // canvas.drawColor(Color.TRANSPARENT, Mode.CLEAR);
        //canvas.drawCircle(0, 0, mRadius, mPaint);
      
    }
  //绘制刻度
    private void paintScale(Canvas canvas) {
        mPaint.setStrokeWidth(DpUtil.dp2px(getContext(), 1));
        int lineWidth = 0;
        for (int i = 0; i < 60; i++) {
            if (i % 5 == 0) { //整点
                mPaint.setStrokeWidth(DpUtil.Dp2Px(getContext(), 1.5f));
                mPaint.setColor(mColorLong);
                lineWidth = 20;
                mPaint.setTextSize(mTextSize);
                String text = ((i / 5) == 0 ? 12 : (i / 5)) + "";
                Rect textBound = new Rect();
                mPaint.getTextBounds(text, 0, text.length(), textBound);
                mPaint.setColor(Color.BLACK);
                canvas.save();
                canvas.translate(0, -mRadius + DptoPx(12) + lineWidth + (textBound.bottom - textBound.top));
                canvas.rotate(-6 * i);
                mPaint.setStyle(Paint.Style.FILL);
                canvas.drawText(text, -(textBound.right - textBound.left) / 2,textBound.bottom, mPaint);
                canvas.restore();
            } else { //非整点
                lineWidth = 15;
                mPaint.setColor(mColorShort);
                mPaint.setStrokeWidth(DpUtil.dp2px(getContext(), 1));
            }
            canvas.drawLine(0, -mRadius + DpUtil.dp2px(getContext(), 10), 0, -mRadius + DpUtil.dp2px(getContext(), 10) + lineWidth, mPaint);
            canvas.rotate(6);
        }
        canvas.restore();
    }
    private void paintPointer(Canvas canvas) {
        Calendar calendar = Calendar.getInstance();
        float hour = calendar.get(Calendar.HOUR_OF_DAY); //时
        float minute = calendar.get(Calendar.MINUTE); //分
        float second = calendar.get(Calendar.SECOND); //秒
        float angleSecond = second * 6.0f; //秒针转过的角度
		float angleMinute =  minute * 6 + 6 * second / 60F; //分针转过的角度
		float angleHour =    (hour % 12) * 30 + 30 * angleMinute / 360F; //时针转过的角度
        //绘制时针
        canvas.save();
        canvas.rotate(angleHour); //旋转到时针的角度
        Bitmap hourBmp = BitmapFactory.decodeResource(getResources(), R.drawable.clock_analog_hour4);
        int mHourWidth = hourBmp.getWidth();
        int mHourHeight = hourBmp.getHeight();
        Rect mHourSrcRect = new Rect(0,0,mHourWidth,mHourHeight);
        RectF mHourDestRect = new RectF(-mHourWidth / 2, -mBackgroundHeight/2  , mHourWidth/ 2, mHourHeight/2);
        canvas.drawBitmap(hourBmp, mHourSrcRect, mHourDestRect, mPaint);

        canvas.restore();
        //绘制分针
        canvas.save();
        canvas.rotate(angleMinute);
        Bitmap minuteBmp = BitmapFactory.decodeResource(getResources(), R.drawable.clock_analog_minute4);
        int mMinuteWidth = minuteBmp.getWidth();
        int mMinuteHeight = minuteBmp.getHeight();
        Rect mMinuteSrcRect = new Rect(0,0,mMinuteWidth,mMinuteHeight);
        RectF mMinuteDestRect  =  new RectF(-mMinuteWidth / 2, -mBackgroundHeight/2  , mMinuteWidth/ 2, mMinuteHeight/2);
        canvas.drawBitmap(minuteBmp, mMinuteSrcRect, mMinuteDestRect, mPaint);

        canvas.restore();
        //绘制秒针
        canvas.save();
        canvas.rotate(angleSecond);
        Bitmap secondBmp = BitmapFactory.decodeResource(getResources(), R.drawable.clock_analog_second4);
        int mSecondWidth = minuteBmp.getWidth();
        int mSecondHeight = minuteBmp.getHeight();
        Rect mSecondSrcRect = new Rect(0,0,mSecondWidth,mSecondHeight);
        RectF mSecondDestRect  = new RectF(-mSecondWidth / 2, -mBackgroundHeight/2  , mSecondWidth/ 2, mSecondHeight/2);
        canvas.drawBitmap(secondBmp, mSecondSrcRect, mSecondDestRect, mPaint);
       //绘制中心圆
//        Bitmap pointerBmp = BitmapFactory.decodeResource(getResources(), R.drawable.clock_analog_point1);
//        int mPointWidth = minuteBmp.getWidth();
//        int mPointHeight = minuteBmp.getHeight();
//        Rect mPointerSrcRect = new Rect(0,0,mPointWidth,mPointHeight);
//        RectF mPointerDestRect  = new RectF(-mPointWidth / 2, -mPointHeight/2 , mPointWidth/ 2, mPointHeight/2);
//       // System.out.print("mPointHeight =" + mPointHeight + ",mPointerWidth =" + mPointWidth);
//        canvas.drawBitmap(pointerBmp, mPointerSrcRect, mPointerDestRect, mPaint);
        canvas.restore();
     
       
    }
	class NoDetermineSizeException extends Exception {
		public NoDetermineSizeException(String message) {
			super(message);
		}
	}
}
