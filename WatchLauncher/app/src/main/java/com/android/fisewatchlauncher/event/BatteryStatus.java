package com.android.fisewatchlauncher.event;

/**
 * Created by sean on 8/26/16.
 */
public class BatteryStatus {
    public long timestamp;
    public int health, level, plugged, scale, status, temperature, voltage;
    public boolean present;
    public String technology;

    @Override
    public String toString() {
        return "BatteryStatus{" +
                "timestamp=" + timestamp +
                ", health=" + health +
                ", level=" + level +
                ", plugged=" + plugged +
                ", scale=" + scale +
                ", status=" + status +
                ", temperature=" + temperature +
                ", voltage=" + voltage +
                ", present=" + present +
                ", technology='" + technology + '\'' +
                '}';
    }
}
