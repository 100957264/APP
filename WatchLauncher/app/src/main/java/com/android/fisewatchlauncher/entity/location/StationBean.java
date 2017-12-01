package com.android.fisewatchlauncher.entity.location;

import com.android.fisewatchlauncher.entity.wifi.NeiborWifi;

/**
 * @author mare
 * @Description:TODO 基站信息(GSM和电信)
 * @csdnblog http://blog.csdn.net/mare_blue
 * @date 2017/11/13
 * @time 20:00
 */
public class StationBean extends NeighboringInfo {
    public int mLocType;
    public String mDate;
    public String mTime;
    public String isGps;//A:定位 V:未定位
    public double mLatitude;//DD.DDDDDD
    public String mLatitudeTpe;//N 表示北纬,S 表示南纬.
    public double mLongitude;
    public String mLongitudeType;//E 表示东经,W 表示西经
    public float mSpeed;//公里/小时
    public float mDerect;//方向在 152 度
    public double mAltitude;//单位为米
    public int mSatelliteNumber;//表明 GPS 卫星个数
    public int mGSMSignalStrength;  //表示当前 GSM 信号强度(0-100)
    public int mBatteryLevel;//表示当前电量等级百分比
    public long mStepCount;//计步数
    public long mTurnOverCount;//翻滚次数
    public TerminalState mTerminalState;//终端状态
    public NeiborWifi neiborWifi;
    public CdmaID mCdmaId;
    public SCell mSCell;
    public boolean isGsm;

    public String format() {
        StringBuilder sb = new StringBuilder();
        String partial = mDate + "," + mTime +
                "," + isGps +
                "," + mLatitude +
                "," + mLatitudeTpe +
                "," + mLongitude +
                "," + mLongitudeType +
                "," + mSpeed +
                "," + mDerect +
                "," + mAltitude +
                "," + mSatelliteNumber +
                "," + mGSMSignalStrength +
                "," + mBatteryLevel +
                "," + mStepCount +
                "," + mTurnOverCount;
        sb.append(partial).append(",");
        if (null == mTerminalState) {
            sb.append("00000010");
        } else {
            sb.append(mTerminalState.format());
        }
        sb.append(",").append(isGsm ? mSCell.format() : mCdmaId.format())
                .append(",");
        if (null == neiborWifi) {
            sb.append(0);
        } else {
            sb.append(neiborWifi.format());
        }
        return sb.toString();
    }
}
