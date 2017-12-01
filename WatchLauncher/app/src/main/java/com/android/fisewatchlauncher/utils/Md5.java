package com.android.fisewatchlauncher.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by fanyang on 2017/8/7.
 */
public class Md5 {

    public static String getMD5(String val) throws NoSuchAlgorithmException {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        md5.update(val.getBytes());
        byte[] m = md5.digest();//加密
        return getString(m);
    }

    private static String getString(byte[] b) {
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            int a = b[i];
            if (a < 0)
                a += 256;
            if (a < 16)
                buf.append("0");
            buf.append(Integer.toHexString(a)).toString().toLowerCase();

        }
        return buf.toString(); //32位
    }

    /**
     * 小写16位
     * @param val
     * @return
     * @throws NoSuchAlgorithmException
     */
    public static String getMD5Lower16(String val) throws NoSuchAlgorithmException {
        return getMD5(val).toLowerCase().substring(8, 24);
    }

    /**
     * 小写32位
     * @param val
     * @return
     * @throws NoSuchAlgorithmException
     */
    public static String getMD5Lower32(String val) throws NoSuchAlgorithmException {
        return getMD5(val).toLowerCase();
    }

}
