package com.android.fisewatchlauncher.acty;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.SimpleAdapter;

import com.android.fisewatchlauncher.R;
import com.android.fisewatchlauncher.utils.BarUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class FiseAnalogClockSelect extends BaseActivity implements OnItemClickListener{
	    int mStyleAnalogClock = 2;
	    GridView mGridView;
	    private List<Map<String,Object>> data_list;
	    private SimpleAdapter clockAdapter;
	    private int[] icon = {
	    		R.drawable.analog_clock_1,
	    		R.drawable.analog_clock_3,
	    		R.drawable.analog_clock_4,
	    		R.drawable.analog_clock_5,
	    };
        @Override
        protected void onCreate(Bundle savedInstanceState) {
        	super.onCreate(savedInstanceState);
			requestWindowFeature(Window.FEATURE_NO_TITLE);
			getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
			 WindowManager.LayoutParams.FLAG_FULLSCREEN);
    	    getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
		    getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
		    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		    getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
		    getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
		    getWindow().setStatusBarColor(Color.TRANSPARENT);
        	setContentView(R.layout.fise_analog_clock_select);
        	mGridView = (GridView) findViewById(R.id.grid_view_analog_clock);
        	data_list = new ArrayList<Map<String,Object>>();
        	//getData();
        	//clockAdapter = new SimpleAdapter(this, getData(), R.xml.items, new String[]{"image"},new int[]{R.id.image} );
        	mGridView.setAdapter(clockAdapter);
        	mGridView.setOnItemClickListener(this);
        }
        
		private List<Map<String,Object>>  getData() {
			for(int i=0;i<icon.length;i++){
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("image", icon[i]);
				data_list.add(map);
			}
			return data_list;
		}

		private void closeActivity(){
        	Intent data = new Intent();
        	data.putExtra("style", mStyleAnalogClock);
        	setResult(Activity.RESULT_OK, data);
        	this.finish();
        }

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
			mStyleAnalogClock = position+1;
			BarUtils.ClockUtil.setAnalogClockIndex(position,FiseAnalogClockSelect.this);
			closeActivity();
		}
}
