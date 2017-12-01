package com.android.fisewatchlauncher.function.clock;

import android.content.Context;
import android.view.View;

import com.android.fisewatchlauncher.widget.FiseRectAnalogClockFive;
import com.android.fisewatchlauncher.widget.FiseRectAnalogClockFour;
import com.android.fisewatchlauncher.widget.FiseRectAnalogClockOne;
import com.android.fisewatchlauncher.widget.FiseRectAnalogClockSix;
import com.android.fisewatchlauncher.widget.FiseRectAnalogClockThree;
import com.android.fisewatchlauncher.widget.FiseRectAnalogClockTwo;


public class ClockFactory{
	private static ClockFactory mClockFactory;
	
	public static ClockFactory getInstances(){
		if(mClockFactory==null){
			mClockFactory = new ClockFactory();
		}
		return mClockFactory;
	}

	public View getClock(int index, Context context){
		View mView=null;
		switch(index){
		    case 1:
			   mView = new FiseRectAnalogClockOne(context,null);
			  break;
			case 2:
			   mView = new FiseRectAnalogClockTwo(context,null);
			  break;
			case 3:
			   mView = new FiseRectAnalogClockThree(context,null);
			  break;
			case 4:
			   mView = new FiseRectAnalogClockFour(context,null);
			  break;
			case 5:
			   mView = new FiseRectAnalogClockFive(context,null);
			  break;
			case 6:
			   mView = new FiseRectAnalogClockSix(context,null);
			  break;  
			default:
              break;			
		}
		return mView;
	}
}