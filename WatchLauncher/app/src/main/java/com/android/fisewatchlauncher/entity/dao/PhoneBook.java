package com.android.fisewatchlauncher.entity.dao;


import com.android.fisewatchlauncher.dao.DaoSession;
import com.android.fisewatchlauncher.dao.MessagePhraseDao;
import com.android.fisewatchlauncher.utils.DaoStringConverter;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToMany;

import java.util.List;
import com.android.fisewatchlauncher.dao.PhoneBookDao;

/**
 * @author mare
 * @Description:
 * @csdnblog http://blog.csdn.net/mare_blue
 * @date 2017/8/10
 * @time 17:43
 */
@Entity
public class PhoneBook extends CenterSettingBase {
    @Id
    private long id;

    public String imei;//主键

    @ToMany(referencedJoinProperty = "message_phrase_id")
    private List<MessagePhrase> phraseSettings;//短语显示设置

    @Convert(columnType = String.class,converter = DaoStringConverter.class)
    private List<String> white_list;//白名单

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 1942644711)
    private transient PhoneBookDao myDao;

    public PhoneBook(String imei) {
        super();
        this.imei = imei;
    }

    public void setPhraseSettings(List<MessagePhrase> phraseSettings) {
        this.phraseSettings = phraseSettings;
    }

    @Generated(hash = 1326829998)
    public PhoneBook(long id, String imei, List<String> white_list) {
        this.id = id;
        this.imei = imei;
        this.white_list = white_list;
    }

    @Generated(hash = 2002744551)
    public PhoneBook() {
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getImei() {
        return this.imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public List<String> getWhite_list() {
        return this.white_list;
    }

    public void setWhite_list(List<String> white_list) {
        this.white_list = white_list;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 1751992148)
    public List<MessagePhrase> getPhraseSettings() {
        if (phraseSettings == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            MessagePhraseDao targetDao = daoSession.getMessagePhraseDao();
            List<MessagePhrase> phraseSettingsNew = targetDao
                    ._queryPhoneBook_PhraseSettings(id);
            synchronized (this) {
                if (phraseSettings == null) {
                    phraseSettings = phraseSettingsNew;
                }
            }
        }
        return phraseSettings;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 2057356998)
    public synchronized void resetPhraseSettings() {
        phraseSettings = null;
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
    @Generated(hash = 921059084)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getPhoneBookDao() : null;
    }

}
