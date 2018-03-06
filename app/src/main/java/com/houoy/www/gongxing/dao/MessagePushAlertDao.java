package com.houoy.www.gongxing.dao;

import com.houoy.www.gongxing.model.MessagePushAlert;

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
}
