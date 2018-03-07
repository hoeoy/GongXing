package com.houoy.www.gongxing.dao;

import com.houoy.www.gongxing.model.MessagePushAlert;

import org.xutils.db.sqlite.WhereBuilder;
import org.xutils.ex.DbException;

import java.lang.reflect.ParameterizedType;

public class MessagePushAlertDao extends BaseDao<MessagePushAlertDao, MessagePushAlert> {
    private static MessagePushAlertDao dao = null;

    protected MessagePushAlertDao() {
    }

    public static MessagePushAlertDao getInstant() {
        if (dao == null) {
            dao = new MessagePushAlertDao();
        }
        return dao;
    }

    public Integer deleteByHouse(int house_id) throws DbException {
        WhereBuilder whereBuilder = WhereBuilder.b();
        whereBuilder.and("house_id", "=", house_id);
        int result = DBHelper.db.delete(MessagePushAlert.class, whereBuilder);
        return result;
    }
}
