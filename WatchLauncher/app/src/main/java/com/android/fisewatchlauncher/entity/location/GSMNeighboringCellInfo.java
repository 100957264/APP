package com.android.fisewatchlauncher.entity.location;

/**
 * @author mare
 * @Description:TODO
 * @csdnblog http://blog.csdn.net/mare_blue
 * @date 2017/9/4
 * @time 17:14
 */
public class GSMNeighboringCellInfo extends NeighboringInfo {
    public int neighboringLac;//基站位置区域码
    public int neighboringCid;
    public int neighboringBiss = 0;

    public GSMNeighboringCellInfo(int neighboringLac, int neighboringCid, int neighboringBiss) {
        this.neighboringLac = neighboringLac;
        this.neighboringCid = neighboringCid;
        this.neighboringBiss = neighboringBiss;
    }

    @Override
    public String toString() {
        return "GSMNeighboringCellInfo{" +
                "neighboringLac=" + neighboringLac +
                ", neighboringCid=" + neighboringCid +
                ", neighboringBiss=" + neighboringBiss +
                '}';
    }

    @Override
    public String format() {
        return neighboringLac + "," + neighboringCid + "," + neighboringBiss;
    }
}
