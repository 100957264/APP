package com.android.fisewatchlauncher.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by xixionghui on 2016/3/31.
 */
public class DateUtils {

    public static final String PATTERN_TYPE_yyyy_MM_dd_HH_mm_ss = "yyyy-MM-dd HH:mm:ss";

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat();

    private static DateUtils instance = new DateUtils();

    public DateUtils() {
    }

    public static DateUtils getInstance() {
//        if (null == instance) instance = new DateUtils();
        return instance;
    }

    //解析指定格式的字符串成date
    public Date parsePattern(String patternType, String content) throws ParseException {
        Date date = null;
        simpleDateFormat.applyPattern(PATTERN_TYPE_yyyy_MM_dd_HH_mm_ss);
        date = simpleDateFormat.parse(content);
        return date;
    }

    public String yyyy_MM_dd_HH_mm_ss() {
        simpleDateFormat.applyPattern(PATTERN_TYPE_yyyy_MM_dd_HH_mm_ss);
        return simpleDateFormat.format(new Date());
    }

    public static String long2DateStr(long millis) {
        Calendar calendar = Calendar.getInstance();
        LogUtils.d(millis);
        calendar.setTimeInMillis(millis * 1000);
        String created = /*calendar.get(Calendar.YEAR) + "年" +*/
                (calendar.get(Calendar.MONTH) + 1) + "月"//从0计算
                + calendar.get(Calendar.DAY_OF_MONTH) + "日"
                + calendar.get(Calendar.HOUR_OF_DAY) + "时"
                + calendar.get(Calendar.MINUTE) + "分"/*+calendar.get(Calendar.SECOND)+"s"*/;
//        new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(millis));
        LogUtils.d(created);
        return created;
    }

    /**
     * 获取时分
     *
     * @param date
     * @return
     */
    public String getHourMinute(Date date) {
        StringBuilder stringBuilder = new StringBuilder();
        int hour = date.getHours();
        int minute = date.getMinutes();
        stringBuilder = hour < 10 ? stringBuilder.append("0").append(String.valueOf(hour)) : stringBuilder.append(String.valueOf(hour));
        stringBuilder.append(":");
        stringBuilder = minute < 10 ? stringBuilder.append("0").append(minute) : stringBuilder.append(minute);
        return stringBuilder.toString();
    }

    public Date timestamp2Date(long timestamp) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(PATTERN_TYPE_yyyy_MM_dd_HH_mm_ss);
        String d = sdf.format(timestamp);
        Date date = null;
        date = sdf.parse(d);
        return date;
    }

    public Calendar getDefaultCalendar() {
        return Calendar.getInstance();
    }


    public Calendar millis2Calendar(String millis) {
        Calendar calendar = getDefaultCalendar();
        calendar.setTimeInMillis(Long.valueOf(millis));
        return calendar;
    }

    public Calendar millis2Calendar(long millis) {
        Calendar calendar = getDefaultCalendar();
        calendar.clear();
        calendar.setTimeInMillis(millis);
        return calendar;
    }

    public Calendar second2Calendar(long seconds) {
        Calendar calendar = getDefaultCalendar();
        calendar.clear();
        calendar.setTimeInMillis(seconds * 1000);
        return calendar;
    }

    public long calendar2Seconds(Calendar calendar) {
        return calendar.getTimeInMillis() / 1000;
    }


    /**
     * 如果是今天,则显示时:分.否则显示月-日
     *
     * @param pattern
     * @return
     */
    public String getTimeOrDate(String pattern) {
//        Calendar disPlayCalendar = millis2Calendar(millis);
        Date date = null;
        try {
            date = parsePattern(PATTERN_TYPE_yyyy_MM_dd_HH_mm_ss, pattern);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar disPlayCalendar = millis2Calendar(date.getTime());
        Calendar currentCalendar = Calendar.getInstance();

        boolean isSameDay = isSameYear(disPlayCalendar, currentCalendar) &&
                isSameMonth(disPlayCalendar, currentCalendar) &&
                isSameDay(disPlayCalendar, currentCalendar);

        StringBuilder stringBuilder = new StringBuilder();
        if (isSameDay) {
            int hour = disPlayCalendar.get(Calendar.HOUR_OF_DAY);
            stringBuilder = hour < 10 ? stringBuilder.append("0").append(String.valueOf(hour)) : stringBuilder.append(String.valueOf(hour));
            stringBuilder.append(":");
            int minute = disPlayCalendar.get(Calendar.MINUTE);
            stringBuilder = minute < 10 ? stringBuilder.append("0").append(String.valueOf(minute)) : stringBuilder.append(minute);
        } else {
            int month = disPlayCalendar.get(Calendar.MONTH) + 1;
            stringBuilder = month < 10 ? stringBuilder.append("0").append(String.valueOf(month)) : stringBuilder.append(month);
            stringBuilder.append("-");
            int day = disPlayCalendar.get(Calendar.DAY_OF_MONTH);
            stringBuilder = day < 10 ? stringBuilder.append("0").append(String.valueOf(day)) : stringBuilder.append(String.valueOf(day));
        }
        return stringBuilder.toString();
    }

    public String second2TimeOrDate(long seconds) {
        Calendar disPlayCalendar = second2Calendar(seconds);
        Calendar currentCalendar = Calendar.getInstance();

        boolean isSameDay = isSameYear(disPlayCalendar, currentCalendar) &&
                isSameMonth(disPlayCalendar, currentCalendar) &&
                isSameDay(disPlayCalendar, currentCalendar);

        StringBuilder stringBuilder = new StringBuilder();
        if (isSameDay) {
            int hour = disPlayCalendar.get(Calendar.HOUR_OF_DAY);
            stringBuilder = hour < 10 ? stringBuilder.append("0").append(String.valueOf(hour)) : stringBuilder.append(String.valueOf(hour));
            stringBuilder.append(":");
            int minute = disPlayCalendar.get(Calendar.MINUTE);
            stringBuilder = minute < 10 ? stringBuilder.append("0").append(String.valueOf(minute)) : stringBuilder.append(minute);
        } else {
            int month = disPlayCalendar.get(Calendar.MONTH) + 1;
            stringBuilder = month < 10 ? stringBuilder.append("0").append(String.valueOf(month)) : stringBuilder.append(month);
            stringBuilder.append("-");
            int day = disPlayCalendar.get(Calendar.DAY_OF_MONTH);
            stringBuilder = day < 10 ? stringBuilder.append("0").append(String.valueOf(day)) : stringBuilder.append(String.valueOf(day));
        }
        return stringBuilder.toString();
    }


    public boolean isSameYear(Calendar calendar1, Calendar calendar2) {
        boolean isSameYear = calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR);
        return isSameYear;
    }

    public boolean isSameMonth(Calendar calendar1, Calendar calendar2) {
        boolean isSameMonth = calendar1.get(Calendar.MONTH) == calendar2.get(Calendar.MONTH);
        return isSameMonth;
    }

    public boolean isSameDay(Calendar calendar1, Calendar calendar2) {
        boolean isSameDay = calendar1.get(Calendar.DAY_OF_MONTH) == calendar2.get(Calendar.DAY_OF_MONTH);
        return isSameDay;
    }


}
