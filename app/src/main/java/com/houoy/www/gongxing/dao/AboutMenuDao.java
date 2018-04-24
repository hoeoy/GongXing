package com.houoy.www.gongxing.dao;

import com.houoy.www.gongxing.model.AboutMenu;

public class AboutMenuDao extends BaseDao<AboutMenuDao, AboutMenu> {
    private static AboutMenuDao dao = null;

    protected AboutMenuDao() {
    }

    public static AboutMenuDao getInstant() {
        if (dao == null) {
            dao = new AboutMenuDao();
        }
        return dao;
    }
}
