package com.houoy.www.gongxing;

import android.app.Application;
import android.content.Intent;

import com.houoy.www.gongxing.dao.GongXingDao;
import com.houoy.www.gongxing.service.MQTTService;

import org.xutils.x;

public class GongXingApplication extends Application {
    private GongXingDao gongXingDao;

    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);//注入view和事件
//        x.view().inject(this); //使用注解模块一定要注意初始化视图注解框架
//        Mqtt.getInstance(this).connect();

        gongXingDao = GongXingDao.getInstant();
        gongXingDao.initDb();
        startService(new Intent(this, MQTTService.class));
    }


}
