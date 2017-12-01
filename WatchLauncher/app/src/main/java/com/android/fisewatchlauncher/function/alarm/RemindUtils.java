package com.android.fisewatchlauncher.function.alarm;

import android.content.Intent;
import android.text.TextUtils;

import com.android.fisewatchlauncher.KApplication;
import com.android.fisewatchlauncher.entity.dao.AlarmInfo;
import com.android.fisewatchlauncher.event.AlarmStateEvent;
import com.android.fisewatchlauncher.prenster.dao.AlarmClockDaoUtils;
import com.android.fisewatchlauncher.utils.AlarmManagerUtil;
import com.android.fisewatchlauncher.utils.LogUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.android.fisewatchlauncher.utils.AlarmManagerUtil.ALARM_ACTION;
import static com.android.fisewatchlauncher.utils.AlarmManagerUtil.cancelAlarm;
import static com.android.fisewatchlauncher.utils.AlarmManagerUtil.getNextAlarmTriggerAtMillis;

/**
 * @author mare
 * @Description:TODO
 * @csdnblog http://blog.csdn.net/mare_blue
 * @date 2017/9/18
 * @time 11:49
 */
public class RemindUtils {

    public static void parseAndSaveAlarm(String content) {
        AlarmClockDaoUtils.instance().deleteAll();
        cancelAlarm(KApplication.sContext,AlarmManagerUtil.ALARM_ACTION,0);
        if (TextUtils.isEmpty(content)){ return;}
        String[] alarmInfos = TextUtils.split(content,",");
        if (null == alarmInfos || alarmInfos.length <= 0) {return;}
        LogUtils.e(Arrays.toString(alarmInfos));
        LogUtils.e("alarmInfos len " + alarmInfos.length);
        String[] info;
        AlarmInfo alarmInfoBean;
        List<AlarmInfo> data = new ArrayList<>();
        for (int j = 0; j < alarmInfos.length; j++) {
            alarmInfoBean = new AlarmInfo();
            info = alarmInfos[j].trim().split("-");
            for (int i = 0; i < info.length; i++) {
                if (i == 0) {
                    alarmInfoBean.setTime((info[0]));
                }
                if (i == 1) {
                    alarmInfoBean.setOpen(!("0".equals(info[1].trim())));
                }
                if (i == 2) {
                    alarmInfoBean.setMode(Integer.parseInt(info[2]));
                }
                if (i == 3) {
                    alarmInfoBean.setRepeatDayByWeek(info[3]);
                }
            }
            alarmInfoBean.setTriggerAtMillis(AlarmManagerUtil.getTriggerAtMillisOfOnceAlarmInfo(alarmInfoBean));
            data.add(alarmInfoBean);
            LogUtils.e(alarmInfoBean.toString());
        }
        AlarmClockDaoUtils.instance().updateClockList(data);
    }

    // 根据数据库设置最近的闹钟
    public static void setNearAlarm() {
        long nextAlarmTriggerAtMillis = getNextAlarmTriggerAtMillis();
        if (nextAlarmTriggerAtMillis > 0) {
            AlarmManagerUtil.setAlarmOfMillis(KApplication.sContext, nextAlarmTriggerAtMillis, new Intent(ALARM_ACTION));
            EventBus.getDefault().postSticky(new AlarmStateEvent(true));
        }else {
            EventBus.getDefault().postSticky(new AlarmStateEvent(false));
        }
    }
}
