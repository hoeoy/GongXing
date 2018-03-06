package com.houoy.www.gongxing.dao;

import com.houoy.www.gongxing.model.ChatHouse;
import com.houoy.www.gongxing.util.StringUtil;

import org.xutils.ex.DbException;

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

    public ChatHouse findByName(String house_name) throws DbException {
        if (StringUtil.isEmpty(house_name)) {
            return null;
        }

        ChatHouse models = DBHelper.db.selector(ChatHouse.class)
                .where("house_name", "=", house_name)
                .findFirst();
        return models;
    }
}
