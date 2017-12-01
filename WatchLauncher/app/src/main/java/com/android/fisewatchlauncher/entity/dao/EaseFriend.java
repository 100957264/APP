package com.android.fisewatchlauncher.entity.dao;

import android.os.Parcel;
import android.os.Parcelable;

import com.android.fisewatchlauncher.dao.DaoSession;
import com.android.fisewatchlauncher.dao.EaseFriendDao;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * @author mare
 * @Description:TODO
 * @csdnblog http://blog.csdn.net/mare_blue
 * @date 2017/11/10
 * @time 18:31
 */
@Entity(active = true)
public class EaseFriend implements Parcelable {
    @Id
    public Long id;
    private String friendId;
    private String nick;
    private String phone;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 811778869)
    private transient EaseFriendDao myDao;

    public EaseFriend(String friendId, String nick, String phone) {
        this.friendId = friendId;
        this.nick = nick;
        this.phone = phone;
    }

    @Generated(hash = 678128948)
    public EaseFriend(Long id, String friendId, String nick, String phone) {
        this.id = id;
        this.friendId = friendId;
        this.nick = nick;
        this.phone = phone;
    }

    @Generated(hash = 1450980959)
    public EaseFriend() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "EaseFriend{" +
                "id=" + id +
                ", friendId='" + friendId + '\'' +
                ", nick='" + nick + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }

    public String getFriendId() {
        return this.friendId;
    }

    public void setFriendId(String friendId) {
        this.friendId = friendId;
    }

    public String getNick() {
        return this.nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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
    @Generated(hash = 668637026)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getEaseFriendDao() : null;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(friendId);
        dest.writeString(nick);
        dest.writeString(phone);
    }

    public static final Parcelable.Creator<EaseFriend> CREATOR = new Creator<EaseFriend>() {

        @Override
        public EaseFriend createFromParcel(Parcel source) {
            return new EaseFriend(source.readLong(), source.readString(), source.readString(), source.readString());
        }

        @Override
        public EaseFriend[] newArray(int size) {
            return new EaseFriend[size];
        }
    };
}
