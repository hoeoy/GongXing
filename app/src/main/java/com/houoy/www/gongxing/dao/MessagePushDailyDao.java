package com.houoy.www.gongxing.dao;

import com.houoy.www.gongxing.model.MessagePushDaily;
import com.houoy.www.gongxing.util.StringUtil;

import org.xutils.db.sqlite.WhereBuilder;
import org.xutils.ex.DbException;

import java.util.List;

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

    public List<MessagePushDaily> findPageByHouseId(int start, int limit, String orderBy, Integer house_id) throws DbException {
        if (StringUtil.isEmpty(orderBy)) {
            orderBy = "ts";
        }

        List<MessagePushDaily> models = DBHelper.db.selector(MessagePushDaily.class)
                .where("house_id", "=", house_id)
                .orderBy(orderBy, true)
                .limit(limit) //只查询几条记录
                .offset(start) //偏移两个,从第三个记录开始返回,limit配合offset达到sqlite的limit m,n的查询
                .findAll();
        return models;
    }

    public MessagePushDaily findByRelationID(String RelationID) throws DbException {
        MessagePushDaily model = DBHelper.db.selector(MessagePushDaily.class)
                .where("RelationID", "=", RelationID).findFirst();
        return model;
    }
}
