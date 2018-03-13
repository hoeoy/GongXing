package com.houoy.www.gongxing;

import android.app.Application;
import android.os.Build;

import com.houoy.www.gongxing.controller.GongXingController;
import com.houoy.www.gongxing.dao.DBHelper;

import org.xutils.x;

public class GongXingApplication extends Application {

    //    public static String url = "http://101.201.67.36:9011/CloudWeChatPlatServer/";
    public static String sign = "dff687bbfd840d3484e2091b09c8c424";
    public final static String State_normal = "正常";
    public final static String State_warning = "项异常";
    public final static String State_warningName = "报警";
    public final static String DB_Name = "gongxing_db";//本地sqlite数据库名称
    public final static Integer DB_Version = 16;//数据库版本

//    private MainActivity lastMainActivity;

    private MyActivityLifecycle lifecycle;

    private GongXingController controller;

    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);//注入view和事件
//        x.view().inject(this); //使用注解模块一定要注意初始化视图注解框架
//        Mqtt.getInstance(this).connect();
        DBHelper.initDb();

        if (Build.VERSION.SDK_INT >= 14) {
            lifecycle = new MyActivityLifecycle();
            registerActivityLifecycleCallbacks(lifecycle);
        }

        controller = GongXingController.getInstant();
        controller.setAppContext(getBaseContext());
    }

    public MyActivityLifecycle getLifecycle() {
        return lifecycle;
    }

//    public MainActivity getLastMainActivity() {
//        return lastMainActivity;
//    }
//
//    public void setLastMainActivity(MainActivity lastMainActivity) {
//        this.lastMainActivity = lastMainActivity;
//    }
}
