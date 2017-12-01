package com.android.fisewatchlauncher.holder;

import java.io.Serializable;

/**
 * @author mare
 * @Description:TODO
 * @csdnblog http://blog.csdn.net/mare_blue
 * @date 2017/9/4 0004
 * @time 04:48
 */

public class ActivityTarget implements Serializable {
        private String targetPackage;
        private String targetActivityName;
        private String targetAction;
        private int flag;

        public ActivityTarget(String targetPackage, String targetActivityName, String targetAction, int flag) {
            this.targetPackage = targetPackage;
            this.targetActivityName = targetActivityName;
            this.targetAction = targetAction;
            this.flag = flag;
        }

    public String getTargetPackage() {
        return targetPackage;
    }

    public void setTargetPackage(String targetPackage) {
        this.targetPackage = targetPackage;
    }

    public String getTargetActivityName() {
        return targetActivityName;
    }

    public void setTargetActivityName(String targetActivityName) {
        this.targetActivityName = targetActivityName;
    }

    public String getTargetAction() {
        return targetAction;
    }

    public void setTargetAction(String targetAction) {
        this.targetAction = targetAction;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    @Override
        public String toString() {
            return "Target{" +
                    "targetPackage='" + targetPackage + '\'' +
                    ", targetActivityName='" + targetActivityName + '\'' +
                    ", targetAction='" + targetAction + '\'' +
                    ", flag=" + flag +
                    '}';
    }
}
