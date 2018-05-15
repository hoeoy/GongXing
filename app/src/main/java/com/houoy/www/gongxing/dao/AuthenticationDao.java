package com.houoy.www.gongxing.dao;

import com.houoy.www.gongxing.model.ForgetTimer;
import com.houoy.www.gongxing.util.DateUtil;

import org.xutils.ex.DbException;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 登录，注册，忘记密码等鉴权相关的dao
 */
public class AuthenticationDao extends BaseDao<AuthenticationDao, ForgetTimer> {
    private static AuthenticationDao dao = null;

    protected AuthenticationDao() {
    }

    public static AuthenticationDao getInstant() {
        if (dao == null) {
            dao = new AuthenticationDao();
        }
        return dao;
    }

    //更新定时器开始时间
    public void refresh() throws DbException {
        ForgetTimer forgetTimer = findFirst();
        if (forgetTimer == null) {
            //add
            forgetTimer = new ForgetTimer();
            forgetTimer.setLastTime(DateUtil.getNowDateTimeShanghai());
            add(forgetTimer);
        } else {
            //update
            forgetTimer.setLastTime(DateUtil.getNowDateTimeShanghai());
            update(forgetTimer);
        }
    }

    /**
     * 获取数据库存储的开始时间与现在时间的差,单位是毫秒
     *
     * @return
     * @throws DbException
     */
    public long diffNow() throws DbException, ParseException {
        ForgetTimer forgetTimer = findFirst();
        if (forgetTimer == null) {
            //数据库没有存，则
            return 0;
        } else {
            String now = DateUtil.getNowDateTimeShanghai();
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date d1 = df.parse(now);
            Date d2 = df.parse(forgetTimer.getLastTime());
            long diff = d1.getTime() - d2.getTime();
            return diff;
        }
    }
}
