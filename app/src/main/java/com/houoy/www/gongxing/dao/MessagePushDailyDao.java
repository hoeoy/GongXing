package com.houoy.www.gongxing.dao;

import com.houoy.www.gongxing.model.MessagePushAlert;
import com.houoy.www.gongxing.model.MessagePushDaily;

import org.xutils.db.sqlite.WhereBuilder;
import org.xutils.ex.DbException;

public class MessagePushDailyDao extends BaseDao<MessagePushDailyDao, MessagePushDaily> {
    private static MessagePushDailyDao dao = null;

    protected MessagePushDailyDao() {
    }

    public static MessagePushDailyDao getInstant() {
        if (dao == null) {
            dao = new MessagePushDailyDao();
        }
        return dao;
    }

    public Integer deleteByHouse(int house_id) throws DbException {
        WhereBuilder whereBuilder = WhereBuilder.b();
        whereBuilder.and("house_id", "=", house_id);
        int result = DBHelper.db.delete(MessagePushDaily.class, whereBuilder);
        return result;
    }
}
