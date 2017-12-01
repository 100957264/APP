package com.android.fisewatchlauncher.function.step;

import android.text.TextUtils;

import com.android.fisewatchlauncher.client.GlobalSettings;
import com.android.fisewatchlauncher.prenster.dao.CenterCounterDaoUtils;
import com.android.fisewatchlauncher.utils.LogUtils;
import com.android.fisewatchlauncher.utils.ToastUtils;

import java.util.Arrays;
import java.util.List;

/**
 * @author mare
 * @Description:TODO
 * @csdnblog http://blog.csdn.net/mare_blue
 * @date 2017/10/24
 * @time 16:51
 */
public class StepUtils {

    /**
     * 解析计步开关
     *
     * @param content
     */
    public static void parseStepSwitch(String content) {
        LogUtils.e("parseStepSwitch " + content);
        if (TextUtils.isEmpty(content)) {
            ToastUtils.showShort("计步开关格式不对...");
            return;
        }
        int pedo = Integer.parseInt(content);
        CenterCounterDaoUtils.instance().updateStepSwitch(pedo == 1);
    }

    /**
     * 解析计步时间段
     *
     * @param content
     */
    public static void parseStepWalkingTime(String content) {
        LogUtils.e("parseStepWalkingTime " + content);
        if (TextUtils.isEmpty(content)) {
            ToastUtils.showShort("解析计步时间段格式错误...");
            return;
        }
        String[] walkingTimes = TextUtils.split(content, GlobalSettings.MSG_CONTENT_SEPERATOR);
        LogUtils.d("parseStepWalkingTime " + Arrays.toString(walkingTimes));
        if (walkingTimes == null || walkingTimes.length != 3) {
            ToastUtils.showShort("解析计步时间段个数错误...");
            return;
        }
        List<String> times = Arrays.asList(walkingTimes);
        CenterCounterDaoUtils.instance().updateStepWalkingTimes(times);
    }

    /**
     * 解析翻转检测时间段设置
     *
     * @param content
     */
    public static void parseTurnOverTimes(String content) {
        LogUtils.e("parseTurnOverTimes " + content);
        if (TextUtils.isEmpty(content)) {
            ToastUtils.showShort("解析翻转检测时间段格式参数为空错误...");
            return;
        }
        String[] turnOverTimes = TextUtils.split(content, GlobalSettings.MSG_CONTENT_SEPERATOR);
        LogUtils.d("parseTurnOverTimes " + Arrays.toString(turnOverTimes));
        if (turnOverTimes == null || turnOverTimes.length != 1) {
            ToastUtils.showShort("解析翻转检测时间段参数错误...");
            return;
        }
        List<String> times = Arrays.asList(turnOverTimes);
        CenterCounterDaoUtils.instance().updateTurnOverTimes(times);
    }
}
