package com.android.fisewatchlauncher.function.gps;

/**
 * @author mare
 * @Description:TODO
 * @csdnblog http://blog.csdn.net/mare_blue
 * @date 2017/11/16
 * @time 14:21
 */
public enum GpsErrorType {
    LOCATE_TIMEOUT, //正在定位（一直定位不到结果）
    LOCATE_ERROR       //定位过程中异常(比如硬件不支持...)
}
