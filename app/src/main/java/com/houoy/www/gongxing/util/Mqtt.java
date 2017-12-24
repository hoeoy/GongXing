package com.houoy.www.gongxing.util;

import android.content.Context;
import android.util.Log;

import com.houoy.www.gongxing.event.PushMessageEvent;
import com.houoy.www.gongxing.event.RegisterEvent;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.greenrobot.eventbus.EventBus;

/**
 * Created by leo on 2017/6/9.
 */

public class Mqtt {
    private MqttAndroidClient mqttAndroidClient;
    private MqttConnectOptions options;
    private static final String BROKER_IP = "tcp://localhost:61613";
    public static final String TOPIC1 = "topic1";
    public static final String TOPIC2 = "topic2";

    private static Mqtt instance;
    private static final String USERNAME = "zz";
    private static final String PASSWORD = "1";
    private static final String clientId = "1234567890";
    private static final String TAG = Mqtt.class.getName();

    private Mqtt(Context context) {
//        clientId = UUID.randomUUID().toString();
//        clientId = AndroidUniqueIdUtil.getUniqueID(context);
        options = new MqttConnectOptions();
        options.setCleanSession(false);
//            // 设置连接的用户名
        options.setUserName(USERNAME);
        // 设置连接的密码
        options.setPassword(PASSWORD.toCharArray());
        // 设置超时时间 单位为秒
        options.setConnectionTimeout(10);
        // 设置会话心跳时间 单位为秒 服务器会每隔1.5*20秒的时间向客户端发送个消息判断客户端是否在线，但这个方法并没有重连的机制
        options.setKeepAliveInterval(20);
        options.setAutomaticReconnect(true);
        try {
            mqttAndroidClient = new MqttAndroidClient(context, BROKER_IP, clientId, new MemoryPersistence());
        } catch (Exception e) {
            e.printStackTrace();
            Log.i(TAG, "create mqtt client error");
        }
        mqttAndroidClient.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {
                Log.i(TAG, "lost:" + cause);
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                Log.i(TAG, "receiveMsg:topic" + topic + ",msg:" + message.toString());
                EventBus.getDefault().post(new PushMessageEvent(topic, message));
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {
                Log.i(TAG, "msg published");
            }
        });

    }

    public static Mqtt getInstance(Context context) {
        if (null == instance) {
            synchronized (Mqtt.class) {
                instance = new Mqtt(context);
            }
        }
        return instance;
    }

    /**
     * 连接服务器
     * MqttService有自己的重连机制，在断线情况下会重连，但是首次连接失败后，需要再调用connect方法
     */
    public void connect() {

        try {
            mqttAndroidClient.connect(options, this, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Log.i(TAG, "connected");
                    try {
                        mqttAndroidClient.subscribe(new String[]{TOPIC1, TOPIC2}, new int[]{2, 2});
                    } catch (MqttException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.i(TAG, "connect error:" + exception);
//                    connect();//当发生连接失败的情况时继续连接。通常只发生在服务器未在线情况，一旦服务器上线，将立刻连接。
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    /**
     * 断开服务器链接
     */
    public void disConnect() {
        if (null == mqttAndroidClient) {
            return;
        }
        try {
            mqttAndroidClient.disconnect();
        } catch (MqttException e) {
            e.printStackTrace();
        }
        Log.i(TAG, "disconnected");
    }

    /**
     * 发布消息
     *
     * @param topic topic
     * @param msg   消息内容
     * @param qos   0：最多一次的传输；1：至少一次的传输；2： 只有一次的传输
     */
    public void publish(String topic, String msg, int qos) {
        MqttMessage mqttMessage = new MqttMessage();
        mqttMessage.setPayload(msg.getBytes());
        mqttMessage.setRetained(true);
        mqttMessage.setQos(qos);
        try {
            IMqttDeliveryToken token = mqttAndroidClient.publish(topic, mqttMessage);
            token.waitForCompletion();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

}