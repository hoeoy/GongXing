//package com.houoy.www.gongxing.util;
//
//import android.os.Environment;
//
///**
// * Created by andyzhao on 2017/12/24.
// */
//
//public class MqttUtil {
//    private static String host = "tcp://192.168.1.1:61613";//这个是你电脑在局域网的ip，端口是61613，tcp是配置文件里面的
//    private static String userName = "admin";
//    private static String passWord = "123456";
//    private static MqttClient client;
//    private static String topicStr = "mqtt/topic";
//    public static final String path = Environment.getExternalStorageDirectory()
//            .getAbsolutePath() + "/apollo";
//    //
//    private static MqttTopic topic;
//    private static MqttMessage message;
//
//
//    public static void connect(MqttCallback callback) {
//// host为主机名，test为clientid即连接MQTT的客户端ID，一般以客户端唯一标识符表示，
//// MemoryPersistence设置clientid的保存形式，默认为以内存保存
//        MqttConnectOptions options = new MqttConnectOptions();
//// 设置是否清空session,这里如果设置为false表示服务器会保留客户端的连接记录，
//// 这里设置为true表示每次连接到服务器都以新的身份连接
//        options.setCleanSession(true);
//// 设置连接的用户名
//        options.setUserName(userName);
//// 设置连接的密码
//        options.setPassword(passWord.toCharArray());
//// 设置超时时间 单位为秒
//        options.setConnectionTimeout(10);
//// 设置会话心跳时间 单位为秒 服务器会每隔1.5*20秒的时间向客户端发送个消息判断客户端是否在线，但这个方法并没有重连的机制
//        options.setKeepAliveInterval(20);
//// 链接
//        try {
//// MemoryPersistence表示保存在内存里面，不写这个的话跑起来报错
//            client = new MqttClient(host, "jyfdjdty", new MemoryPersistence());
//            client.setCallback(callback);
//            client.connect(options);
//// 订阅
//            client.subscribe(topicStr, 2);
//        } catch (MqttSecurityException e) {
//            e.printStackTrace();
//        } catch (MqttException e) {
//            e.printStackTrace();
//        }
//    }
//
//
//    public static void sendMsg(String msg) {
//        if (client != null && client.isConnected()) {
//            topic = client.getTopic(topicStrservice);
//            message = new MqttMessage();
//            message.setQos(1);// 至少一次，可能会重复
//            message.setRetained(true);
//            System.out.println(message.isRetained() + "------ratained状态");
//            MqttDeliveryToken token;
//            try {
//                message.setPayload(msg.getBytes());
//                token = topic.publish(message);
//                token.waitForCompletion();
//            } catch (MqttPersistenceException e) {
//                e.printStackTrace();
//            } catch (MqttException e) {
//                e.printStackTrace();
//            }
//        } else {
//            System.out.println("?");
//        }
//    }
//}
