package com.android.fisewatchlauncher.event;

/**
 * @author mare
 * @Description:TODO 白名单设置更新事件
 * @csdnblog http://blog.csdn.net/mare_blue
 * @date 2017/9/13
 * @time 10:15
 */
public class WhiteListEvent {

    public enum Type {
        WHITELIST1, WHITELIST2
    }

    public Type type;
    public String whiteListStr;

    public WhiteListEvent(Type type, String whiteListStr) {
        this.type = type;
        this.whiteListStr = whiteListStr;
    }

    @Override
    public String toString() {
        return "WhiteListEvent{" +
                "type=" + type.toString() +
                ", whiteListStr='" + whiteListStr + '\'' +
                '}';
    }
}
