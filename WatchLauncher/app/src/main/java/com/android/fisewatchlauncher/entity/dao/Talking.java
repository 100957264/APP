package com.android.fisewatchlauncher.entity.dao;

import com.android.fisewatchlauncher.dao.DaoSession;
import com.android.fisewatchlauncher.dao.TalkingDao;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToMany;

import java.util.List;
import com.android.fisewatchlauncher.dao.WetMessageDao;

/**
 * @author mare
 * @Description:TODO 针对防走丢协议里的对讲功能
 * @csdnblog http://blog.csdn.net/mare_blue
 * @date 2017/10/17
 * @time 20:22
 */
@Entity(active = true)
public class Talking {
    @Id
    public Long id;

    @ToMany(referencedJoinProperty = "talkingId")
    public List<WetMessage> wetMessages;//所有对讲消息

    public void setWetMessages(List<WetMessage> wetMessages) {
        this.wetMessages = wetMessages;
    }

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 139510276)
    private transient TalkingDao myDao;

    @Generated(hash = 1838116685)
    public Talking() {
    }

    @Generated(hash = 855286259)
    public Talking(Long id) {
        this.id = id;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 716276795)
    public synchronized void resetWetMessages() {
        wetMessages = null;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 1185597981)
    public List<WetMessage> getWetMessages() {
        if (wetMessages == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            WetMessageDao targetDao = daoSession.getWetMessageDao();
            List<WetMessage> wetMessagesNew = targetDao._queryTalking_WetMessages(id);
            synchronized (this) {
                if (wetMessages == null) {
                    wetMessages = wetMessagesNew;
                }
            }
        }
        return wetMessages;
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
    @Generated(hash = 765362547)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getTalkingDao() : null;
    }

}
