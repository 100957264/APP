package com.android.fisewatchlauncher.net.subscriber;

import android.content.Context;

import com.android.fisewatchlauncher.client.GlobalSettings;

/**
 * @author mare
 * @Description:
 * @csdnblog http://blog.csdn.net/mare_blue
 * @date 2017/8/9
 * @time 9:46
 */
public class IPSubscribe<String> extends CustomSubscriber<java.lang.String> {

    IPCallBack callBack;

    /**
     * @param context
     */
    public IPSubscribe(Context context, IPCallBack callBack) {
        super(context);
        this.callBack = callBack;
    }

    @Override
    public void onNext(java.lang.String s) {
        super.onNext(s);
        GlobalSettings.instance().setIP(s);
        if (null !=callBack){
            callBack.getIP(s);
        }
    }

    @Override
    public void onError(Throwable e) {
        super.onError(e);

    }

    public interface IPCallBack {
        void getIP(java.lang.String ip);
    }

    @Override
    public boolean showDialog() {
        return false;
    }
}
