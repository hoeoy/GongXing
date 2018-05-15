package com.houoy.www.gongxing;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.alibaba.sdk.android.push.CloudPushService;
import com.alibaba.sdk.android.push.CommonCallback;
import com.alibaba.sdk.android.push.noonesdk.PushServiceFactory;
import com.alibaba.sdk.android.push.register.GcmRegister;
import com.alibaba.sdk.android.push.register.HuaWeiRegister;
import com.alibaba.sdk.android.push.register.MiPushRegister;
import com.houoy.www.gongxing.controller.GongXingController;
import com.houoy.www.gongxing.dao.DBHelper;

import org.xutils.x;

public class GongXingApplication extends Application {
    private static final String TAG = "Init";

//    public static String mqtt_host = "tcp://101.201.67.36:61613";
//    //    public static String mqtt_host = "tcp://192.168.0.103:61613";
//    public static String mqtt_userName = "admin";
//    public static String mqtt_password = "password";

    //    public static String url = "http://101.201.67.36:9011/CloudWeChatPlatServer/";
    public static String sign = "dff687bbfd840d3484e2091b09c8c424";
    public final static String State_normal = "正常";
    public final static String State_warning = "项异常";
    public final static String State_warningName = "报警";
    public final static String DB_Name = "gongxing_db";//本地sqlite数据库名称
    public final static Integer DB_Version = 26;//数据库版本

//    private MainActivity lastMainActivity;

    private MyActivityLifecycle lifecycle;

    private GongXingController controller;

    public CloudPushService pushService;

    public static GongXingApplication gongXingApplication = null;

    @Override
    public void onCreate() {
        super.onCreate();
        gongXingApplication = this;
        initCloudChannel(this);

        x.Ext.init(this);//注入view和事件
//        x.view().inject(this); //使用注解模块一定要注意初始化视图注解框架
//        Mqtt.getInstance(this).connect();
        DBHelper.initDb();

        if (Build.VERSION.SDK_INT >= 14) {
            lifecycle = new MyActivityLifecycle();
            registerActivityLifecycleCallbacks(lifecycle);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //解决v7.0的不能访问下载路径的问题
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
        }

        controller = GongXingController.getInstant();
        controller.setAppContext(getBaseContext());
    }

    /**
     * 初始化云推送通道
     *
     * @param applicationContext
     */
    private void initCloudChannel(Context applicationContext) {
        PushServiceFactory.init(applicationContext);
        pushService = PushServiceFactory.getCloudPushService();
        pushService.register(applicationContext, new CommonCallback() {
            @Override
            public void onSuccess(String response) {
                Log.d(TAG, "init cloudchannel success");
            }

            @Override
            public void onFailed(String errorCode, String errorMessage) {
                Log.d(TAG, "init cloudchannel failed -- errorcode:" + errorCode + " -- errorMessage:" + errorMessage);
            }
        });

        MiPushRegister.register(applicationContext, "XIAOMI_ID", "XIAOMI_KEY"); // 初始化小米辅助推送
        HuaWeiRegister.register(applicationContext); // 接入华为辅助推送
        GcmRegister.register(applicationContext, "send_id", "application_id"); // 接入FCM/GCM初始化推送
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
