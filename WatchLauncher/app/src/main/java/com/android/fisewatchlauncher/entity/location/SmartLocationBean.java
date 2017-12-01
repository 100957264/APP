package com.android.fisewatchlauncher.entity.location;

/**
 * @author mare
 * @Description:TODO 智能机定位实体类
 * @csdnblog http://blog.csdn.net/mare_blue
 * @date 2017/11/13
 * @time 13:51
 */
public class SmartLocationBean extends NeighboringInfo {
    public String mDate;//日期（UTC）
    public String mTime;//时间（UTC）
    public double mLatitude;//纬度
    public double mLongitude;//经度
    public float accuracy;//精准度
    public String province;//省
    public String city;//市
    public String district;//区
    public String street;// 街道

    public SmartLocationBean() {
    }

    public SmartLocationBean(String mDate, String mTime, double mLatitude, double mLongitude, float accuracy, String province, String city, String district, String street) {
        this.mDate = mDate;
        this.mTime = mTime;
        this.mLatitude = mLatitude;
        this.mLongitude = mLongitude;
        this.accuracy = accuracy;
        this.province = province;
        this.city = city;
        this.district = district;
        this.street = street;
    }


    @Override
    public String format() {
        final StringBuffer sb = new StringBuffer();
        sb.append(mDate);
        sb.append(",").append(mTime);
        sb.append(",").append(mLatitude);
        sb.append(",").append(mLongitude);
        sb.append(",").append(accuracy);
        sb.append(",").append(province);
        sb.append(",").append(city);
        sb.append(",").append(district);
        sb.append(",").append(street);
        return sb.toString();
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("SmartLocationBean{");
        sb.append("mDate='").append(mDate).append('\'');
        sb.append(", mTime='").append(mTime).append('\'');
        sb.append(", mLatitude=").append(mLatitude);
        sb.append(", mLongitude=").append(mLongitude);
        sb.append(", accuracy=").append(accuracy);
        sb.append(", province='").append(province).append('\'');
        sb.append(", city='").append(city).append('\'');
        sb.append(", district='").append(district).append('\'');
        sb.append(", street='").append(street).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
