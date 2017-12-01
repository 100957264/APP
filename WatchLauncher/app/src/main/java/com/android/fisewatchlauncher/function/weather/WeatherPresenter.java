package com.android.fisewatchlauncher.function.weather;

import android.text.TextUtils;

import com.android.fisewatchlauncher.client.GlobalSettings;
import com.android.fisewatchlauncher.client.msg.MsgType;
import com.android.fisewatchlauncher.entity.weather.Weather;
import com.android.fisewatchlauncher.event.WeatherUpdateEvent;
import com.android.fisewatchlauncher.manager.StaticManager;
import com.android.fisewatchlauncher.parser.MsgParser;
import com.android.fisewatchlauncher.prenster.dao.WeatherDaoUtils;
import com.android.fisewatchlauncher.utils.DigitalConvert;
import com.android.fisewatchlauncher.utils.LogUtils;
import com.android.fisewatchlauncher.utils.UnicodeUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author mare
 * @Description:TODO 天气相关功能总代理
 * @csdnblog http://blog.csdn.net/mare_blue
 * @date 2017/11/16
 * @time 10:51
 */
public class WeatherPresenter {

    /**
     * TODO 智能协议方式请求天气
     */
    public static void requestSmartWeather() {
        String locationStr = StaticManager.instance().getLocationBeanStr();
        LogUtils.i("requestSmartWeather " + locationStr);
        if (!TextUtils.isEmpty(locationStr)) {
            locationStr = UnicodeUtils.str2UnicodeNo0xu(locationStr).toUpperCase();
            MsgParser.instance().sendMsgByType(MsgType.WEA, locationStr);
        }
    }

    /**
     * TODO 功能协议方式请求天气
     */
    public static void requestFunctionWeather() {
        String locationStr = StaticManager.instance().getLocationBeanStr();
        LogUtils.i("requestFunctionWeather " + locationStr);
        if (!TextUtils.isEmpty(locationStr)) {
            MsgParser.instance().sendMsgByType(MsgType.WT, locationStr);
        }
    }

    /**
     * TODO 功能协议方式请求天气2
     */
    public static void requestFunctionWeather2() {
        String locationStr = StaticManager.instance().getLocationBeanStr();
        LogUtils.i("requestFunctionWeather " + locationStr);
        if (!TextUtils.isEmpty(locationStr)) {
            locationStr = UnicodeUtils.str2UnicodeNo0xu(locationStr).toUpperCase();
            LogUtils.i("requestFunctionWeather after " + locationStr);
            MsgParser.instance().sendMsgByType(MsgType.WT2, locationStr);
        }
    }

    /**
     * TODO 从返回的字符串里解析出温度
     *
     * @param content -27度
     * @return -27
     */
    private static String getTemprature(String content) {
        String result = null;
        if (TextUtils.isEmpty(content)) {
            return result;
        }
        Pattern p = Pattern.compile("-?\\d+");
        Matcher matcher = p.matcher(content.trim());
        if (matcher.find()) {
            result = matcher.group(0);
        }
        return result;
    }

    public static void parseWeather(Weather.CmdType type, String content) {
        if (TextUtils.isEmpty(content)) return;
        Weather wt = getWeatherByServer(type, content);
        LogUtils.e("parseWeather 天气信息\n" + content);
        if (null == wt) {
            LogUtils.e("parseWeather 天气信息解析失败!\n" + content);
            return;
        }
        WeatherUpdateEvent wtEvent = new WeatherUpdateEvent(wt);
        EventBus.getDefault().post(wtEvent);
        StaticManager.instance().weatherUpdateEvent = wtEvent;
        WeatherDaoUtils.instance().update(wt);
    }

    private static Weather getWeatherByServer(Weather.CmdType type, String content) {
        if (TextUtils.isEmpty(content)) return null;
        LogUtils.e("getWeatherByServer " + content);
        Weather weather = new Weather();
        weather.setCmdType(type);
        String[] args;
        switch (type) {
            case WT:
//                天气编号：0——晴 1——阴 2——雨 3——雪
//                年 - 月 - 日, 时:分:
//            秒, 天气描述, 天气编号, 当前温度, 最低温, 最高温, 城 市名
                args = TextUtils.split(content, GlobalSettings.MSG_CONTENT_SEPERATOR);
                weather.date = args[0];
                weather.time = args[1];
                weather.weatherDes = args[2];
                weather.weatherCode = Integer.parseInt(args[3]);
                weather.curTemperature = Double.parseDouble(args[4]);
                weather.minTemperature = Double.parseDouble(args[5]);
                weather.maxTemperature = Double.parseDouble(args[6]);
                weather.city = args[7];
                break;
            case WT2:
            case WEA://同 WT2
                content = DigitalConvert.hexNone0x2String(content);
                args = TextUtils.split(content, GlobalSettings.MSG_CONTENT_SEPERATOR);
                LogUtils.e("getWeatherByServer " + content);
//                深圳市,多云,-27 度,西风,1 级,2016-11-14,10:00:00
                weather.city = args[0];
                weather.weatherDes = args[1];
                LogUtils.e("getTemprature " + args[2]);
                String tempStr = getTemprature(args[2]);
                if (TextUtils.isEmpty(tempStr)) {
                    LogUtils.e("获取天气 温度解析失败 " + args[2]);
                    return null;
                }
                weather.curTemperature = Double.parseDouble(tempStr);
                LogUtils.e("curTemperature " + weather.curTemperature);
                weather.windDirection = args[3];
                weather.windStrength = args[4];
                weather.date = args[5];
                weather.time = args[6];
                break;
        }
        LogUtils.e(weather.toString());
        return weather;
    }
}
