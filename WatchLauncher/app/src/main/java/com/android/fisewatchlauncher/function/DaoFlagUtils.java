package com.android.fisewatchlauncher.function;

import android.text.TextUtils;

import com.android.fisewatchlauncher.client.GlobalSettings;

/**
 * @author mare
 * @Description:TODO
 * @csdnblog http://blog.csdn.net/mare_blue
 * @date 2017/9/12
 * @time 14:13
 */
public class DaoFlagUtils {

    public static String insertSuccessOr(boolean isSuccess) {
        return isSuccess ? "成功" : "失败";
    }

    public static boolean isImeiNull() {
        return TextUtils.isEmpty(GlobalSettings.instance().getImei());
    }

    public static String getImei() {
        return GlobalSettings.instance().getImei();
    }

}
