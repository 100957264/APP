package com.android.fisewatchlauncher.entity.location;

import java.util.List;

/**
 * @author mare
 * @Description:TODO 基站信息结构体
 * @csdnblog http://blog.csdn.net/mare_blue
 * @date 2017/9/4
 * @time 9:48
 */
public class SCell extends NeighboringInfo {
    public int baseStationCount;//上报基站个数,0 表示不上报基站信息
    public long GSMDelay = 1;//GSM 时延
    public String mcc;//460
    public String mnc;//00
    public int lac;//获取gsm网络编号
    public int cid;//获取gsm基站识别标号

    public List<GSMNeighboringCellInfo> neighboringCellInfos;

    public SCell(String mcc, String mnc, int lac, int cid) {
        this.mcc = mcc;
        this.mnc = mnc;
        this.lac = lac;
        this.cid = cid;
    }

    public SCell(String mcc, String mnc, int lac, int cid, List<GSMNeighboringCellInfo> neighboringCellInfos) {
        this.mcc = mcc;
        this.mnc = mnc;
        this.lac = lac;
        this.cid = cid;
        this.neighboringCellInfos = neighboringCellInfos;
    }

    public List<GSMNeighboringCellInfo> getNeighboringCellInfos() {
        return neighboringCellInfos;
    }

    public void setNeighboringCellInfos(List<GSMNeighboringCellInfo> neighboringCellInfos) {
        this.neighboringCellInfos = neighboringCellInfos;
    }

    public int getBaseStationCount() {
        return baseStationCount;
    }

    public void setBaseStationCount(int baseStationCount) {
        this.baseStationCount = baseStationCount;
    }

    public long getGSMDelay() {
        return GSMDelay;
    }

    public void setGSMDelay(long GSMDelay) {
        this.GSMDelay = GSMDelay;
    }

    public String getMcc() {
        return mcc;
    }

    public void setMcc(String mcc) {
        this.mcc = mcc;
    }

    public String getMnc() {
        return mnc;
    }

    public void setMnc(String mnc) {
        this.mnc = mnc;
    }

    public int getLac() {
        return lac;
    }

    public void setLac(int lac) {
        this.lac = lac;
    }

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    @Override
    public String toString() {
        return "SCell{" +
                "baseStationCount=" + baseStationCount +
                ", GSMDelay=" + GSMDelay +
                ", mcc=" + mcc +
                ", mnc=" + mnc +
                ", lac=" + lac +
                ", cid=" + cid +
                ", neighboringCellInfos=" + neighboringCellInfos +
                '}';
    }

    public String format() {
        String header = baseStationCount + "," + GSMDelay + "," + mcc + "," + mnc;
        if (baseStationCount == 0) return header;
        StringBuilder sb = new StringBuilder(header);
        //sb.append("," + lac + "," + cid);//功能机上传不需要gsm网络编号D和gsm基站识别标号
        String neiborStr;
        for (GSMNeighboringCellInfo neibor : neighboringCellInfos) {
            neiborStr = neibor.format();
            sb.append(",").append(neiborStr);
        }
        return sb.toString();
    }
}
