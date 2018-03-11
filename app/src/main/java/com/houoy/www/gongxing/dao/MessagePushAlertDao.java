package com.houoy.www.gongxing.dao;

import com.houoy.www.gongxing.model.MessagePushAlert;
import com.houoy.www.gongxing.util.StringUtil;

import org.xutils.db.sqlite.WhereBuilder;
import org.xutils.ex.DbException;

import java.util.List;

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


    public List<MessagePushAlert> findPageByHouseId(int start, int limit, String orderBy, Integer house_id) throws DbException {
        if (StringUtil.isEmpty(orderBy)) {
            orderBy = "ts";
        }

        List<MessagePushAlert> models = DBHelper.db.selector(MessagePushAlert.class)
                .where("house_id", "=", house_id)
                .orderBy(orderBy, true)
                .limit(limit) //只查询几条记录
                .offset(start) //偏移两个,从第三个记录开始返回,limit配合offset达到sqlite的limit m,n的查询
                .findAll();
        return models;
    }

    public MessagePushAlert findByRelationID(String RelationID) throws DbException {
        MessagePushAlert model = DBHelper.db.selector(MessagePushAlert.class)
                .where("RelationID", "=", RelationID).findFirst();
        return model;
    }
}
