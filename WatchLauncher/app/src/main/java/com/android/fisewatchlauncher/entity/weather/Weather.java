package com.android.fisewatchlauncher.entity.weather;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.converter.PropertyConverter;

/**
 * @author mare
 * @Description:TODO
 * @date 2017/9/18/018
 * @time 9:15
 */
@Entity
public class Weather {
    @Id
    public long id;

    public enum CmdType {
        WT(0), WT2(1), WEA(2);

        final int id;

        CmdType(int id) {
            this.id = id;
        }
    }

//        @Transient
    @Convert(converter = WeatherTypeConverter.class, columnType = Integer.class)
    public CmdType cmdType;
    public String date;
    public String time;
    public String weatherDes;
    public int weatherCode;//天气编号
    //                年 - 月 - 日, 时:分:
//            秒, 天气描述, 天气编号, 当前温度, 最低温, 最高温, 城 市名
    public double curTemperature;
    public double minTemperature;
    public double maxTemperature;
    public String city;
    public String windDirection;//风向
    public String windStrength;//风力

    @Generated(hash = 556711069)
    public Weather() {
    }

    @Generated(hash = 486647791)
    public Weather(long id, CmdType cmdType, String date, String time, String weatherDes,
            int weatherCode, double curTemperature, double minTemperature,
            double maxTemperature, String city, String windDirection, String windStrength) {
        this.id = id;
        this.cmdType = cmdType;
        this.date = date;
        this.time = time;
        this.weatherDes = weatherDes;
        this.weatherCode = weatherCode;
        this.curTemperature = curTemperature;
        this.minTemperature = minTemperature;
        this.maxTemperature = maxTemperature;
        this.city = city;
        this.windDirection = windDirection;
        this.windStrength = windStrength;
    }

    public static class WeatherTypeConverter implements PropertyConverter<CmdType, Integer> {
        @Override
        public CmdType convertToEntityProperty(Integer databaseValue) {
            if (databaseValue == null) {
                return null;
            }
            for (CmdType role : CmdType.values()) {
                if (role.id == databaseValue) {
                    return role;
                }
            }
            return CmdType.WT;
        }

        @Override
        public Integer convertToDatabaseValue(CmdType entityProperty) {
            return entityProperty == null ? null : entityProperty.id;
        }
    }

    @Override
    public String toString() {
        return "Weather{" +
                "cmdType=" + cmdType.toString() +
                ", date='" + date + '\'' +
                ", time='" + time + '\'' +
                ", weatherDes='" + weatherDes + '\'' +
                ", weatherCode='" + weatherCode + '\'' +
                ", curTemperature=" + curTemperature +
                ", minTemperature=" + minTemperature +
                ", maxTemperature=" + maxTemperature +
                ", city='" + city + '\'' +
                ", windDirection='" + windDirection + '\'' +
                ", windStrength='" + windStrength + '\'' +
                '}';
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDate() {
        return this.date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return this.time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getWeatherDes() {
        return this.weatherDes;
    }

    public void setWeatherDes(String weatherDes) {
        this.weatherDes = weatherDes;
    }

    public int getWeatherCode() {
        return this.weatherCode;
    }

    public void setWeatherCode(int weatherCode) {
        this.weatherCode = weatherCode;
    }

    public double getCurTemperature() {
        return this.curTemperature;
    }

    public void setCurTemperature(double curTemperature) {
        this.curTemperature = curTemperature;
    }

    public double getMinTemperature() {
        return this.minTemperature;
    }

    public void setMinTemperature(double minTemperature) {
        this.minTemperature = minTemperature;
    }

    public double getMaxTemperature() {
        return this.maxTemperature;
    }

    public void setMaxTemperature(double maxTemperature) {
        this.maxTemperature = maxTemperature;
    }

    public String getCity() {
        return this.city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getWindDirection() {
        return this.windDirection;
    }

    public void setWindDirection(String windDirection) {
        this.windDirection = windDirection;
    }

    public String getWindStrength() {
        return this.windStrength;
    }

    public void setWindStrength(String windStrength) {
        this.windStrength = windStrength;
    }

    public CmdType getCmdType() {
        return cmdType;
    }

    public void setCmdType(CmdType cmdType) {
        this.cmdType = cmdType;
    }
}
