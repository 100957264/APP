package com.android.fisewatchlauncher.event;

/**
 * @author mare
 * @Description:TODO 首页 碎片里小红点改变事件
 * @csdnblog http://blog.csdn.net/mare_blue
 * @date 2017/11/2
 * @time 10:39
 */
public class HomeBageEvent {
    public String pagerTitle;
    public int bageCount;

    @Override
    public String toString() {
        return "HomeBageEvent{" +
                "pagerTitle='" + pagerTitle + '\'' +
                ", bageCount=" + bageCount +
                '}';
    }
}
