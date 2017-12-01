package com.android.fisewatchlauncher.utils;

import android.text.TextUtils;

import com.android.fisewatchlauncher.parser.MsgParser;

/**
 * @author mare
 * @Description:TODO unicode和utf-8互转工具类
 * @csdnblog http://blog.csdn.net/mare_blue
 * @date 2017/9/18
 * @time 20:24
 */
public class UnicodeUtils {
    /**
     * 将utf-8的汉字转换成unicode格式汉字码
     *
     * @param string
     * @return
     */
    public static String stringToUnicode(String string) {
        if (TextUtils.isEmpty(string)) {
            return null;
        }
        StringBuffer unicode = new StringBuffer();
        for (int i = 0; i < string.length(); i++) {
            char c = string.charAt(i);
            unicode.append("\\u" + Integer.toHexString(c));
        }
        String str = unicode.toString();

        return str.replaceAll("\\\\", "0x");
    }

    /**
     * 将utf-8的汉字转换成unicode格式并且不含有0xu的汉字码
     *
     * @param string
     * @return
     */
    public static String stringToUnicodeNo0xu(String string) {
        if (TextUtils.isEmpty(string)) {
            return null;
        }
        StringBuffer unicode = new StringBuffer();
        for (int i = 0; i < string.length(); i++) {
            char c = string.charAt(i);
            unicode.append(Integer.toHexString(c));
        }
        String str = unicode.toString();

        return str;
    }

    /**
     * 将unicode并且不含有0xu的汉字码转换成utf-8格式的汉字
     *
     * @param unicode
     * @return
     */
    public static String unicodeToString(String unicode) {
        if (TextUtils.isEmpty(unicode)) {
            return null;
        }
        String str = unicode.replace("0x", "\\");

        StringBuffer string = new StringBuffer();
        String[] hex = str.split("\\\\u");
        for (int i = 1; i < hex.length; i++) {
            int data = Integer.parseInt(hex[i], 16);
            string.append((char) data);
        }
        return string.toString();
    }

    /**
     * TODO 将unicode的汉字码转换成utf-8格式的汉字
     *
     * @param unicode
     * @return
     */
    public static String unicodeNo0xuToString(String unicode) {
        if (TextUtils.isEmpty(unicode)) {
            return null;
        }
        StringBuffer string = new StringBuffer();
        int len = unicode.length();
        int arrayLen = len / 4;
        String hex;
        int data;
        for (int i = 0; i < arrayLen; i++) {
            hex = unicode.substring(i * 4, (i + 1) * 4);
            data = Integer.parseInt(hex, 16);
            string.append((char) data);
        }
        return string.toString();
    }

    /**
     * TODO UTF-8的字符串转换unicode(不含0x //u)
     */
    public static String str2UnicodeNo0xu(String string) {
        StringBuffer unicode = new StringBuffer();
        for (int i = 0; i < string.length(); i++) {
            // 取出每一个字符
            char c = string.charAt(i);
            // 转换为unicode
            unicode.append(MsgParser.get4ByteHexString(c));
        }
        return unicode.toString();
    }
}
