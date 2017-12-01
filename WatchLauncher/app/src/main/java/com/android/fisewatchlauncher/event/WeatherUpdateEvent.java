package com.android.fisewatchlauncher.event;

import com.android.fisewatchlauncher.entity.weather.Weather;

/**
 * @author mare
 * @Description:
 * @csdnblog http://blog.csdn.net/mare_blue
 * @date 2017/8/26
 * @time 15:07
 */
public class WeatherUpdateEvent {

    public Weather weather;

    public WeatherUpdateEvent(Weather weather) {
        this.weather = weather;
    }

    @Override
    public String toString() {
        return "WeatherUpdateEvent{" +
                "weather=" + weather.toString() +
                '}';
    }
}
