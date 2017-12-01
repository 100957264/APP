package com.android.fisewatchlauncher.prenster.dao;

import android.database.sqlite.SQLiteException;
import android.text.TextUtils;

import com.android.fisewatchlauncher.client.GlobalSettings;
import com.android.fisewatchlauncher.constant.DBConstant;
import com.android.fisewatchlauncher.dao.PhoneContractorDao;
import com.android.fisewatchlauncher.entity.dao.PhoneContractor;
import com.android.fisewatchlauncher.function.DaoFlagUtils;
import com.android.fisewatchlauncher.manager.DBManager;
import com.android.fisewatchlauncher.utils.LogUtils;

import java.util.List;

/**
 * @author mare
 * @Description:TODO
 * @csdnblog http://blog.csdn.net/mare_blue
 * @date 2017/9/5
 * @time 18:08
 */
public class PhoneContactorDaoUtils implements GreenDaoChargeRecordImpl<PhoneContractor> {
    private PhoneContactorDaoUtils() {
    }

    private static class SingletonHolder {
        private static final PhoneContactorDaoUtils INSTANCE = new PhoneContactorDaoUtils();
    }

    public static PhoneContactorDaoUtils instance() {
        return SingletonHolder.INSTANCE;
    }

    private PhoneContractorDao contractorDao;

    private PhoneContractorDao getDao() {
        if (null == contractorDao) {
            contractorDao = DBManager.instance().getDaoSession().getPhoneContractorDao();
        }
        return contractorDao;
    }

    @Override
    public long insert(PhoneContractor data) {
        long add = getDao().insert(data);
        return add;
    }

    /***
     * 通过city 更新天气
     * @param data
     * @return
     */
    @Override
    public boolean update(PhoneContractor data) {
        return false;
    }

    /****
     *更新所有电话本联系人
     * @param contractors
     */
    public boolean updateAllPhoneContactor(final List<PhoneContractor> contractors) {
        String imei = GlobalSettings.instance().getImei();
        if (TextUtils.isEmpty(imei) || null == contractors || contractors.isEmpty()) return false;
        final PhoneContractorDao dao = getDao();
        try {
            dao.getSession().runInTx(new Runnable() {
                @Override
                public void run() {
                    PhoneContractor queryContactor;
                    String toInsertNum;
                    for (PhoneContractor toInsert : contractors) {
                        toInsertNum = toInsert.getNum();
                        if (TextUtils.isEmpty(toInsertNum)) {
                            continue;//跳过空的号码
                        }
                        queryContactor = dao.queryBuilder().where(PhoneContractorDao.Properties.Num.eq(toInsertNum)).unique();
                        if (null != queryContactor) {
                            toInsert.setId(queryContactor.getId());
                            toInsert.setNum(toInsertNum);
                            toInsert.setName(toInsert.getName());
                            toInsert.setAvatar(toInsert.getAvatar());
                        } else {
                            toInsert.setId(System.currentTimeMillis());
                        }

                        long insertUsersID = dao.insertOrReplace(toInsert);
                        boolean isSuccess = insertUsersID >= 0;
                        LogUtils.i("更新 id= " + toInsert.getId() + " 的数据" + DaoFlagUtils.insertSuccessOr(isSuccess));
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.e(" xxxxxx " + e);
        }
        return true;
    }

    /***
     * 更新前5条数据
     * @param contractors
     */
    public void updateBottom5Contactor(final List<PhoneContractor> contractors) {
        if (contractors == null || contractors.isEmpty()) {
            return;
        }
        insertContactorsOffset(contractors, 0);
    }

    /***
     * 更新后5条数据
     * @param contractors
     */
    public void updateTop5Contactor(final List<PhoneContractor> contractors) {
        if (contractors == null || contractors.isEmpty()) {
            return;
        }
        final int start = 5;

        insertContactorsOffset(contractors, start);
    }

    /**
     * 从指定ID开始插入
     *
     * @param contractors
     * @param start
     */
    private void insertContactorsOffset(final List<PhoneContractor> contractors, final int start) {
        final PhoneContractorDao dao = getDao();
        dao.getSession().runInTx(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < contractors.size(); i++) {
                    PhoneContractor contractor = contractors.get(i);
                    contractor.setId((long) (i + start));
                    long insertUsersID = dao.insertOrReplace(contractor);
                    boolean isSuccess = insertUsersID >= 0;
                    LogUtils.i("更新 id= " + contractor.getId() + " 的数据" + DaoFlagUtils.insertSuccessOr(isSuccess));
                }
            }
        });
    }

    /**
     * 分页显示信息
     *
     * @param typeid   信息类别
     * @param pageNum  当前页数
     * @param pageSize 每页显示数
     * @return 信息列表
     */
    public List<PhoneContractor> getInfosBypageSize(long typeid, int pageNum, int pageSize) {
        PhoneContractorDao dao = getDao();
        return dao.queryBuilder().where(PhoneContractorDao.Properties.Id.eq(typeid)).offset(pageNum - 1).limit(pageSize).list();
//        return infoDao.queryBuilder().where(infosDao.Properties.TypeId.eq(typeid)).offset(pageNum - 1).limit(pageSize).list();
    }


    /****
     *查询前五条联系人
     */
    public List<PhoneContractor> queryBottom5Contactor() {
        List<PhoneContractor> queryContactors = null;
        String imei = GlobalSettings.instance().getImei();
        if (TextUtils.isEmpty(imei)) return queryContactors;
        PhoneContractorDao dao = getDao();
        try {
            queryContactors = dao.queryBuilder().limit(DBConstant.CONTACTOR_LIMIT_SIZE_PHB).list();
        } catch (SQLiteException e) {
            LogUtils.e("查询 queryBottom5Contactor" + "失败");
        }
        return queryContactors;
    }

    /****
     *查询后五条联系人
     */
    public List<PhoneContractor> queryTop5Contactor() {
        List<PhoneContractor> queryContactors = null;
        String imei = GlobalSettings.instance().getImei();
        if (TextUtils.isEmpty(imei)) return queryContactors;
        PhoneContractorDao dao = getDao();
        try {
            queryContactors = dao.queryBuilder().offset(DBConstant.CONTACTOR_LIMIT_SIZE_PHB).limit(DBConstant.CONTACTOR_LIMIT_SIZE_PHB).list();
        } catch (SQLiteException e) {
            LogUtils.e("查询 queryBottom5Contactor" + "失败");
        }
        return queryContactors;
    }

    @Override
    public void deleteAll() {
        getDao().deleteAll();
    }

    @Override
    public void deleteWhere(long id) {
        getDao().deleteByKey(id);
    }

    @Override
    public List<PhoneContractor> selectAll() {
        List<PhoneContractor> list = getDao().loadAll();
        return null != list && list.size() > 0 ? list : null;
    }

    @Override
    public List<PhoneContractor> selectWhere(PhoneContractor data) {
        return null;
    }

    @Override
    public PhoneContractor seelctWhrer(String name) {
        return null;
    }

    @Override
    public List<PhoneContractor> selectWhrer(long id) {
        return null;
    }
}
