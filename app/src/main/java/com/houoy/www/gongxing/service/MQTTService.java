package com.houoy.www.gongxing.service;

/**
 * Created by andyzhao on 1/7/2018.
 */

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.houoy.www.gongxing.MainActivity;
import com.houoy.www.gongxing.R;
import com.houoy.www.gongxing.dao.GongXingDao;
import com.houoy.www.gongxing.model.MessagePush;
import com.houoy.www.gongxing.util.DateUtil;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.greenrobot.eventbus.EventBus;

/**
 * MQTT长连接服务
 *
 * @author 一口仨馍 联系方式 : yikousamo@gmail.com
 * @version 创建时间：2016/9/16 22:06
 */
public class MQTTService extends Service {

    public static final String TAG = MQTTService.class.getSimpleName();

    private static MqttAndroidClient client;
    private MqttConnectOptions conOpt;

    //    private String host = "tcp://10.0.2.2:61613";
//    private String host = "tcp://192.168.1.103:61613";
//    private String host = "tcp://192.168.0.102:61613";
    private String host = "tcp://101.201.67.36:61613";
    private String userName = "admin";
    private String passWord = "password";
    private static String myTopic = "topic";
    private String clientId = "test";

    private NotificationManager mNManager;
    private Notification notify1;
    private static final int NOTIFYID_1 = 1;
    Bitmap LargeBitmap = null;
    private GongXingDao gongXingDao;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        init();
        mNManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        //创建大图标的Bitmap
        LargeBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_menu_share);
        gongXingDao = GongXingDao.getInstant();
        return super.onStartCommand(intent, flags, startId);
    }

    public static void publish(String msg) {
        String topic = myTopic;
        Integer qos = 0;
        Boolean retained = false;
        try {
            client.publish(topic, msg.getBytes(), qos.intValue(), retained.booleanValue());
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    private void init() {
        // 服务器地址（协议+地址+端口号）
        String uri = host;
        client = new MqttAndroidClient(this, uri, clientId);
        // 设置MQTT监听并且接受消息
        client.setCallback(mqttCallback);

        conOpt = new MqttConnectOptions();
        // 清除缓存
        conOpt.setCleanSession(true);
        // 设置超时时间，单位：秒
        conOpt.setConnectionTimeout(10);
        // 心跳包发送间隔，单位：秒
        conOpt.setKeepAliveInterval(20);
        // 用户名
        conOpt.setUserName(userName);
        // 密码
        conOpt.setPassword(passWord.toCharArray());

        // last will message
        boolean doConnect = true;
        String message = "{\"terminal_uid\":\"" + clientId + "\"}";
        String topic = myTopic;
        Integer qos = 0;
        Boolean retained = false;
        if ((!message.equals("")) || (!topic.equals(""))) {
            // 最后的遗嘱
            try {
                conOpt.setWill(topic, message.getBytes(), qos.intValue(), retained.booleanValue());
            } catch (Exception e) {
                Log.i(TAG, "Exception Occured", e);
                doConnect = false;
                iMqttActionListener.onFailure(null, e);
            }
        }

        if (doConnect) {
            doClientConnection();
        }

    }

    @Override
    public void onDestroy() {
        try {
            client.disconnect();
        } catch (MqttException e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }

    /**
     * 连接MQTT服务器
     */
    private void doClientConnection() {
        if (!client.isConnected() && isConnectIsNomarl()) {
            try {
                client.connect(conOpt, null, iMqttActionListener);
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }

    }

    // MQTT是否连接成功
    private IMqttActionListener iMqttActionListener = new IMqttActionListener() {

        @Override
        public void onSuccess(IMqttToken arg0) {
            Log.i(TAG, "连接成功 ");
            try {
                // 订阅myTopic话题
                client.subscribe(myTopic, 1);
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(IMqttToken arg0, Throwable arg1) {
            arg1.printStackTrace();
            // 连接失败，重连
        }
    };

    // MQTT监听并且接受消息
    private MqttCallback mqttCallback = new MqttCallback() {

        @Override
        public void messageArrived(String topic, MqttMessage message) throws Exception {

            String str1 = new String(message.getPayload());
            MessagePush msg = JSON.parseObject(str1, MessagePush.class);

            EventBus.getDefault().post(msg);

            gongXingDao.addMessagePush(msg);

            //定义一个PendingIntent点击Notification后启动一个Activity
            Intent it = new Intent(getBaseContext(), MainActivity.class);
            PendingIntent pit = PendingIntent.getActivity(getBaseContext(), 0, it, 0);

            //设置图片,通知标题,发送时间,提示方式等属性
            Notification.Builder mBuilder = new Notification.Builder(getBaseContext());
            mBuilder.setContentTitle("躬行监控")                        //标题
                    .setContentText(str1)      //内容
                    .setSubText(DateUtil.getNowDateTimeShanghai())                    //内容下面的一小段文字
                    .setTicker("收到叶良辰发送过来的信息~")             //收到信息后状态栏显示的文字信息
                    .setWhen(System.currentTimeMillis())           //设置通知时间
                    .setSmallIcon(R.drawable.ic_menu_send)            //设置小图标
                    .setLargeIcon(LargeBitmap)                     //设置大图标
                    .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE)    //设置默认的三色灯与振动器
                    .setSound(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.biaobiao))  //设置自定义的提示音
                    .setAutoCancel(true)                           //设置点击后取消Notification
                    .setContentIntent(pit);                        //设置PendingIntent
            notify1 = mBuilder.build();
            mNManager.notify(NOTIFYID_1, notify1);
        }

        @Override
        public void deliveryComplete(IMqttDeliveryToken arg0) {

        }

        @Override
        public void connectionLost(Throwable arg0) {
            // 失去连接，重连
        }
    };

    /**
     * 判断网络是否连接
     */
    private boolean isConnectIsNomarl() {
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        if (info != null && info.isAvailable()) {
            String name = info.getTypeName();
            Log.i(TAG, "MQTT当前网络名称：" + name);
            return true;
        } else {
            Log.i(TAG, "MQTT 没有可用网络");
            return false;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    //清除通知
    public void cleanAllNotification() {
        mNManager.cancelAll();
    }
}