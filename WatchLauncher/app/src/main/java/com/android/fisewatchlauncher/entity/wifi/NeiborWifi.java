package com.android.fisewatchlauncher.entity.wifi;

import java.util.List;

/**
 * @author mare
 * @Description:TODO
 * @csdnblog http://blog.csdn.net/mare_blue
 * @date 2017/9/4
 * @time 18:51
 */
public class NeiborWifi {
    public int neiborCount;
    public List<NeiborWifiInfo> neiborWifiInfos;

    public NeiborWifi(List<NeiborWifiInfo> neiborWifiInfos) {
        this(neiborWifiInfos.size(), neiborWifiInfos);
    }

    public NeiborWifi(int neiborCount, List<NeiborWifiInfo> neiborWifiInfos) {
        this.neiborCount = neiborCount;
        this.neiborWifiInfos = neiborWifiInfos;
    }

    @Override
    public String toString() {
        return "NeiborWifi{" +
                "neiborCount=" + neiborCount +
                ", neiborWifiInfos=" + neiborWifiInfos.toString() +
                '}';
    }

    public String format(){
        StringBuilder sb = new StringBuilder();
        sb.append(neiborCount);
        if (null == neiborWifiInfos || neiborWifiInfos.isEmpty())return sb.toString();
        for (NeiborWifiInfo neiborWifiInfo :neiborWifiInfos){
            sb.append(",").append(neiborWifiInfo.format());
        }
        return sb.toString();
    }
}
