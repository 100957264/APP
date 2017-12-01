package com.android.fisewatchlauncher.manager;

import android.database.sqlite.SQLiteDatabase;

import com.android.fisewatchlauncher.KApplication;
import com.android.fisewatchlauncher.constant.DBConstant;
import com.android.fisewatchlauncher.dao.DaoMaster;
import com.android.fisewatchlauncher.dao.DaoSession;
import com.android.fisewatchlauncher.utils.DaoHelper;
import com.android.fisewatchlauncher.utils.GreenDaoUtils;


/**
 * @author mare
 * @Description:
 * @csdnblog http://blog.csdn.net/mare_blue
 * @date 2017/8/11
 * @time 16:13
 */
public class DBManager {

    private DaoMaster mDaoMaster;
    private DaoSession mDaoSession;
    private DaoHelper mDaoHelper;
    private SQLiteDatabase db;

    private DBManager() {
    }

    private static class SingletonHolder {
        private static final DBManager INSTANCE = new DBManager();
    }

    public static DBManager instance() {
        return SingletonHolder.INSTANCE;
    }

    public void initDao() {
        if (null == mDaoHelper) {
            mDaoHelper = new DaoHelper(new GreenDaoUtils(KApplication.sContext), DBConstant.DBNAME);
        }
        db = getDb();
        // 通过 DaoMaster 的内部类 DevOpenHelper，你可以得到一个便利的 SQLiteOpenHelper 对象。
        // 可能你已经注意到了，你并不需要去编写「CREATE TABLE」这样的 SQL 语句，因为 greenDAO 已经帮你做了。
        // 注意：默认的 DaoMaster.DevOpenHelper 会在数据库升级时，删除所有的表，意味着这将导致数据的丢失。
        // 所以，在正式的项目中，你还应该做一层封装，来实现数据库的安全升级。

        // 注意：该数据库连接属于 DaoMaster，所以多个 Session 指的是相同的数据库连接。
        if (null == mDaoMaster) {
            mDaoMaster = new DaoMaster(db);
        }
        if (null == mDaoSession) {
            mDaoSession = mDaoMaster.newSession();
        }
    }

    public DaoSession getDaoSession() {
        if (null == mDaoMaster) {
            mDaoMaster = new DaoMaster(getDb());//avoid NPE
        }
        if (null == mDaoSession) {
            mDaoSession = mDaoMaster.newSession();
        }
        return mDaoSession;
    }

    public SQLiteDatabase getDb() {
        if (null == mDaoHelper) {
            mDaoHelper = new DaoHelper(new GreenDaoUtils(KApplication.sContext), DBConstant.DBNAME);
        }
        if (null == db) {
            db = mDaoHelper.getWritableDatabase();
        }
        return db;
    }
}
