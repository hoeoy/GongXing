package com.houoy.www.gongxing.dao;

import com.houoy.www.gongxing.model.ChatHouse;
import com.houoy.www.gongxing.util.StringUtil;

import org.xutils.ex.DbException;

import java.util.List;

//基础dao
public class HouseDao extends BaseDao<HouseDao, ChatHouse> {
    private static HouseDao dao = null;

    protected HouseDao() {
    }

    public static HouseDao getInstant() {
        if (dao == null) {
            dao = new HouseDao();
        }
        return dao;
    }

    public ChatHouse findByNameAndUserid(String house_name, String UserID) throws DbException {
        if (StringUtil.isEmpty(house_name)) {
            return null;
        }

        ChatHouse models = DBHelper.db.selector(ChatHouse.class)
                .where("house_name", "=", house_name)
                .and("userid", "=", UserID)
                .findFirst();
        return models;
    }

    public List<ChatHouse> findByUserid(String UserID) throws DbException {
        if (StringUtil.isEmpty(UserID)) {
            return null;
        }

        List<ChatHouse> models = DBHelper.db.selector(ChatHouse.class)
                .where("userid", "=", UserID)
                .findAll();
        return models;
    }
}
