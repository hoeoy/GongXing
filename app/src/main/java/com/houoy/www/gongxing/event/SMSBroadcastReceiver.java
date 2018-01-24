/*
package com.houoy.www.gongxing.event;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsMessage;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;

import java.text.SimpleDateFormat;
import java.util.Date;

*/
/**
 * Created by andyzhao on 2017/12/8.
 *//*

public class SMSBroadcastReceiver extends BroadcastReceiver {

    public SMSBroadcastReceiver() {
        super();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Object[] pdus = (Object[]) intent.getExtras().get("pdus");
        for (Object pdu : pdus) {
            SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdu);
            String sender = smsMessage.getDisplayOriginatingAddress();
            String content = smsMessage.getMessageBody();
            long date = smsMessage.getTimestampMillis();
            Date timeDate = new Date(date);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String time = simpleDateFormat.format(timeDate);

            Log.i("SMS", "onReceive: 短信来自:" + sender);
            Log.i("SMS", "onReceive: 短信内容:" + content);
            Log.i("SMS", "onReceive: 短信时间:" + time);

            //如果短信号码来自自己的短信网关号码
            if ("your sender number".equals(sender)) {
                Log.i("SMS", "onReceive: 回调");
                // 接口回调传过去状态的类型
                EventBus.getDefault().post(new SMSBroadcastEvent("logout", content));
            }
        }
    }
}*/
