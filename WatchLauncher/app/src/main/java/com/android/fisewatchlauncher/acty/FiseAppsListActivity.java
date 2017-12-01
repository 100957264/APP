package com.android.fisewatchlauncher.acty;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.fisewatchlauncher.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;


public class FiseAppsListActivity extends BaseActivity implements AdapterView.OnItemClickListener {

	ListView mAppList;
	//SimpleAdapter mFiseAdapter;
	Map<String,Object>  appMap ;
	List<Map<String,Object>> appList;
	List<AppInfo> mAdapgerListAppInfo;
	TextView appNameTextView;
	Typeface fontFace;
	FiseAdapter fiseAdapter;
   @Override
    protected void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
	   setContentView(R.layout.fise_apps_list);
	   mAppList = (ListView)findViewById(R.id.fise_listt_view);
//	   mFiseAdapter = new SimpleAdapter(this,getAppNameIcon(),
//				R.xml.list_item,
//				new String[]{"app_icon","app_name"},
//				new int[]{ R.id.app_icon,R.id.app_name});
	   showListView();

    }
    private void showListView(){
		mAdapgerListAppInfo = getAppNameIcon();
		fiseAdapter = new FiseAdapter(this,mAdapgerListAppInfo);
		mAppList.setAdapter(fiseAdapter);
		mAppList.setOnItemClickListener(this);

	}
	@Override
	protected void onResume() {
		super.onResume();
		IntentFilter filter = new IntentFilter();
		filter.addAction("android.intent.action.PACKAGE_ADDED");
		filter.addAction("android.intent.action.PACKAGE_REMOVED");
		filter.addDataScheme("package");
        this.registerReceiver(mFiseReceiver,filter);
		Log.d("fengqing","registerReceiver...start");
	}
    BroadcastReceiver mFiseReceiver = new BroadcastReceiver(){

		@Override
		public void onReceive(Context context, Intent intent) {
			Log.d("fengqing","onReceive.....start intent=" + intent);
			if(intent.getAction().equals("android.intent.action.PACKAGE_ADDED")){
				fiseAdapter.notifyDataSetChanged();
				showListView();
				String packageName = intent.getDataString();
                Log.d("fengqing","packageName =" + packageName + "is installed");
			} else if(intent.getAction().equals("android.intent.action.PACKAGE_REMOVED")){
				fiseAdapter.notifyDataSetChanged();
				showListView();
				String packageName2 = intent.getDataString();
				Log.d("fengqing","packageName2 =" + packageName2 + "is uninstalled");
			}
		}
	};
	@Override
	protected void onDestroy() {
		super.onDestroy();
		this.unregisterReceiver(mFiseReceiver);
	}

	private  List<AppInfo> getAppNameIcon(){
		List<AppInfo>  mListAppInfo = new ArrayList<AppInfo>();
		appList = new ArrayList<Map<String,Object>>();

		PackageManager pm = this.getPackageManager();
		Intent mainIntent = new Intent(Intent.ACTION_MAIN,null);
		mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
		List<ResolveInfo> resolveInfos = pm.queryIntentActivities(mainIntent,0);
		Collections.sort(resolveInfos,new ResolveInfo.DisplayNameComparator(pm));
		if(appList != null){
			appList.clear();
			for(ResolveInfo resolveInfo:resolveInfos){
				String activityName = resolveInfo.activityInfo.name;
				String pkgName = resolveInfo.activityInfo.packageName;
				if(pkgName.equals("com.android.fisewatchlauncher")){
					continue;
				}
				
				String appName =(String) resolveInfo.loadLabel(pm);
				Drawable appIcon = resolveInfo.loadIcon(pm);
				AppInfo appInfo = new AppInfo();
				ComponentName componentName = new ComponentName(pkgName,activityName);
				Intent launchIntent = new Intent();
				launchIntent.setComponent(componentName);
				appInfo.setAppName(appName);
				appInfo.setAppIcon(appIcon);
				appInfo.setIntent(launchIntent);
				mListAppInfo.add(appInfo);
				Log.d("fengqing","appName =" + appName + ",pkgName = "+ pkgName + ".activityName + " + activityName);
			}
		}
        return mListAppInfo;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Intent intent = mAdapgerListAppInfo.get(position).getIntent();
		startActivity(intent);
	}

	class AppInfo{
		String appName = "";
		String packageName = "";
		String activityName = "";
		Drawable appIcon = null;
		ComponentName componentName = null;
		Intent intent = null;
		public void setComponentName(ComponentName componentName){
			this.componentName = componentName;
		}
	    public void setAppName(String appName){
		    this.appName = appName;
	    }
	    public void setpackageName(String packageName){
		    this.packageName = packageName;
	    }
	    public void setActivityName(String activityName){
		    this.activityName = activityName;
	    }
	    public void setAppIcon(Drawable appIcon){
		    this.appIcon = appIcon;
	    }
		public void setIntent(Intent intent){
			this.intent = intent;
		}
		public Drawable getAppIcon(){
			return appIcon;
		}
		public String getAppName(){
			return appName;
		}
		public ComponentName getComponentName(){
			return componentName;
		}
		public Intent getIntent(){
			return  intent;
		}
	}
	 class FiseAdapter extends BaseAdapter{
		 private List<AppInfo> mAppInfo = null;

		 LayoutInflater infater = null;
		 public FiseAdapter(Context context,List<AppInfo> apps){
			 infater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			 mAppInfo  = apps;
		 }
		 @Override
		 public int getCount() {
			 return mAppInfo.size();
		 }

		 @Override
		 public Object getItem(int position) {
			 return mAppInfo.get(position);
		 }

		 @Override
		 public long getItemId(int position) {
			 return 0;
		 }

		 @Override
		 public View getView(int position, View convertView, ViewGroup parent) {
			 View view = null;
			 FiseViewHolder holder = null;
			 if(convertView == null || convertView.getTag() == null){
				 view = infater.inflate(R.layout.fise_list_item,null);
				 holder = new FiseViewHolder(view);
				 view.setTag(holder);
			 }else {
				 view = convertView;
				 holder = (FiseViewHolder)convertView.getTag();
			 }
			 AppInfo appInfo = (AppInfo) getItem(position);
			 holder.appIcon.setImageDrawable(appInfo.getAppIcon());
			 holder.appName.setText(appInfo.getAppName());
			 return view;
		 }
		 class FiseViewHolder{
			 ImageView appIcon;
			 TextView appName;
			 public FiseViewHolder(View view){
				 this.appIcon = (ImageView)view.findViewById(R.id.app_icon);
				 this.appName = (TextView) view.findViewById(R.id.app_name);
			 }
		 }
	 }
   }



