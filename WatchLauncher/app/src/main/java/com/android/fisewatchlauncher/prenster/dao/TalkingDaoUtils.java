package com.android.fisewatchlauncher.prenster.dao;

import android.database.sqlite.SQLiteException;

import com.android.fisewatchlauncher.dao.TalkingDao;
import com.android.fisewatchlauncher.dao.WetMessageDao;
import com.android.fisewatchlauncher.entity.dao.Talking;
import com.android.fisewatchlauncher.entity.dao.WetMessage;
import com.android.fisewatchlauncher.function.DaoFlagUtils;
import com.android.fisewatchlauncher.manager.DBManager;
import com.android.fisewatchlauncher.utils.LogUtils;

import org.greenrobot.greendao.query.Query;
import org.greenrobot.greendao.query.WhereCondition;

import java.util.Arrays;
import java.util.List;

/**
 * @author mare
 * @Description:TODO
 * @csdnblog http://blog.csdn.net/mare_blue
 * @date 2017/9/5
 * @time 18:08
 */
public class TalkingDaoUtils implements GreenDaoChargeRecordImpl<Talking> {
    public static final int REPEAT_MODE_ONCE = 1;
    public static final int REPEAT_MODE_EVERDAY = 2;
    public static final int REPEAT_MODE_WEEK = 3;

    private TalkingDaoUtils() {
    }

    private static class SingletonHolder {
        private static final TalkingDaoUtils INSTANCE = new TalkingDaoUtils();
    }

    public static TalkingDaoUtils instance() {
        return SingletonHolder.INSTANCE;
    }

    private TalkingDao settingsDao;

    private TalkingDao getDao() {
        if (null == settingsDao) {
            settingsDao = DBManager.instance().getDaoSession().getTalkingDao();
        }
        return settingsDao;
    }

    private WetMessageDao messageDao;

    private WetMessageDao getMessageDao() {
        if (null == messageDao) {
            messageDao = DBManager.instance().getDaoSession().getWetMessageDao();
        }
        return messageDao;
    }

    @Override
    public long insert(Talking data) {
        long add = getDao().insert(data);
        return add;
    }

    /***
     * 更新所有对讲消息到数据库
     * @param data
     * @return
     */
    @Override
    public boolean update(Talking data) {
        if (null == data) return false;
        Talking userQuery = null;
        long insertUsersID = -1;
        String className = data.getClass().getSimpleName();
        TalkingDao dao = getDao();
        Query<Talking> query;
        try {
            query = dao.queryBuilder().build();
            userQuery = query.forCurrentThread().unique();
        } catch (SQLiteException e) {
            LogUtils.e("查询 " + className + "失败");
        }
        if (null != userQuery) {//覆盖更新全部对讲消息
            data.setId(userQuery.getId());
            LogUtils.d("开始更新" + className + "数据...");
        } else {
            LogUtils.d("没找到" + className + "的所在数据 开始插入");
        }
        insertUsersID = dao.insertOrReplace(data);
        boolean isSuccess = insertUsersID >= 0;
        LogUtils.i("更新 Talking " + "的对讲数据" + DaoFlagUtils.insertSuccessOr(isSuccess));
        return isSuccess;
    }

    private List<WetMessage> getRawWetMessages() {
        TalkingDao dao = getDao();
        Talking userQuery = null;
        List<WetMessage> result = null;
        try {
            userQuery = dao.queryBuilder().build().forCurrentThread().unique();
        } catch (SQLiteException e) {
            LogUtils.e("查询 " + Talking.class.getSimpleName() + "失败");
        }
        if (null != userQuery) {
            result = userQuery.getWetMessages();
            LogUtils.d("查询到" + TalkingDao.class.getSimpleName() + "的数据...");
        } else {
            LogUtils.d("没找到" + TalkingDao.class.getSimpleName() + "的所在数据");
        }
        return result;
    }

    /**
     * 批量插入对讲消息记录到数据库
     *
     * @param wetMessages
     */
    public void updateMessageList(final List<WetMessage> wetMessages) {
        final TalkingDao dao = getDao();
        final WetMessageDao wetMessageDao = getMessageDao();
        if (null == wetMessages || wetMessages.isEmpty()) return;
        dao.getSession().runInTx(new Runnable() {
            @Override
            public void run() {
                Talking queryTalking = dao.queryBuilder().build().forCurrentThread().unique();
                long talkingId;
                if (null == queryTalking) {//第一次插入数据
                    queryTalking = new Talking();
                    talkingId = System.currentTimeMillis();
                    queryTalking.setId(talkingId);
                    queryTalking.setWetMessages(wetMessages);
                    dao.update(queryTalking);
                    LogUtils.i("首次更新 TalkingDao 数据库 id: " + talkingId);
                    return;
                }
                talkingId = queryTalking.getId();
                for (int i = 0; i < wetMessages.size(); i++) {
                    WetMessage message = wetMessages.get(i);
                    WetMessage.MessageDirection direction = message.getMessageDirection();//消息方向
                    String senderId = message.getSenderId();
                    String targetId = message.getTargetId();
                    WetMessage.MessageContentType messageContentType = message.getMessageContentType();//消息类型
                    boolean isRecvMsg = direction == WetMessage.MessageDirection.REC;
                    long time = message.getTimestamp();
                    WetMessage.ConverstationType converstationType = message.getConverstationType();
                    WhereCondition timeCondition = WetMessageDao.Properties.Timestamp.eq(time);
                    WhereCondition conversationTypeCondition = WetMessageDao.Properties.ConverstationType.eq(converstationType);
//                    WhereCondition directionCondition = WetMessageDao.Properties.MessageDirection.eq(direction);
                    WhereCondition msgContentTypeCondition = WetMessageDao.Properties.MessageContentType.eq(messageContentType);

                    //时间 会话类型 方向(不要：时间线没方向) 消息内容类型
                    WetMessage queryWetMessage = wetMessageDao.queryBuilder().where(timeCondition,
                            conversationTypeCondition, /*directionCondition,*/ msgContentTypeCondition).build().unique();
                    long msgId = 0;
                    if (null != queryWetMessage) {//replace this queryWetMessage
                        msgId = queryWetMessage.getId();
                    } else {
                        msgId = time;//以消息时间戳作为id
                    }
                    message.setId(msgId);
                    message.setTalkingId(talkingId);//关联talkingId
                    long insertUsersID = wetMessageDao.insertOrReplace(message);
                    boolean isSuccess = insertUsersID >= 0;
                    LogUtils.i("更新 id= " + msgId + " 的数据" + DaoFlagUtils.insertSuccessOr(isSuccess));
                }
            }
        });
    }

    /**
     * 插入单条对讲消息记录到数据库
     *
     * @param message
     */
    public void updateMessage(final WetMessage message) {
        final TalkingDao dao = getDao();
        final WetMessageDao wetMessageDao = getMessageDao();
        if (null == message) return;
        Talking queryTalking = dao.queryBuilder().build().forCurrentThread().unique();
        long talkingId;
        if (null == queryTalking) {//第一次插入数据
            queryTalking = new Talking();
            talkingId = System.currentTimeMillis();
            queryTalking.setId(talkingId);
            queryTalking.setWetMessages(Arrays.asList(new WetMessage[]{message}));
            dao.update(queryTalking);
            LogUtils.i("首次更新 TalkingDao 数据库 id: " + talkingId);
            return;
        }
        talkingId = queryTalking.getId();
        WetMessage.MessageDirection direction = message.getMessageDirection();//消息方向
        String senderId = message.getSenderId();
        String targetId = message.getTargetId();
        WetMessage.MessageContentType messageContentType = message.getMessageContentType();//消息类型
        boolean isRecvMsg = direction == WetMessage.MessageDirection.REC;
        long time = message.getTimestamp();
        WetMessage.ConverstationType converstationType = message.getConverstationType();
        WhereCondition timeCondition = WetMessageDao.Properties.Timestamp.eq(time);
        WhereCondition conversationTypeCondition = WetMessageDao.Properties.ConverstationType.eq(converstationType);
//        WhereCondition directionCondition = WetMessageDao.Properties.MessageDirection.eq(direction);
        WhereCondition msgContentTypeCondition = WetMessageDao.Properties.MessageContentType.eq(messageContentType);

        //时间 会话类型 方向(不要：时间线没方向)  消息内容类型
        WetMessage queryWetMessage = wetMessageDao.queryBuilder().where(timeCondition,
                conversationTypeCondition,/*directionCondition,*/msgContentTypeCondition).build().unique();
        long msgId = 0;
        if (null != queryWetMessage) {//replace this queryWetMessage
            msgId = queryWetMessage.getId();
        } else {
            msgId = time;//以消息时间戳作为id
        }
        message.setId(msgId);
        message.setTalkingId(talkingId);//关联talkingId
        long insertUsersID = wetMessageDao.insertOrReplace(message);
        boolean isSuccess = insertUsersID >= 0;
        LogUtils.i("更新 id= " + msgId + " 的数据" + DaoFlagUtils.insertSuccessOr(isSuccess));

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
    public List<Talking> selectAll() {
        List<Talking> list = getDao().loadAll();
        return null != list && list.size() > 0 ? list : null;
    }

    @Override
    public List<Talking> selectWhere(Talking data) {
        return null;
    }

    @Override
    public Talking seelctWhrer(String name) {
        return null;
    }

    @Override
    public List<Talking> selectWhrer(long id) {
        return null;
    }
}
