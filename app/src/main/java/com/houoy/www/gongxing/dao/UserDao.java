package com.houoy.www.gongxing.dao;

import com.houoy.www.gongxing.model.ClientInfo;

import org.xutils.db.sqlite.WhereBuilder;
import org.xutils.ex.DbException;

import java.util.List;

public class UserDao {
    private static UserDao userDao = null;

    private UserDao() {
    }

    public static UserDao getInstant() {
        if (userDao == null) {
            userDao = new UserDao();
        }
        return userDao;
    }


    public Boolean setUser(ClientInfo clientInfo) throws DbException {
        //如果之前有用户则删除
        List<ClientInfo> users = findUsers();
        if (users != null && users.size() > 0) {
            clearUser();
        }

        Boolean result = DBHelper.db.saveBindingId(clientInfo);
        return result;
    }

    public ClientInfo findUser() throws DbException {
        List<ClientInfo> users = DBHelper.db.selector(ClientInfo.class)
                .limit(1) //只查询几条记录
                .offset(0) //偏移两个,从第三个记录开始返回,limit配合offset达到sqlite的limit m,n的查询
                .findAll();
        if (users == null || users.size() < 1) {
            return null;
        }

        return users.get(0);
    }

    public List<ClientInfo> findUsers() throws DbException {
        List<ClientInfo> users = DBHelper.db.selector(ClientInfo.class).findAll();
        return users;
    }

    public Integer deleteUser(int id) throws DbException {
        WhereBuilder whereBuilder = WhereBuilder.b();
        whereBuilder.and("id", "=", id);
        int result = DBHelper.db.delete(ClientInfo.class, whereBuilder);
        return result;
    }

    public void clearUser() throws DbException {
        DBHelper.db.delete(ClientInfo.class);
    }

//    public Boolean setSetting(Setting user) throws DbException {
//        //如果之前有设置则删除
//        Setting user1 = findSetting();
//        if (user1 != null) {
//            clearSetting();
//        }
//
//        Boolean result = DBHelper.db.saveBindingId(user);
//        return result;
//    }
//
//    public Setting findSetting() throws DbException {
//        List<Setting> users = DBHelper.db.selector(Setting.class)
//                .limit(1) //只查询几条记录
//                .offset(0) //偏移两个,从第三个记录开始返回,limit配合offset达到sqlite的limit m,n的查询
//                .findAll();
//        if (users == null || users.size() < 1) {
//            return null;
//        }
//
//        return users.get(0);
//    }
//
//    public Integer deleteSetting(int id) throws DbException {
//        WhereBuilder whereBuilder = WhereBuilder.b();
//        whereBuilder.and("id", "=", id);
//        int result = DBHelper.db.delete(Setting.class, whereBuilder);
//        return result;
//    }
//
//    public void clearSetting() throws DbException {
//        DBHelper.db.delete(Setting.class);
//    }
}
