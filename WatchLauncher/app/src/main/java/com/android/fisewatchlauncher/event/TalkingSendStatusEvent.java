package com.android.fisewatchlauncher.event;

import com.android.fisewatchlauncher.entity.dao.WetMessage;

/**
 * @author mare
 * @Description:TODO 根据对讲路径更新发送状态
 * @csdnblog http://blog.csdn.net/mare_blue
 * @date 2017/10/23
 * @time 20:09
 */
public class TalkingSendStatusEvent {
    public String audiopath;
    public WetMessage.SendStatus sendStatus;

    public TalkingSendStatusEvent(String audiopath, WetMessage.SendStatus sendStatus) {
        this.audiopath = audiopath;
        this.sendStatus = sendStatus;
    }

    @Override
    public String toString() {
        return "TalkingSendStatusEvent{" +
                "audiopath='" + audiopath + '\'' +
                ", sendStatus=" + sendStatus.toString() +
                '}';
    }
}
