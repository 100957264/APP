package com.android.fisewatchlauncher.entity.location;

/**
 * @author mare
 * @Description:TODO
 * @csdnblog http://blog.csdn.net/mare_blue
 * @date 2017/9/4
 * @time 17:14
 */
public class CdmaNeighboringCellInfo extends NeighboringInfo {
    int systemId;
    int lac;
    int cellid;
    int signal;

    public CdmaNeighboringCellInfo(int systemId, int lac, int cellid, int signal) {
        this.systemId = systemId;
        this.lac = lac;
        this.cellid = cellid;
        this.signal = signal;
    }

    @Override
    public String toString() {
        return "CdmaNeighboringCellInfo{" +
                "systemId=" + systemId +
                ", lac=" + lac +
                ", cellid=" + cellid +
                ", signal=" + signal +
                '}';
    }

//    public String format() {
//        return neighboringLac + "," + neighboringCid + "," + neighboringBiss;
//    }

    @Override
    public String format() {
        return systemId + "," + lac + "," + cellid+"," + signal;
    }
}
