package com.android.fisewatchlauncher.function.weather;


import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Join;
import android.util.AttributeSet;

import com.android.fisewatchlauncher.R;

/**
 * @author mare
 * @Description:TODO 阵雨
 * @csdnblog http://blog.csdn.net/mare_blue
 * @date 2017/9/10 0010
 * @time 23:15
 */
public class ShowerView extends BaseView {
    private static final String TAG = "BigRainView";
    private Context mContext;
    private int viewWidth, viewHight;
    private Paint mPaint, mRainDropsPaint;
    private Bitmap smallCloudy, bigCloudy, mRaindrops, mlightning;
    private float fristCloudyStartX, fristCloudyStartY;
    private float secondCloudyStartX, secondCloudyStartY;
    private WaterDrop[] waterDrop = new WaterDrop[4];
    private int mTime[] = {800, 700, 1000, 900};
    /**
     * 雨滴显示初始Y轴坐标
     */
    private int dropsStartY[] = {200, 250, 160, 190};

    public ShowerView(Context context) {
        this(context, null);
    }

    public ShowerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        initPaint();
    }

    public void initPaint() {
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
        this.setBackgroundColor(getResources().getColor(R.color.weather_color));
        smallCloudy = BitmapFactory.decodeResource(mContext.getResources(),
                R.drawable.bmp_weather_cloud_right);
        bigCloudy = BitmapFactory.decodeResource(mContext.getResources(),
                R.drawable.bmp_weather_bigcloudy);
        mRaindrops = BitmapFactory.decodeResource(mContext.getResources(),
                R.drawable.bmp_weather_raindrops);
        mlightning = BitmapFactory.decodeResource(mContext.getResources(),
                R.drawable.bmp_weather_lightning);
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
        if (moveDistance != 0) {
            return;
        }
        viewWidth = getWidth();
        viewHight = getHeight();
        fristCloudyStartX = -viewWidth / 4f;
        fristCloudyStartY = viewHight / 4f;
        secondCloudyStartX = viewWidth;
        secondCloudyStartY = viewHight / 5.5f;
        initWaterDrop();
    }

    private void initWaterDrop() {
        dropsStartY[0] = (int) (5 / 12 * viewHight + fristCloudyStartY);
        dropsStartY[1] = (int) (1 / 2 * viewHight + secondCloudyStartY);
        dropsStartY[2] = (int) (1 / 3 * viewHight + fristCloudyStartY);
        dropsStartY[3] = (int) (19 / 48 * viewHight + secondCloudyStartY);
    }

    private float moveDistance = 0;

    // 画大小白云
    private void drawBitmap(Canvas canvas) {
        mPaint.setAlpha(255);
        canvas.drawBitmap(smallCloudy, secondCloudyStartX - moveDistance,
                secondCloudyStartY, mPaint);
        canvas.drawBitmap(bigCloudy, fristCloudyStartX + moveDistance,
                fristCloudyStartY, mPaint);
    }

    // 开始下雨
    private void startRain(Canvas canvas) {
        if (moveDistanceY == 0) {
            for (int i = 0, count = waterDrop.length; i < count; i++) {
                startDrop(waterDrop[i], (int) mTime[i]);
            }
        }
        if (moveDistanceY <= 12) {
            setBackgroundColor(Color.WHITE);
        } else {
            this.setBackgroundColor(getResources().getColor(R.color.weather_color));
        }
        moveDistanceY += 2;
        if (moveDistanceY >= 7 / 12f * viewHight) {
            moveDistanceY = 0;
            isMove = false;
        }
        dropSpeace = 0;
        for (int i = 0, count = waterDrop.length; i < count; i++) {
            dropSpeace += 2 / 10f * viewWidth;
            waterDrop[i].draw(canvas, i);
        }
    }

    private boolean isMove = false;


    private void moveCloudy(Canvas canvas) {
        if (isMove) {
            moveDistance += 1;
            if (moveDistance >= 3 / 4f * viewWidth) {
                moveDistance = 3 / 4f * viewWidth;
                startRain(canvas);
                canvas.drawBitmap(mlightning, (secondCloudyStartX + fristCloudyStartX) / 2,
                        (secondCloudyStartY + fristCloudyStartY) / 2, mRainDropsPaint);
                canvas.drawBitmap(mlightning, (secondCloudyStartX + fristCloudyStartX) / 2 + moveDistance / 3,
                        (secondCloudyStartY + fristCloudyStartY) / 3 + moveDistance / 2, mRainDropsPaint);
            }
        } else {
            moveDistance -= 1;
            if (moveDistance <= 1 / 4f * viewWidth) {
                isMove = true;
            }
        }
    }

    private int moveDistanceY = 0;
    private int dropSpeace = 0;

    /**
     * 开始动画
     */
    private void beginAnimation(Canvas canvas) {
        if ((Boolean) this.getTag()) {
            // reboundEffect(canvas);
            moveCloudy(canvas);
        } else {
            moveDistance += 10;
            if (moveDistance >= 1/2f * viewWidth) {
                this.setTag(true);
            }
        }
        drawBitmap(canvas);
        invalidate();
    }


    private void startDrop(final WaterDrop waterDrop, final int time) {
        final ObjectAnimator waterPointYAnim = ObjectAnimator.ofInt(waterDrop,
                "alpha", 0, 244); //
        waterPointYAnim.setDuration(time);
        waterPointYAnim.setRepeatCount(1);
        waterPointYAnim.setRepeatMode(ValueAnimator.REVERSE);
        waterPointYAnim.start();
    }

    class WaterDrop {
        // 水滴当前透明值
        private int alpha = 0;

        public int getAlpha() {
            return alpha;
        }

        public void setAlpha(int alpha) {
            this.alpha = alpha;
        }

        public void draw(Canvas canvas, int item) {
            mRainDropsPaint.setAlpha(alpha);
            canvas.drawBitmap(mRaindrops, 1 / 5f * viewWidth + dropSpeace,
                    dropsStartY[item] + moveDistanceY, mRainDropsPaint);
        }

        public void changeFrame() {
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        // TODO Auto-generated method stub
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
        if (mlightning != null) {
            mlightning.recycle();
            mlightning = null;
        }
    }

//    /**
//     * 左边第一个雨滴的X坐标
//     */
//    private static final int RAINDROPS_STARTX = 165;

    /**
     * 白云左右移动最大距离
     */
    private static final int MAX_CLOUDYMOVE = 430;

//    /**
//     * 雨滴下落最大距离
//     */
//    private static final int MAX_RAINDROPMOVE = 270;

}