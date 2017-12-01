package com.android.fisewatchlauncher.net.subscriber;

import android.app.ProgressDialog;
import android.content.Context;

import com.android.fisewatchlauncher.exception.CustomException;
import com.android.fisewatchlauncher.exception.CustomThrowable;
import com.android.fisewatchlauncher.utils.LogUtils;
import com.android.fisewatchlauncher.utils.StringUtils;

import rx.Subscriber;

/**
 * @author mare
 * @Description:
 * @csdnblog http://blog.csdn.net/mare_blue
 * @date 2017/8/9
 * @time 9:38
 */
public abstract class BaseSubscriber<T> extends Subscriber<T> {
    public ProgressDialog progress;
    public Context context;

    public BaseSubscriber(Context ctx) {
        this.context = ctx;
    }

    @Override
    public void onCompleted() {
        LogUtils.v("onCompleted");
    }

    @Override
    public void onError(Throwable e) {
        if (e != null && e.getMessage() != null) {
            LogUtils.v(e.getMessage());

        } else {
            LogUtils.v("Throwable  || Message == Null");
        }

        if (e instanceof CustomThrowable) {
            LogUtils.i("--> e instanceof CustomThrowable");
            LogUtils.i("--> " + StringUtils.getCauseStr(e));
            onError((CustomThrowable) e);
        } else {
            LogUtils.i("e !instanceof CustomThrowable");
            LogUtils.e("--> " + StringUtils.getCauseStr(e));
            onError(CustomException.handleException(e));
        }
        onCompleted();
    }

    //    public abstract void onError(CustomThrowable e);
    public abstract void onError(CustomThrowable e);

    @Override
    public void onStart() {
        super.onStart();
        LogUtils.v("onStart");
    }

    public boolean showDialog() {
        return true;
    }

    public void dismissDialog() {
        if (progress != null && progress.isShowing()) {
            progress.dismiss();
        }
    }

    public void onBegin(){
        if (showDialog()) {
            progress = new ProgressDialog(context);
            progress.setMessage("正在加载请稍后....");
        }
        if (progress != null) {
            dismissDialog();
            if (showDialog()) {
                progress.show();
            }
        }
    }

    public String getSubscriberName() {
        return getClass().getSimpleName();
    }

}
