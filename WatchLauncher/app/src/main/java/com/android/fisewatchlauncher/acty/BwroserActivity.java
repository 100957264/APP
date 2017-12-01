package com.android.fisewatchlauncher.acty;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.Window;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.android.fisewatchlauncher.R;
import com.android.fisewatchlauncher.exception.CustomException;
import com.android.fisewatchlauncher.widget.CommonWebClient;

/**
 * @author mare
 * @Description:
 * @csdnblog http://blog.csdn.net/mare_blue
 * @date 2017/8/28
 * @time 11:41
 */
public class BwroserActivity extends BaseActivity implements CommonWebClient.WebLoadListener {
    private WebView mWebView;
    private static final String KEY_ACTION_URL = "url";

    public static void openUrl(Context context, String url) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        Intent defaultBrower = new Intent(context, BwroserActivity.class);
        defaultBrower.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        defaultBrower.putExtra(KEY_ACTION_URL, url);
        context.startActivity(defaultBrower);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_browser);
        String url = getIntent().getStringExtra(KEY_ACTION_URL);
        initWebView();
        mWebView.loadUrl(url);
    }

    void initWebView() {
        mWebView = (WebView) findViewById(R.id.webview);
        //mEemptyView = (CommonEmptyView) findViewById(R.id.web_emptyview);
        //setEmptyViewVisibility(false);

        WebSettings webSettings = mWebView.getSettings();//通过WevView获取WevSitting
        webSettings.setSupportZoom(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);//网页加载不完全，有可能是你的DOM储存API没有打开
        webSettings.setLoadsImagesAutomatically(true);
        mWebView.setWebViewClient(new CommonWebClient(this, this));
//        [ERROR:interface_registry.cc(104)] Failed to locate a binder for interface: autofill::mojom::PasswordManagerDriver

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // Check if the key event was the Back button and if there's history
        if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_BACK && mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        } else {
            return false;
        }
        // If it wasn't the Back key or there's no web page history, bubble up to the default
        // system behavior (probably exit the activity)
    }

    @Override
    public void onLoadingStart() {

    }

    @Override
    public void onLoadingFinished() {

    }

    @Override
    public void onLoadingError() {

    }

    @Override
    public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
        int errorCode = error.getErrorCode();
        switch(errorCode) {
            case CustomException.NOT_FOUND:
               // view.loadUrl("file:///android_assets/error.html");
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mWebView != null) {
            try {
                mWebView.getSettings().setBuiltInZoomControls(true);
                mWebView.removeAllViews();
                mWebView.setVisibility(View.GONE);
                long l = ViewConfiguration.getZoomControlsTimeout();
                mWebView.destroy();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mWebView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mWebView.onResume();
    }
}
