package com.android.fisewatchlauncher.entity.dao;

import com.android.fisewatchlauncher.utils.DaoStringConverter;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.OrderBy;

import java.util.List;

/**
 * @author mare
 * @Description:TODO
 * @csdnblog http://blog.csdn.net/mare_blue
 * @date 2017/9/5
 * @time 0:34
 */
@Entity
public class CenterLocation {
    @Id(autoincrement = true)
    @OrderBy
    private Long id;

    public String city;

    public String location;//当前存储的地理位置信息

    public long time;//UTC时间
    public double mLatitude;//DD.DDDDDD
    public String mLatitudeTpe;//N 表示北纬,S 表示南纬.
    public double mLongitude;
    public String mLongitudeType;//E 表示东经,W 表示西经
    public String province;
    public String street;
    public String addr;
    private Boolean isSmartPhone = true;//是否是智能机协议

    @Convert(columnType = String.class, converter = DaoStringConverter.class)
    public List<String> pois;

    public CenterLocation(String city, String location, long time, double mLatitude, double mLongitude) {
        this.city = city;
        this.location = location;
        this.time = time;
        this.mLatitude = mLatitude;
        this.mLongitude = mLongitude;
    }

    @Generated(hash = 469849977)
    public CenterLocation(Long id, String city, String location, long time, double mLatitude, String mLatitudeTpe,
            double mLongitude, String mLongitudeType, String province, String street, String addr,
            Boolean isSmartPhone, List<String> pois) {
        this.id = id;
        this.city = city;
        this.location = location;
        this.time = time;
        this.mLatitude = mLatitude;
        this.mLatitudeTpe = mLatitudeTpe;
        this.mLongitude = mLongitude;
        this.mLongitudeType = mLongitudeType;
        this.province = province;
        this.street = street;
        this.addr = addr;
        this.isSmartPhone = isSmartPhone;
        this.pois = pois;
    }

    @Generated(hash = 1608075134)
    public CenterLocation() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public double getmLatitude() {
        return mLatitude;
    }

    public void setmLatitude(double mLatitude) {
        this.mLatitude = mLatitude;
    }

    public String getmLatitudeTpe() {
        return mLatitudeTpe;
    }

    public void setmLatitudeTpe(String mLatitudeTpe) {
        this.mLatitudeTpe = mLatitudeTpe;
    }

    public double getmLongitude() {
        return mLongitude;
    }

    public void setmLongitude(double mLongitude) {
        this.mLongitude = mLongitude;
    }

    public String getmLongitudeType() {
        return mLongitudeType;
    }

    public void setmLongitudeType(String mLongitudeType) {
        this.mLongitudeType = mLongitudeType;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public Boolean getSmartPhone() {
        return isSmartPhone;
    }

    public void setSmartPhone(Boolean smartPhone) {
        isSmartPhone = smartPhone;
    }

    public List<String> getPois() {
        return pois;
    }

    public void setPois(List<String> pois) {
        this.pois = pois;
    }

    public double getMLatitude() {
        return this.mLatitude;
    }

    public void setMLatitude(double mLatitude) {
        this.mLatitude = mLatitude;
    }

    public String getMLatitudeTpe() {
        return this.mLatitudeTpe;
    }

    public void setMLatitudeTpe(String mLatitudeTpe) {
        this.mLatitudeTpe = mLatitudeTpe;
    }

    public double getMLongitude() {
        return this.mLongitude;
    }

    public void setMLongitude(double mLongitude) {
        this.mLongitude = mLongitude;
    }

    public String getMLongitudeType() {
        return this.mLongitudeType;
    }

    public void setMLongitudeType(String mLongitudeType) {
        this.mLongitudeType = mLongitudeType;
    }

    public Boolean getIsSmartPhone() {
        return this.isSmartPhone;
    }

    public void setIsSmartPhone(Boolean isSmartPhone) {
        this.isSmartPhone = isSmartPhone;
    }
}
