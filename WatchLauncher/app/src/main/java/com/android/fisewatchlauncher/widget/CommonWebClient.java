package com.android.fisewatchlauncher.widget;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.CountDownTimer;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.android.fisewatchlauncher.utils.LogUtils;


/**
 * Created by blue on 2017/6/16 0016.
 */

public class CommonWebClient extends WebViewClient {
    private static final int WEBVIEW_LOAD_TIMEOUT = 5 * 60 * 1000;// 5 min
    private Context mContext;
    private WebLoadListener mListener;
    private boolean isCounting = false;

    public CommonWebClient(Context mContext, WebLoadListener listener) {
        this.mContext = mContext;
        this.mListener = listener;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {//对网页中超链接按钮的响应
        try {
            if (url.startsWith("http:") || url.startsWith("https:")) {
                view.loadUrl(url); //在当前的webview中跳转到新的url
                return true;
            } else {
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(url));
                    mContext.startActivity(intent);
                    return true;
                } catch (Exception e) {
                    LogUtils.e(String.format("打开url=%s失败 /n错误详情：%s", url, e.getMessage()));
                    return false;
                }
            }
        } catch (Exception e) {
            LogUtils.e(String.format("打开url=%s失败 /n错误详情：%s", url, e.getMessage()));
            return false;
        }
    }

    @Override
    public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
        handler.proceed();// 接受所有证书
        //super.onReceivedSslError(view, handler, error);
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
        countDownTimer.start();
        isCounting = true;
        if (null != mListener) {
            mListener.onLoadingStart();
        }
    }

    @Override
    public void onLoadResource(WebView view, String url) {
        super.onLoadResource(view, url);
        if (!isCounting) {
            if (null != mListener) {
                mListener.onLoadingError();
            }
        }
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        countDownTimer.cancel();
        if (null != mListener) {
            mListener.onLoadingFinished();
        }
    }

    @Override
    public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
//        super.onReceivedError(view, request, error);
        if (null != mListener) {
            mListener.onReceivedError(view, request, error);
        }
    }

    private CountDownTimer countDownTimer = new CountDownTimer(WEBVIEW_LOAD_TIMEOUT, 1000) {
        @Override
        public void onTick(long millisUntilFinished) {
            isCounting = true;
        }

        @Override
        public void onFinish() {
            isCounting = false;
        }
    };

    public interface WebLoadListener {
        public void onLoadingStart();

        public void onLoadingFinished();

        public void onLoadingError();

        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error);
    }
}
