package com.android.fisewatchlauncher.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.android.fisewatchlauncher.R;


public class SizeUtil {
	public static float Dp2Px(Context context, float f) {
		// TODO Auto-generated method stub
		final float scale = context.getResources().getDisplayMetrics().density;
		return f*scale+0.5f;
	}

	public static float Dp2Px(Context context, int f) {
		// TODO Auto-generated method stub
		final float scale = context.getResources().getDisplayMetrics().density;
		return f*scale+0.5f;
	}

	public static float Sp2Px(Context context, int value) {
		// TODO Auto-generated method stub
		final float fontScale = context.getResources().getDisplayMetrics().density;
		return value*fontScale + 0.5f;
	}
	
	public static void setAnalogClockIndex(int index,Context context){
		SharedPreferences mSharedPreference = context.getSharedPreferences("analog_clock", Context.MODE_PRIVATE);
		Editor editor =mSharedPreference.edit();
    	editor.putInt("index", index);
    	editor.commit();
	}
	
	public static int getAnalogClockIndex(Context context){
		SharedPreferences mSharedPreference = context.getSharedPreferences("analog_clock", Context.MODE_PRIVATE);
		int defaultClock=context.getResources().getInteger(R.integer.default_colock);
		return mSharedPreference.getInt("index", defaultClock);
	}
	
	public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

}
