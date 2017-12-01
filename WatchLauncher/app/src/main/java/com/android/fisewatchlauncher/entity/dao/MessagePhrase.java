package com.android.fisewatchlauncher.entity.dao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * @author mare
 * @Description:短信息 采用Unicode编码下发给终端
 * @csdnblog http://blog.csdn.net/mare_blue
 * @date 2017/8/11
 * @time 9:59
 */
@Entity
public class MessagePhrase extends CenterSettingBase {
    @Id(autoincrement = true)
    private Long id;

    private long message_phrase_id;//sourceFrom作为外键

    private String date;

    private long time;//接收信息时间

    private String message_sourceFrom;//发信人

    private String content;//短信内容

    @Generated(hash = 339337291)
    public MessagePhrase() {
    }

    public MessagePhrase(long phrase_id) {
        super();
        this.message_phrase_id = phrase_id;
    }

    @Generated(hash = 1428949551)
    public MessagePhrase(Long id, long message_phrase_id, String date, long time,
            String message_sourceFrom, String content) {
        this.id = id;
        this.message_phrase_id = message_phrase_id;
        this.date = date;
        this.time = time;
        this.message_sourceFrom = message_sourceFrom;
        this.content = content;
    }

    public long getTime() {
        return this.time;
    }

    public void setTime() {
        this.time = System.currentTimeMillis();
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDate() {
        return this.date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMessage_sourceFrom() {
        return this.message_sourceFrom;
    }

    public void setMessage_sourceFrom(String message_sourceFrom) {
        this.message_sourceFrom = message_sourceFrom;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getMessage_phrase_id() {
        return this.message_phrase_id;
    }

    public void setMessage_phrase_id(long message_phrase_id) {
        this.message_phrase_id = message_phrase_id;
    }

    public void setTime(long time) {
        this.time = time;
    }

}