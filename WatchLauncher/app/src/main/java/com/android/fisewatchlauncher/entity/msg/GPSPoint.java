package com.android.fisewatchlauncher.entity.msg;

/**
 * @author mare
 * @Description:TODO 坐标点
 * @csdnblog http://blog.csdn.net/mare_blue
 * @date 2017/9/12
 * @time 10:28
 */
public class GPSPoint {

    public enum PointType {
        GCJ, BD09, WGS84
    }

    public double latitude;//DD.DDDDDD
    public String latitudeTpe;//N 表示北纬,S 表示南纬.
    public double longitude;
    public String longitudeType;//E 表示东经,W 表示西经

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getLatitudeTpe() {
        return latitudeTpe;
    }

    public void setLatitudeTpe(String latitudeTpe) {
        this.latitudeTpe = latitudeTpe;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getLongitudeType() {
        return longitudeType;
    }

    public void setLongitudeType(String longitudeType) {
        this.longitudeType = longitudeType;
    }

    public GPSPoint(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public GPSPoint(double latitude, String latitudeTpe, double longitude, String longitudeType) {
        this.latitude = latitude;
        this.latitudeTpe = latitudeTpe;
        this.longitude = longitude;
        this.longitudeType = longitudeType;
    }
}
