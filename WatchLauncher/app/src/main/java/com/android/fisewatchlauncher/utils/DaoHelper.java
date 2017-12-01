package com.android.fisewatchlauncher.utils;


import android.content.Context;

import com.android.fisewatchlauncher.dao.AlarmInfoDao;
import com.android.fisewatchlauncher.dao.CenterCounterDao;
import com.android.fisewatchlauncher.dao.CenterLocationDao;
import com.android.fisewatchlauncher.dao.CenterSettingsDao;
import com.android.fisewatchlauncher.dao.DaoMaster;
import com.android.fisewatchlauncher.dao.DaoSession;
import com.android.fisewatchlauncher.dao.EaseFriendDao;
import com.android.fisewatchlauncher.dao.FriendDao;
import com.android.fisewatchlauncher.dao.GroupDao;
import com.android.fisewatchlauncher.dao.GroupMemberDao;
import com.android.fisewatchlauncher.dao.MessagePhraseDao;
import com.android.fisewatchlauncher.dao.PhoneBookDao;
import com.android.fisewatchlauncher.dao.PhoneContractorDao;
import com.android.fisewatchlauncher.dao.TalkingDao;
import com.android.fisewatchlauncher.dao.WeatherDao;
import com.android.fisewatchlauncher.dao.WetMessageDao;
import com.android.fisewatchlauncher.dao.WetchatDao;

import org.greenrobot.greendao.database.Database;

/**
 * Created by zhangqie on 2016/3/26.
 */

public class DaoHelper extends DaoMaster.OpenHelper {

    private static DaoMaster daoMaster;
    private static DaoSession daoSession;

    private static String DBNAME = "";

    public DaoHelper(Context context, String name) {
        super(context, name, null);
        DaoHelper.DBNAME = name;
    }

    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {
        super.onUpgrade(db, oldVersion, newVersion);
        LogUtils.i("version", oldVersion + "---先前和更新之后的版本---" + newVersion);
        if (oldVersion < newVersion) {
            LogUtils.i("version", oldVersion + "---先前和更新之后的版本---" + newVersion);
            MigrationHelper.getInstance().migrate(db, AlarmInfoDao.class, CenterCounterDao.class, CenterSettingsDao.class,
                    CenterLocationDao.class, PhoneBookDao.class, MessagePhraseDao.class, PhoneContractorDao.class,
                    FriendDao.class, GroupDao.class, GroupMemberDao.class, TalkingDao.class,
                    WetchatDao.class, WetMessageDao.class,EaseFriendDao.class, WeatherDao.class);
            //更改过的实体类(新增的不用加)   更新UserDao文件 可以添加多个  XXDao.class 文件
//             MigrationHelper.getInstance().migrate(db, UserDao.class,XXDao.class);
        }
    }

    /**
     * 取得DaoMaster
     *
     * @param context
     * @return
     */
    public static DaoMaster getDaoMaster(Context context) {
        if (daoMaster == null) {
            DaoMaster.OpenHelper helper = new DaoMaster.DevOpenHelper(context,
                    DBNAME, null);
            daoMaster = new DaoMaster(helper.getWritableDatabase());
        }
        return daoMaster;
    }

    /**
     * 取得DaoSession
     *
     * @param context
     * @return
     */
    public static DaoSession getDaoSession(Context context) {
        if (daoSession == null) {
            if (daoMaster == null) {
                daoMaster = getDaoMaster(context);
            }
            daoSession = daoMaster.newSession();
        }
        return daoSession;
    }
}
