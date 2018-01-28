package com.houoy.www.gongxing.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.houoy.www.gongxing.model.Message;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;


/**
 * @author lx
 * @Description:
 * @date 2017-1-12 下午1:19:42
 */
public class SenderMQTTTest {
    //消息发送的模式   选择消息发送的次数，依据不同的使用环境使用不同的模式
    private static int qos = 0;
    //消息的类型
    private static String topic = "oEUjn08Drl1HyQEOpiNv1Jd7q_X8";//zhaozhao的openid

    public static void main(String args[]) {

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

            Message msg = getMessage("1");
            publish(sampleClient, msg);

//            Message msg2 = getMessage("2");
//            publish(sampleClient, msg2);

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

    public static void publish(MqttClient sampleClient, Message msg) throws MqttException {
        String content = JSON.toJSONString(msg);
        MqttMessage message = new MqttMessage(content.getBytes());
        message.setPayload(content.getBytes());
        //给消息设置发送的模式
        message.setQos(qos);
        //发布消息到服务器
        sampleClient.publish(topic, message);
    }

    public static Message getMessage(String type) {
        Message warnMessage = null;
        switch (type) {
            case "1"://日报
                String day = "{" +
                        "\"sign\": \"CC0DE3D52637C796D49C70C60C39A833\"," +
                        "\"Params\": {" +
                        "\"touser\": \"oEUjn07LrcKcPgwn-L9u1QJF8B2s\"," +
                        "\"title_value\": \"运行日报" + DateUtil.getNowDateTimeShanghai() + "\"," +
                        "\"title_color\": \"#000000\"," +
                        "\"temperature_value\": \"20.3 ℃\"," +
                        "\"temperature_color\": \"#000000\"," +
                        "\"humidity_value\": \"9.9 %\"," +
                        "\"humidity_color\": \"#000000\"," +
                        "\"state_value\": \"无\"," +
                        "\"state_color\": \"#000000\"," +
                        "\"alarm_num_value\": \"无\"," +
                        "\"alarm_num_color\": \"#000000\"," +
                        "\"remark_value\": \"日报摘要：\\r\\n今日机房运行正常。\"," +
                        "\"remark_color\": \"#000000\"," +
                        "\"RelationID\": 7794" +
                        "}" +
                        "}";
                warnMessage = JSONObject.parseObject(day, Message.class);
                break;
            case "2"://报警
                String warn = "{" +
                        "\"sign\": \"7BF53340D8632BCEF071B485BC06C0B3\"," +
                        "\"Params\": {" +
                        "\"touser\": \"oEUjn07LrcKcPgwn-L9u1QJF8B2s\"," +
                        "\"title_value\": \"回龙观机房发生报警" + DateUtil.getNowDateTimeShanghai() + "\"," +
                        "\"title_color\": \"#000000\"," +
                        "\"rule_name_value\": \"报警测试1\"," +
                        "\"rule_name_color\": \"#000000\"," +
                        "\"trigger_time_value\": \"2018-01-25 10:55:07\"," +
                        "\"trigger_time_color\": \"#000000\"," +
                        "\"device_name_value\": \"采集主机afa\"," +
                        "\"device_name_color\": \"#000000\"," +
                        "\"subkey_name_value\": \"怎么写都成\"," +
                        "\"subkey_name_color\": \"#000000\"," +
                        "\"current_parameter_value\": \"报警\"," +
                        "\"current_parameter_color\": \"#000000\"," +
                        "\"remark_value\": \"报警区间：true \\r\\n测试报警联动与取消\"," +
                        "\"remark_color\": \"#000000\"," +
                        "\"RelationID\": 7776" +
                        "}" +
                        "}";
                warnMessage = JSONObject.parseObject(warn, Message.class);
                break;
            default:
                break;
        }
        return warnMessage;
    }

}