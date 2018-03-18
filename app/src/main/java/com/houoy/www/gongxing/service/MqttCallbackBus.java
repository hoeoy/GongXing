//package com.houoy.www.gongxing.service;
//
///**
// * Created by andyzhao on 3/18/2018.
// */
//
//import android.util.Log;
//import android.widget.Toast;
//
//import com.alibaba.fastjson.JSON;
//import com.houoy.www.gongxing.model.Message;
//
//import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
//import org.eclipse.paho.client.mqttv3.MqttCallback;
//import org.eclipse.paho.client.mqttv3.MqttMessage;
//import org.xutils.x;
//
///**
// * @author Ai（陈祥林）
// * @date 2018/1/3  10:45
// * @email Webb@starcc.cc
// */
//public class MqttCallbackBus implements MqttCallback {
//
//    /**
//     * 连接中断
//     */
//    @Override
//    public void connectionLost(Throwable cause) {
//        Log.e("MqttManager", "cause : " + cause.toString());
//    }
//
//    /**
//     * 消息送达
//     *
//     * @param topic   主题
//     * @param message 消息
//     */
//    @Override
//    public void messageArrived(String topic, MqttMessage message) throws Exception {
//        Log.e("MqttManager", "topic : " + topic + "\t MqttMessage : " + message.toString());
////        EventBusUtil.sendStickyEvent(new EventModel(10001, topic));
////        EventBusUtil.sendStickyEvent(new EventModel(10010, message));
//
//        String str1 = new String(message.getPayload());
//        Message ddd = JSON.parseObject(str1, Message.class);
//        Toast.makeText(x.app(), str1, Toast.LENGTH_LONG).show();
//    }
//
//    /**
//     * 交互完成
//     *
//     * @param token Mqtt 提交令牌
//     */
//    @Override
//    public void deliveryComplete(IMqttDeliveryToken token) {
//        Log.e("MqttManager", "token : " + token.toString());
//    }
//}