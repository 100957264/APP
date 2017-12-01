package com.android.fisewatchlauncher.entity.view;

/**
 * Created by fanyang on 2017/8/8.
 */

public class TagShow {
    private String tagName;
    private boolean isChecked;

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public TagShow() {
    }

    public TagShow(String tagName) {
        this.tagName = tagName;
    }

    public TagShow(String tagName, boolean isChecked) {
        this.tagName = tagName;
        this.isChecked = isChecked;
    }
}
