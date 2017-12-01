package com.android.fisewatchlauncher.entity.dao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * @author mare
 * @Description:TODO
 * @csdnblog http://blog.csdn.net/mare_blue
 * @date 2017/10/14
 * @time 15:50
 */
@Entity
public class BlackFriend {
    @Id
    public long id;
    public String blackId;
    public String nickName;
    private String status;
    @Generated(hash = 1947268530)
    public BlackFriend(long id, String blackId, String nickName, String status) {
        this.id = id;
        this.blackId = blackId;
        this.nickName = nickName;
        this.status = status;
    }
    @Generated(hash = 652845065)
    public BlackFriend() {
    }
    public long getId() {
        return this.id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getBlackId() {
        return this.blackId;
    }
    public void setBlackId(String blackId) {
        this.blackId = blackId;
    }
    public String getNickName() {
        return this.nickName;
    }
    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
    public String getStatus() {
        return this.status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
}
