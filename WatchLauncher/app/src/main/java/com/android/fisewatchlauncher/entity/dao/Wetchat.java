package com.android.fisewatchlauncher.entity.dao;

import com.android.fisewatchlauncher.dao.BlackFriendDao;
import com.android.fisewatchlauncher.dao.DaoSession;
import com.android.fisewatchlauncher.dao.FriendDao;
import com.android.fisewatchlauncher.dao.GroupDao;
import com.android.fisewatchlauncher.dao.WetchatDao;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToMany;

import java.util.List;

/**
 * @author mare
 * @Description:TODO 针对融云通信的微聊
 * @csdnblog http://blog.csdn.net/mare_blue
 * @date 2017/10/14
 * @time 15:48
 */
@Entity
public class Wetchat {

    @Id
    private String userId;
    private String nickName;
    private String phoneNum;
    @ToMany(referencedJoinProperty = "friendId")
    private List<Friend> friends;
    @ToMany(referencedJoinProperty = "blackId")
    private List<BlackFriend> blackFriends;
    @ToMany(referencedJoinProperty = "groupId")
    private List<Group> wetGroups;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 1948282276)
    private transient WetchatDao myDao;
    @Generated(hash = 1973062499)
    public Wetchat(String userId, String nickName, String phoneNum) {
        this.userId = userId;
        this.nickName = nickName;
        this.phoneNum = phoneNum;
    }
    @Generated(hash = 6687850)
    public Wetchat() {
    }
    public String getUserId() {
        return this.userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getNickName() {
        return this.nickName;
    }
    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
    public String getPhoneNum() {
        return this.phoneNum;
    }
    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }
    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 525919377)
    public List<Friend> getFriends() {
        if (friends == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            FriendDao targetDao = daoSession.getFriendDao();
            List<Friend> friendsNew = targetDao._queryWetchat_Friends(userId);
            synchronized (this) {
                if (friends == null) {
                    friends = friendsNew;
                }
            }
        }
        return friends;
    }
    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 1638260638)
    public synchronized void resetFriends() {
        friends = null;
    }
    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 1169392235)
    public List<BlackFriend> getBlackFriends() {
        if (blackFriends == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            BlackFriendDao targetDao = daoSession.getBlackFriendDao();
            List<BlackFriend> blackFriendsNew = targetDao
                    ._queryWetchat_BlackFriends(userId);
            synchronized (this) {
                if (blackFriends == null) {
                    blackFriends = blackFriendsNew;
                }
            }
        }
        return blackFriends;
    }
    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 664132438)
    public synchronized void resetBlackFriends() {
        blackFriends = null;
    }
    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 1492914724)
    public List<Group> getWetGroups() {
        if (wetGroups == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            GroupDao targetDao = daoSession.getGroupDao();
            List<Group> wetGroupsNew = targetDao._queryWetchat_WetGroups(userId);
            synchronized (this) {
                if (wetGroups == null) {
                    wetGroups = wetGroupsNew;
                }
            }
        }
        return wetGroups;
    }
    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 16832320)
    public synchronized void resetWetGroups() {
        wetGroups = null;
    }
    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }
    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }
    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }
    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 705089733)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getWetchatDao() : null;
    }

}
