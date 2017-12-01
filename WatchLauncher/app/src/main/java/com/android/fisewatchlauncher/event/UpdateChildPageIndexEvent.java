package com.android.fisewatchlauncher.event;

/**
 * @author mare
 * @Description:TODO
 * @csdnblog http://blog.csdn.net/mare_blue
 * @date 2017/9/4 0004
 * @time 01:09
 */

public class UpdateChildPageIndexEvent {
    public int toPage;
    public boolean hasAnim;

    public UpdateChildPageIndexEvent(int toPage, boolean hasAnim) {
        this.toPage = toPage;
        this.hasAnim = hasAnim;
    }
}
