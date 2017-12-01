package com.android.fisewatchlauncher.event;

import android.graphics.Bitmap;

/**
 * @author mare
 * @Description:
 * @csdnblog http://blog.csdn.net/mare_blue
 * @date 2017/8/26
 * @time 15:07
 */
public class QrUpdateEvent {

    public enum Type {
        UPDATE_BMP, UPDATE_QR
    }

    public Type type;
    public String newImei;
    public Bitmap bmpQR;
    public Bitmap bmpLogo;
    public String description;

    public QrUpdateEvent(Type type,String newImei) {
        this(type,newImei, null);
    }

    public QrUpdateEvent(Type type,String newImei, Bitmap bmpQR) {
        this(type,newImei,bmpQR,null);
    }

    public QrUpdateEvent(Type type, String newImei, Bitmap bmpQR, Bitmap bmpLogo) {
        this.type = type;
        this.newImei = newImei;
        this.bmpQR = bmpQR;
        this.bmpLogo = bmpLogo;
    }
}
