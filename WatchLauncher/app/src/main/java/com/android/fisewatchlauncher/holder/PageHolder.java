package com.android.fisewatchlauncher.holder;

import java.io.Serializable;

/**
 * Created by mare on 2017/8/27 0027.
 */

public class PageHolder implements Serializable {
    private int iconId;
    private int bgId;
    private int pageTitle;
    private ActivityTarget target;

    public PageHolder(int iconId, int pageTitle, ActivityTarget target) {
        this.iconId = iconId;
        this.bgId = bgId;
        this.pageTitle = pageTitle;
        this.target = target;
    }

    public int getIconId() {
        return iconId;
    }

    public void setIconId(int iconId) {
        this.iconId = iconId;
    }

    public int getBgId() {
        return bgId;
    }

    public void setBgId(int bgId) {
        this.bgId = bgId;
    }

    public int getPageTitle() {
        return pageTitle;
    }

    public void setPageTitle(int pageTitle) {
        this.pageTitle = pageTitle;
    }

    public ActivityTarget getTarget() {
        return target;
    }

    public void setTarget(ActivityTarget target) {
        this.target = target;
    }

    @Override
    public String toString() {
        return "PageHolder{" +
                "iconId=" + iconId +
                ", bgId=" + bgId +
                ", pageTitle=" + pageTitle +
                ", target=" + target.toString() +
                '}';
    }
}
