package com.android.fisewatchlauncher.client;

/**
 * Created by user on 2017/9/8.
 */

public class StickPackageBean {

    public int len;
    public int lenOffset;// 字段开始位置
    public int contentOffset;//整体字段偏移值
    public boolean isSmallPort;//是否是小端
    public String head;
    public String tail;

    public int getLen() {
        return len;
    }

    public void setLen(int len) {
        this.len = len;
    }

    public int getLenOffset() {
        return lenOffset;
    }

    public void setLenOffset(int lenOffset) {
        this.lenOffset = lenOffset;
    }

    public int getContentOffset() {
        return contentOffset;
    }

    public void setContentOffset(int contentOffset) {
        this.contentOffset = contentOffset;
    }

    public boolean isSmallPort() {
        return isSmallPort;
    }

    public void setSmallPort(boolean smallPort) {
        isSmallPort = smallPort;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public String getTail() {
        return tail;
    }

    public void setTail(String tail) {
        this.tail = tail;
    }
}
