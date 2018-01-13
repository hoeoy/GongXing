package com.houoy.www.gongxing.util;

import com.alibaba.fastjson.JSON;
import com.houoy.www.gongxing.model.MessagePush;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.util.Date;


/**
 * @author lx
 * @Description:
 * @date 2017-1-12 下午1:19:42
 */
public class TestMQTT {

    public static void main(String args[]) {

        //消息的类型
        String topic = "topic";
        //消息发送的模式   选择消息发送的次数，依据不同的使用环境使用不同的模式
        int qos = 0;
        //服务器地址
//        String broker = "tcp://192.168.0.102:61613";
        String broker = "tcp://101.201.67.36:61613";
        //客户端的唯一标识
        String clientId = "1234567891";
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
            System.out.println("Connecting to broker: " + broker);
            //链接服务器
            sampleClient.connect(connOpts);

            MessagePush msg = new MessagePush();
            msg.setTitle_value("title" + new Date());
            msg.setRule_name_value("rule" + new Date());
            msg.setSubkey_name_value("subkey" + new Date());
            msg.setTime(DateUtil.getNowDateTimeShanghai());
            msg.setDevice_name_value("shebei asdf" + new Date());
            msg.setRelationID("sdsfsdfsdfusodfusduf897987807t78" + new Date());
            msg.setCurrent_parameter_value("current parmeters" + new Date());
            String content = JSON.toJSONString(msg);
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
        } catch (MqttException me) {
            System.out.println("reason " + me.getReasonCode());
            System.out.println("msg " + me.getMessage());
            System.out.println("loc " + me.getLocalizedMessage());
            System.out.println("cause " + me.getCause());
            System.out.println("excep " + me);
            me.printStackTrace();
        }

    }

}