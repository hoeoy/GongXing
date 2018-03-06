package com.houoy.www.gongxing.dao;

import com.houoy.www.gongxing.model.ChatTalker;
import com.houoy.www.gongxing.util.StringUtil;

import org.xutils.ex.DbException;

public class TalkerDao extends BaseDao<TalkerDao, ChatTalker> {
    private static TalkerDao dao = null;

    protected TalkerDao() {
    }

    public static TalkerDao getInstant() {
        if (dao == null) {
            dao = new TalkerDao();
        }
        return dao;
    }

    public ChatTalker findByName(String talker_name) throws DbException {
        if (StringUtil.isEmpty(talker_name)) {
            return null;
        }

        ChatTalker models = DBHelper.db.selector(ChatTalker.class)
                .where("talker_name", "=", talker_name)
                .findFirst();
        return models;
    }
}
