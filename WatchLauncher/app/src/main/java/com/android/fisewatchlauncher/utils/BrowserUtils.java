package com.android.fisewatchlauncher.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/**
 * 浏览器工具类
 * 
 * @author lizw
 * 
 */
public class BrowserUtils {
	private static final String TAG ="BrowserUtils" ;

	public static void startBrower(Context context, String uri) {
		startBrowser(context, Uri.parse(uri));
	}

	/**
	 * 打开android 浏览器
	 * 
	 * @author lizw
	 * @param context
	 * @param uri
	 */
	public static void startBrowser(Context context, Uri uri) {
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_VIEW);
		/* 其实可以不用添加该Category */
//		intent.addCategory(Intent.CATEGORY_BROWSABLE);
		intent.setData(uri);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		// /* 如果想用浏览器打开本地html文件的话，则只能通过显式intent启动浏览器 */
		boolean explicitMode = false;
		String scheme = uri.getScheme();
		if (scheme != null && scheme.startsWith("file")) {
			explicitMode = true;
		}
		if (explicitMode) {
			intent.setClassName("com.android.browser",
					"com.android.browser.BrowserActivity");
		} else {
			intent.addCategory(Intent.CATEGORY_BROWSABLE);
		}
		context.startActivity(intent);
	}

	public static void startBrowser(Context context, String uri,
                                    String packageName, String className) {
		startBrowser(context, Uri.parse(uri), packageName, className);
	}

	public static void startBrowser(Context context, Uri uri,
                                    String packageName, String className) {
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_VIEW);
		intent.setData(uri);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setClassName(packageName, className);
		context.startActivity(intent);
	}

	/**
	 * 打开默认浏览器
	 * @author lizw
	 * @param context
	 * @param uri
	 */
	public static void startDefaultBrowser(Context context, Uri uri) {
		LogUtils.i("url=" + uri.toString());
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_VIEW);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setData(uri);
		context.startActivity(intent);
	}

	public static void startDefaultBrowser(Context context, String uri) {
		startDefaultBrowser(context, Uri.parse(uri));
	}
}
