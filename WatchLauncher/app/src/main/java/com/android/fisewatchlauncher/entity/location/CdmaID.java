package com.android.fisewatchlauncher.entity.location;

/**
 * @author mare
 * @Description:
 * @csdnblog http://blog.csdn.net/mare_blue
 * @date 2017/9/4
 * @time 10:58
 */
public class CdmaID extends NeighboringInfo {
    public int sid;
    public int nid;
    public int bid;

    public CdmaID(int sid, int nid, int bid) {
        this.sid = sid;
        this.nid = nid;
        this.bid = bid;
    }

    @Override
    public String toString() {
        return "CdmaID{" +
                "sid=" + sid +
                ", nid=" + nid +
                ", bid=" + bid +
                '}';
    }

    public String format(){
        return sid + "," + nid + "," + bid;
    }
}
