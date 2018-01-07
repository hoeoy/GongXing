package com.houoy.www.gongxing.util;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;


/**
 *@Description:
 *@author lx
 *@date 2017-1-12 下午1:19:42
 */
public class TestMQTT {

    public static void main(String args[]){

        //消息的类型
        String topic        = "topic";
        //消息内容
        String content      = "XX发布了消息";
        //消息发送的模式   选择消息发送的次数，依据不同的使用环境使用不同的模式
        int qos             = 0;
        //服务器地址
        String broker       = "tcp://192.168.0.102:61613";
        //客户端的唯一标识
        String clientId     = "1234567891";
        //消息缓存的方式  内存缓存
        MemoryPersistence persistence = new MemoryPersistence();

        try {
            //创建以MQTT客户端
            MqttClient sampleClient = new MqttClient(broker, clientId, persistence);
            //消息的配置参数
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setUserName("admin");
            connOpts.setPassword("password".toCharArray());
            //不记忆上一次会话
            connOpts.setCleanSession(false);
            System.out.println("Connecting to broker: "+broker);
            //链接服务器
            sampleClient.connect(connOpts);
            System.out.println("Connected");
            System.out.println("Publishing message: "+content);
            //创建消息
            MqttMessage message = new MqttMessage(content.getBytes());
            message.setPayload(content.getBytes());
            //给消息设置发送的模式
            message.setQos(qos);
            //发布消息到服务器
            sampleClient.publish(topic, message);
            System.out.println("Message published");
            //断开链接
            sampleClient.disconnect();
            System.out.println("Disconnected");
            System.exit(0);
        } catch(MqttException me) {
            System.out.println("reason "+me.getReasonCode());
            System.out.println("msg "+me.getMessage());
            System.out.println("loc "+me.getLocalizedMessage());
            System.out.println("cause "+me.getCause());
            System.out.println("excep "+me);
            me.printStackTrace();
        }

    }

}