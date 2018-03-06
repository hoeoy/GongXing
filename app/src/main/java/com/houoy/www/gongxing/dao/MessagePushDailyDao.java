package com.houoy.www.gongxing.dao;

import com.houoy.www.gongxing.model.MessagePushDaily;

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
}
