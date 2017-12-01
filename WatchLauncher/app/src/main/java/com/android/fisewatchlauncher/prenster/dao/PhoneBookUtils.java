package com.android.fisewatchlauncher.prenster.dao;

import android.content.Context;
import android.database.sqlite.SQLiteException;
import android.text.TextUtils;

import com.android.fisewatchlauncher.client.GlobalSettings;
import com.android.fisewatchlauncher.dao.CenterSettingsDao;
import com.android.fisewatchlauncher.dao.MessagePhraseDao;
import com.android.fisewatchlauncher.dao.PhoneBookDao;
import com.android.fisewatchlauncher.entity.dao.MessagePhrase;
import com.android.fisewatchlauncher.entity.dao.PhoneBook;
import com.android.fisewatchlauncher.entity.dao.PhoneContractor;
import com.android.fisewatchlauncher.function.DaoFlagUtils;
import com.android.fisewatchlauncher.manager.DBManager;
import com.android.fisewatchlauncher.utils.LogUtils;
import com.android.fisewatchlauncher.utils.RegexUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author mare
 * @Description:
 * @csdnblog http://blog.csdn.net/mare_blue
 * @date 2017/8/11
 * @time 17:12
 */
public class PhoneBookUtils implements GreenDaoChargeRecordImpl<PhoneBook> {

    private PhoneBookDao centerDao;

    private MessagePhraseDao phraseDao;

    private PhoneBookUtils() {
    }

    private static class SingletonHolder {
        private static final PhoneBookUtils INSTANCE = new PhoneBookUtils();
    }

    public static PhoneBookUtils instance() {
        return SingletonHolder.INSTANCE;
    }

    private PhoneBookDao getCenterDao() {
        if (null == centerDao) {
            centerDao = DBManager.instance().getDaoSession().getPhoneBookDao();
        }
        return centerDao;
    }

    private MessagePhraseDao getPhraseDao() {
        if (null == phraseDao) {
            phraseDao = DBManager.instance().getDaoSession().getMessagePhraseDao();
        }
        return phraseDao;
    }

    @Override
    public long insert(PhoneBook data) {
        long add = centerDao.insert(data);
        return add;
    }

    /****
     * 指定imei修改信息
     * @param data
     */
    @Override
    public boolean update(PhoneBook data) {
        String imei = data.getImei();
        PhoneBook userQuery = null;
        PhoneBookDao dao = getCenterDao();
        long insertUsersID = -1;
        String className = data.getClass().getSimpleName();
        try {
            userQuery = dao.queryBuilder().where(PhoneBookDao.Properties.Imei.eq(imei)).build().unique();
        } catch (SQLiteException e) {
            LogUtils.e("查询 " + className + "失败");
        }
        if (null != userQuery) {
            data.setId(userQuery.getId());
            LogUtils.d("开始更新" + className + "数据...");
            insertUsersID = dao.insertOrReplace(data);
        } else {
            LogUtils.d("没找到" + className + "的所在数据 开始插入");
            insertUsersID = dao.insertOrReplace(data);
        }
        boolean isSuccess = insertUsersID >= 0;
        LogUtils.i("更新 PhoneBook" + "数据" + DaoFlagUtils.insertSuccessOr(isSuccess));
        return isSuccess;
    }

    /***
     * 删除全部
     */
    @Override
    public void deleteAll() {
        getCenterDao().deleteAll();
    }

    /***
     * 条件删除
     * @param id
     */
    @Override
    public void deleteWhere(long id) {
        getCenterDao().deleteByKey(id);
    }

    /***
     * 查询全部
     * @return
     */
    @Override
    public List<PhoneBook> selectAll() {
        List<PhoneBook> list = getCenterDao().loadAll();
        return null != list && list.size() > 0 ? list : null;
    }

    /***
     * 模糊查询
     * @param data
     * @return
     */
    @Override
    public List<PhoneBook> selectWhere(PhoneBook data) {
        String imei = data.getImei();
        List<PhoneBook> list = getCenterDao().queryBuilder().where(
                PhoneBookDao.Properties.Imei.like(imei)).build().list();
        return null != list && list.size() > 0 ? list : null;
    }

    /***
     * 唯一查询
     * @param name
     * @return
     */
    @Override
    public PhoneBook seelctWhrer(String name) {
        PhoneBook user = getCenterDao().queryBuilder().where(
                PhoneBookDao.Properties.Imei.eq(name)).build().unique();
        return null != user ? user : null;
    }

    /***
     * Id查询
     * @param id
     * @return
     */
    @Override
    public List<PhoneBook> selectWhrer(long id) {
        List<PhoneBook> users = getCenterDao().queryBuilder().where(
                PhoneBookDao.Properties.Id.le(id)).build().list();
        return null != users && users.size() > 0 ? users : null;
    }

    /**
     * 更新短消息
     *
     * @param settings
     * @return
     */
    public boolean updatePhraseMsg(PhoneBook settings) {
        String sourceFrom;
        List<MessagePhrase> phrases = settings.getPhraseSettings();
        String imei = settings.getImei();
        PhoneBook userQuery = null;
        PhoneBookDao dao = getCenterDao();
        long insertUsersID = -1;
        String className = settings.getClass().getSimpleName();
        try {
            userQuery = dao.queryBuilder().where(PhoneBookDao.Properties.Imei.eq(imei)).build().unique();
        } catch (SQLiteException e) {
            LogUtils.e("查询 " + className + "失败");
        }
        if (null != userQuery) {
            userQuery.setPhraseSettings(phrases);
            LogUtils.d("开始更新" + className + "数据...");
            insertUsersID = dao.insertOrReplace(userQuery);
        } else {
            LogUtils.d("没找到" + className + "的所在数据 开始插入");
            insertUsersID = dao.insertOrReplace(settings);
        }
        boolean isSuccess = insertUsersID >= 0;
        return isSuccess;
    }

    public List<MessagePhrase> queryPhraseMsg(String sourceFrom) {
        List<MessagePhrase> userQuery = null;
        MessagePhraseDao dao = getPhraseDao();
        String className = PhoneBook.class.getSimpleName();
        try {
            userQuery = dao.queryBuilder().where(MessagePhraseDao.Properties.Message_sourceFrom.eq(sourceFrom)).list();
        } catch (SQLiteException e) {
            LogUtils.e("查询 " + className + "失败");
        }
        boolean isSuccess = null != userQuery && userQuery.size() > 0;
        String alertMsg = isSuccess ? "找到了" + userQuery.size() + "条数据" : "没找到" + className + "的数据";
        LogUtils.d(alertMsg);
        return userQuery;
    }

    /**
     * 清空中心设置信息
     *
     * @param ctx
     * @return
     */
    public void clearCenterSettings(Context ctx) {
        PhoneBookDao dao = getCenterDao();
        dao.deleteAll();
    }

    /**
     * 解析PHB PHB2
     *
     * @param content
     * @return
     */
    public List<PhoneContractor> parsePhoneBookValues(String content) {
        String[] fields = TextUtils.split(content,GlobalSettings.MSG_CONTENT_SEPERATOR);
        int pair = fields.length / 2;
        List<PhoneContractor> contractors = new ArrayList<>();
        boolean isNumNull;
        String num;
        String name;
        PhoneContractor contractor;
        for (int i = 0; i < pair; i++) {
            num = fields[i];
            name = fields[i + 1];
            isNumNull = TextUtils.isEmpty(num);
            if (isNumNull) {
                continue;
            }
            contractor = new PhoneContractor(name, num);
            contractor.setImei(GlobalSettings.instance().getImei());
            contractors.add(contractor);
        }
        return contractors;
    }

    /**
     * 设置白名单信息
     *
     * @param whiteListStr
     * @return
     */
    public boolean updateWhiteList(String whiteListStr) {
        if (TextUtils.isEmpty(whiteListStr)) {
            return false;
        }
        String imei = GlobalSettings.instance().getImei();
        if (TextUtils.isEmpty(imei)) {
            return false;
        }
        int count = RegexUtils.getMatchsCount(GlobalSettings.MSG_CONTENT_SEPERATOR, whiteListStr);
        if (count < 4) {
            LogUtils.e("白名单" + "格式不对 必须五个++");
            return false;
        }
        String[] whiteArray = whiteListStr.split(GlobalSettings.MSG_CONTENT_SEPERATOR);
        List<String> whiteList = Arrays.asList(whiteArray);
        PhoneBookDao dao = getCenterDao();
        PhoneBook userQuery = null;
        try {
            userQuery = dao.queryBuilder().where(PhoneBookDao.Properties.Imei.eq(imei)).build().unique();
        } catch (SQLiteException e) {
            LogUtils.e("查询 " + CenterSettingsDao.class.getSimpleName() + "失败");
        }
        long insertUsersID = -1;
        if (null != userQuery) {
            LogUtils.d("开始更新 设置白名单" + "数据...");
        } else {
            LogUtils.d("没找到 设置白名单 " + "所在数据 开始插入");
            userQuery = new PhoneBook(imei);
        }
        userQuery.setWhite_list(whiteList);
        insertUsersID = dao.insertOrReplace(userQuery);
        boolean isSuccess = insertUsersID >= 0;
        LogUtils.i("更新 设置白名单" + "数据到" + getCenterDao().getClass().getSimpleName() + DaoFlagUtils.insertSuccessOr(isSuccess));
        return isSuccess;
    }

}
