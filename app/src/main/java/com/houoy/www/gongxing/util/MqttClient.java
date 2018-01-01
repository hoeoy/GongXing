package com.houoy.www.gongxing.util;

import org.fusesource.hawtbuf.Buffer;
import org.fusesource.hawtbuf.UTF8Buffer;
import org.fusesource.mqtt.client.FutureConnection;
import org.fusesource.mqtt.client.MQTT;
import org.fusesource.mqtt.client.QoS;

public class MqttClient {

    public static void main(String[] args) throws Exception {
        sendMessage("hello");
    }
    public static void sendMessage(String message) {
        String user = MqttConstants.user ;
        String password = MqttConstants.password;
        String host = MqttConstants.host;
        int port = MqttConstants.port;
        final String destination = MqttConstants.destination;
//        String user = env("APOLLO_USER", "admin");
//        String password = env("APOLLO_PASSWORD", "password");
//        String host = env("APOLLO_HOST", "localhost");//apollo服务器地址
//        int port = 61613;//apollo端口号
//        String destination = "/topic/1/OOOOOOOOOO/aaaa";//topic
        Buffer msg = new UTF8Buffer(message);
        MQTT mqtt = new MQTT();//新建MQTT
        try {
            mqtt.setHost(host, port);
            mqtt.setUserName(user);
            mqtt.setPassword(password);

            FutureConnection connection = mqtt.futureConnection();
            connection.connect().await();
            UTF8Buffer topic = new UTF8Buffer(destination);
            connection.publish(topic, msg, QoS.AT_LEAST_ONCE, false);
            connection.disconnect().await();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // System.exit(0);
        }
    }

    private static String env(String key, String defaultValue) {
        String rc = System.getenv(key);
        if (rc == null) {
            return defaultValue;
        }
        return rc;
    }

}
