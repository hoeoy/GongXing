package com.houoy.www.gongxing.service;

/**
 * Created by andyzhao on 1/7/2018.
 */

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.houoy.www.gongxing.dao.UserDao;
import com.houoy.www.gongxing.model.ClientInfo;
import com.houoy.www.gongxing.util.StringUtil;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.xutils.ex.DbException;
import org.xutils.x;

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
    private String host = "tcp://192.168.253.1:61613";
//    private String host = "tcp://101.201.67.36:61613";
    private String userName = "admin";
    private String passWord = "password";
    //    private static String myTopic = "topic";
//    private String clientId = "test123456789";

    private UserDao userDao;
    private MyMqttCallback myMqttCallback = null;
    private static MQTTService mqttService = null;
    private int qos = 0;

    public static MQTTService getInstant() {
        if (mqttService == null) {
            mqttService = new MQTTService();
        }
        return mqttService;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try {
            userDao = UserDao.getInstant();
            ClientInfo clientInfo = userDao.findUser();
            if (clientInfo == null || StringUtil.isEmpty(clientInfo.getTopic())) {
                Toast.makeText(x.app(), "无法获得用户的Topic，无法接收到推送消息，请尝试清空缓存后重启应用。", Toast.LENGTH_LONG).show();
            }
            String clientId = clientInfo.getClientId();

            myMqttCallback = MyMqttCallback.getInstant();
            myMqttCallback.init(this);

            // 服务器地址（协议+地址+端口号）
            String uri = host;
            client = new MqttAndroidClient(this, uri, clientId);
            // 设置MQTT监听并且接受消息
            client.setCallback(myMqttCallback);

            conOpt = new MqttConnectOptions();
            //设置是否清空session,这里如果设置为false表示服务器会保留客户端的连接记录，设置为true表示每次连接到服务器都以新的身份连接
            conOpt.setCleanSession(true);
            // 设置超时时间，单位：秒
            conOpt.setConnectionTimeout(10);
            // 心跳包发送间隔，单位：秒
            conOpt.setKeepAliveInterval(20);
            // 用户名
            conOpt.setUserName(userName);
            // 密码
            conOpt.setPassword(passWord.toCharArray());
            conOpt.setAutomaticReconnect(true);

            // last will message
            boolean doConnect = true;
            String message = "{\"terminal_uid\":\"" + clientId + "\"}";

            clientInfo = userDao.findUser();
            if (clientInfo != null) {
                String topic = clientInfo.getTopic();
                if (!StringUtil.isEmpty(topic)) {
                    int qos = 1;
                    Boolean retained = false;
                    if ((!message.trim().equals("")) || (!topic.trim().equals(""))) {
                        try {
                            conOpt.setWill(topic, message.getBytes(), qos, retained.booleanValue());
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
            }
        } catch (DbException e) {
            e.printStackTrace();
            Toast.makeText(x.app(), e.getMessage(), Toast.LENGTH_LONG).show();
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        try {
            client.disconnect();
        } catch (MqttException e) {
            e.printStackTrace();
            Toast.makeText(x.app(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
        super.onDestroy();
    }

    /**
     * 连接MQTT服务器
     */
    public void doClientConnection() {
        if (!client.isConnected() && isConnectIsNomarl()) {
            try {
                client.connect(conOpt, null, iMqttActionListener);
            } catch (MqttException e) {
                e.printStackTrace();
                Toast.makeText(x.app(), e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }

    }

    // MQTT是否连接成功
    private IMqttActionListener iMqttActionListener = new IMqttActionListener() {

        @Override
        public void onSuccess(IMqttToken arg0) {
            Log.i(TAG, "连接成功 ");
            subTopic();
        }

        @Override
        public void onFailure(IMqttToken arg0, Throwable arg1) {
            arg1.printStackTrace();
            // 连接失败，重连
            doClientConnection();
        }
    };

    public void subTopic() {
        try {
            // 订阅myTopic话题
//                client.subscribe(clientInfo.getTopic(), 1);
            ClientInfo clientInfo = userDao.findUser();
            client.subscribe(clientInfo.getTopic(), qos);//只接受一次,确定到达
        } catch (MqttException e) {
            e.printStackTrace();
            Toast.makeText(x.app(), e.getMessage(), Toast.LENGTH_LONG).show();
        } catch (DbException e) {
            e.printStackTrace();
            Toast.makeText(x.app(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void unSubscribe(String topic) {
        try {
            client.unsubscribe(topic);
        } catch (MqttException e) {
            e.printStackTrace();
            Toast.makeText(x.app(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

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

    //    public static void publish(String msg) {
//        String topic = myTopic;
//        Integer qos = 0;
//        Boolean retained = false;
//        try {
//            client.publish(topic, msg.getBytes(), qos.intValue(), retained.booleanValue());
//        } catch (MqttException e) {
//            e.printStackTrace();
//        }
//    }
}