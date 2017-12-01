package com.android.fisewatchlauncher.function.weather;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Join;
import android.util.AttributeSet;
import android.view.View;

import com.android.fisewatchlauncher.R;
import com.android.fisewatchlauncher.utils.LogUtils;


/**
 * @author mare
 * @Description:TODO 大雨
 * @csdnblog http://blog.csdn.net/mare_blue
 * @date 2017/9/10 0010
 * @time 23:15
 */
public class BigRainView extends View {
    private static final String TAG = "BigRainView";
    private Context mContext;
    private int viewWidth, viewHight;
    private Paint mPaint, mRainDropsPaint;
    private Bitmap smallCloudy, bigCloudy, mRaindrops;
    private float fristCloudyStartX, fristCloudyStartY;
    private float secondCloudyStartX, secondCloudyStartY;
    private WaterDrop[] waterDrop = new WaterDrop[4];
    private int mTime[] = {800, 700, 1000, 900};
    private int[] dropsStartY = {200, 250, 160, 190};

    public BigRainView(Context context) {
        this(context, null);
    }

    public BigRainView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        initPaint();
    }

    private void initPaint() {
        mPaint = new Paint();
        mRainDropsPaint = new Paint();
        mPaint.setColor(getResources().getColor(R.color.version_eye_2));
        mPaint.setStrokeWidth(15);
        mPaint.setAntiAlias(true);// 设置画笔的锯齿效果
        mPaint.setStrokeJoin(Join.ROUND);//
        mPaint.setStrokeCap(Cap.ROUND);
        mPaint.setStyle(Paint.Style.FILL);// 实心
        this.setTag(false);
        for (int i = 0, count = waterDrop.length; i < count; i++) {
            waterDrop[i] = new WaterDrop();
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.setBackground(getContext().getDrawable(R.color.weather_color));
        smallCloudy = BitmapFactory.decodeResource(mContext.getResources(),
                R.drawable.bmp_weather_cloud_right);
        bigCloudy = BitmapFactory.decodeResource(mContext.getResources(),
                R.drawable.bmp_weather_bigcloudy);
        mRaindrops = BitmapFactory.decodeResource(mContext.getResources(),
                R.drawable.bmp_weather_raindrops);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        beginAnimation(canvas);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(WeatherManager.getMeasuredW(), WeatherManager.getMeasuredH());
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right,
                            int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        viewWidth = getWidth();
        viewHight = getHeight();
        LogUtils.e("viewW- viewH " + viewWidth + " - " + viewHight);
        fristCloudyStartX = -viewWidth / 4;
        fristCloudyStartY = viewHight / 4f;
        secondCloudyStartX = viewWidth;
        secondCloudyStartY = viewHight / 4.7f;
        initWaterDrop();
    }

    private void initWaterDrop() {
        dropsStartY[0] = (int) (5 / 12 * viewHight + fristCloudyStartY);
        dropsStartY[1] = (int) (1 / 2 * viewHight + secondCloudyStartY);
        dropsStartY[2] = (int) (1 / 3 * viewHight + fristCloudyStartY);
        dropsStartY[3] = (int) (19 / 48 * viewHight + secondCloudyStartY);
    }

    private float moveDistance = 0;

    private void drawBitmap(Canvas canvas) {
        canvas.drawBitmap(smallCloudy, secondCloudyStartX - moveDistance,
                secondCloudyStartY, mPaint);
        canvas.drawBitmap(bigCloudy, fristCloudyStartX + moveDistance,
                fristCloudyStartY, mPaint);

    }

    private float mScale = 1;
    private float mScale1 = 1;

    private void drawBigCloudy(Canvas canvas) {
        int width = bigCloudy.getWidth();
        int height = bigCloudy.getHeight();
        canvas.save();
        canvas.scale(1, mScale, fristCloudyStartX + moveDistance - width,
                fristCloudyStartY + height);
        canvas.drawBitmap(bigCloudy, fristCloudyStartX + moveDistance,
                fristCloudyStartY, mPaint);
        canvas.restore();

    }

    private void drawSmallCloudy(Canvas canvas) {
        int width = smallCloudy.getWidth();
        int height = smallCloudy.getHeight();
        canvas.save();
        canvas.scale(1, mScale1, secondCloudyStartX - moveDistance + width,
                secondCloudyStartY + height);
        canvas.drawBitmap(smallCloudy, secondCloudyStartX - moveDistance,
                secondCloudyStartY, mPaint);
        canvas.restore();
    }

    private boolean isAdd = false, isAdd1 = false;


    //开始压缩
    private void startCompress(Canvas canvas) {
        if (isAdd) {
            mScale += 0.01;
            if (mScale >= 1) {
                isAdd = false;
            }
        } else {
            mScale -= 0.01;
            if (mScale <= 0.5f) {
                isAdd = true;
            }
        }
        if (isAdd1) {
            mScale1 += 0.008;
            if (mScale1 >= 1) {
                isAdd1 = false;
            }
        } else {
            mScale1 -= 0.008;
            if (mScale1 <= 0.5f) {
                isAdd1 = true;
            }
        }
        startRain(canvas);
        drawBigCloudy(canvas);
        drawSmallCloudy(canvas);
        invalidate();
    }


    //开始下雨
    private void startRain(Canvas canvas) {
        if (moveDistanceY == 0) {
            for (int i = 0, count = waterDrop.length; i < count; i++) {
                startWave(waterDrop[i], (int) mTime[i]);
            }
        }
        moveDistanceY += 1;
        if (moveDistanceY >= viewHight * 3 / 4) {
            moveDistanceY = 0;
        }
        dropSpeace = 0;
        for (int i = 0, count = waterDrop.length; i < count; i++) {
            dropSpeace += 4;
            waterDrop[i].draw(canvas, i);
        }
    }

    private int moveDistanceY = 0;
    private int dropSpeace = 0;

    private void beginAnimation(Canvas canvas) {
        if ((Boolean) this.getTag()) {
            startCompress(canvas);
        } else {
            moveDistance += 4;
            if (moveDistance >= viewWidth / 2) {
                this.setTag(true);
            }
            drawBitmap(canvas);
            invalidate();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (smallCloudy != null) {
            smallCloudy.recycle();
            smallCloudy = null;
        }
        if (bigCloudy != null) {
            bigCloudy.recycle();
            bigCloudy = null;
        }
        if (mRaindrops != null) {
            mRaindrops.recycle();
            mRaindrops = null;
        }
    }

    private void startWave(final WaterDrop waterDrop, final int time) {
        final ObjectAnimator waterPointYAnim = ObjectAnimator.ofInt(waterDrop,
                "alpha", 0, 244);
        waterPointYAnim.setDuration(time);
        waterPointYAnim.setRepeatCount(1);
        waterPointYAnim.setRepeatMode(ValueAnimator.REVERSE);
        waterPointYAnim.start();
    }

    class WaterDrop {
        private static final String TAG = "WaterDrop";
        // 水滴当前透明值
        private int alpha = 150;
        // 水滴当前下落的距离

        public int getAlpha() {
            return alpha;
        }

        public void setAlpha(int alpha) {
            this.alpha = alpha;
        }

        public void draw(Canvas canvas, int item) {
            int startX = (int) (item % 2 == 1 ? (secondCloudyStartX - moveDistance) : (fristCloudyStartX + moveDistance));
            mRainDropsPaint.setAlpha(alpha);
            canvas.drawBitmap(mRaindrops, startX + dropSpeace,
                    dropsStartY[item] + moveDistanceY, mRainDropsPaint);
        }

        public void changeFrame() {
        }
    }

}