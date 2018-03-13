package com.houoy.www.gongxing.service;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.houoy.www.gongxing.ActivityPool;
import com.houoy.www.gongxing.GongXingApplication;
import com.houoy.www.gongxing.MessageActivity;
import com.houoy.www.gongxing.MessageDetailActivity;
import com.houoy.www.gongxing.R;
import com.houoy.www.gongxing.dao.HouseDao;
import com.houoy.www.gongxing.dao.MessagePushAlertDao;
import com.houoy.www.gongxing.dao.MessagePushDailyDao;
import com.houoy.www.gongxing.dao.TalkerDao;
import com.houoy.www.gongxing.dao.UserDao;
import com.houoy.www.gongxing.event.RefreshChatEvent;
import com.houoy.www.gongxing.event.RefreshMessageEvent;
import com.houoy.www.gongxing.model.ChatHouse;
import com.houoy.www.gongxing.model.ChatTalker;
import com.houoy.www.gongxing.model.ClientInfo;
import com.houoy.www.gongxing.model.Message;
import com.houoy.www.gongxing.model.MessagePushAlert;
import com.houoy.www.gongxing.model.MessagePushBase;
import com.houoy.www.gongxing.model.MessagePushDaily;
import com.houoy.www.gongxing.util.DateUtil;
import com.houoy.www.gongxing.util.StringUtil;
import com.houoy.www.gongxing.vo.MessageVO;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.greenrobot.eventbus.EventBus;
import org.xutils.ex.DbException;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by andyzhao on 3/7/2018.
 */
// MQTT监听并且接受消息
public class MyMqttCallback implements MqttCallback {

    private static MyMqttCallback callback = null;

    public MyMqttCallback() {
    }

    public static MyMqttCallback getInstant() {
        if (callback == null) {
            callback = new MyMqttCallback();
        }
        return callback;
    }

    public void init(MQTTService _service) {
        activityPool = ActivityPool.getInstant();
        messagePushAlertDao = MessagePushAlertDao.getInstant();
        messagePushDailyDao = MessagePushDailyDao.getInstant();
        talkerDao = TalkerDao.getInstant();
        houseDao = HouseDao.getInstant();
        userDao = UserDao.getInstant();
        gongXingApplication = (GongXingApplication) _service.getApplication();
        context = _service.getBaseContext();
        service = _service;
        mNManager = (NotificationManager) _service.getSystemService(NOTIFICATION_SERVICE);

        //创建大图标的Bitmap
        LargeBitmap = BitmapFactory.decodeResource(_service.getResources(), R.mipmap.ic_launcher_round);
    }

    private Notification notify1;
    private MessagePushAlertDao messagePushAlertDao;
    private MessagePushDailyDao messagePushDailyDao;
    private TalkerDao talkerDao;
    private HouseDao houseDao;
    private ActivityPool activityPool;
    private NotificationManager mNManager;
    private MQTTService service;
    Bitmap LargeBitmap = null;
    private UserDao userDao;
    private GongXingApplication gongXingApplication;
    private Context context;

    @Override
    public void messageArrived(String topic, final MqttMessage msg) throws Exception {
        String str1 = new String(msg.getPayload());
        Message message = JSON.parseObject(str1, Message.class);
        if (message != null) {
            MessageVO msgVO = message.getParams();
            String ticker = "";
            if (msgVO != null) {
                try {
                    msgVO.setTime(DateUtil.getNowDateTimeShanghai());
                    MessagePushAlert messagePushAlert = null;
                    MessagePushDaily messagePushDaily = null;

                    ChatHouse chatHouse = null;
                    ChatTalker chatTalker = null;
                    String house_name = "";
                    Integer house_type = -1;

                    //处理消息表
                    if (msgVO.getRule_name_value() != null) {//报警类型属性
                        msgVO.setType(2);
                        ticker = "来自躬行监控的报警消息";
                        house_name = "报警";
                        house_type = ChatHouse.HouseTypeSystemAlert;
                        //验证是否有此relationid的消息，如果有，说明收到重复消息不再存储
                        MessagePushAlert has = messagePushAlertDao.findByRelationID(msgVO.getRelationID());
                        if (has != null) {
                            return;
                        }
                    } else {//日报类型属性
                        msgVO.setType(1);
                        ticker = "来自躬行监控的日报消息";
                        house_name = "日报";
                        house_type = ChatHouse.HouseTypeSystemDaily;
                        //验证是否有此relationid的消息，如果有，说明收到重复消息不再存储
                        MessagePushDaily has = messagePushDailyDao.findByRelationID(msgVO.getRelationID());
                        if (has != null) {
                            return;
                        }
                    }

                    //判断是否在当前聊天室中
                    Activity currentActivity = activityPool.currentActivity();
                    Boolean isJustInTheHouse = false;//是否接收消息时候正在此聊天室中
                    if (currentActivity instanceof MessageActivity) {//聊天室消息列表
                        MessageActivity ca = (MessageActivity) currentActivity;
                        //如果正好在当前聊天室中\
                        if (ca.getChatHouse().getHouse_name().equals(house_name)) {
                            isJustInTheHouse = true;
                        }
                    }

                    if (currentActivity instanceof MessageDetailActivity) {//是否在消息相信中
                        MessageDetailActivity ca = (MessageDetailActivity) currentActivity;
                        //如果正好在当前聊天室中\
                        if (ca.getChatHouse().getHouse_name().equals(house_name)) {
                            isJustInTheHouse = true;
                        }
                    }

                    ClientInfo clientInfo = userDao.findUser();
                    //chathouse,chatUser处理聊天室和聊天用户表
                    chatHouse = houseDao.findByNameAndUserid(house_name, clientInfo.getUserID());
                    if (chatHouse == null) {
                        chatHouse = new ChatHouse();
                        chatHouse.setHouse_name(house_name);
                        chatHouse.setTs(DateUtil.getNowDateTimeShanghai());
                        chatHouse.setLast_essage(ticker);
                        chatHouse.setHouse_type(house_type);
                        chatHouse.setUserid(clientInfo.getUserID());
                        if (!isJustInTheHouse) {//不在当前聊天室，需要更新unreadnum
                            chatHouse.addUnreadNum();
                        }
                        houseDao.add(chatHouse);
                    } else {
                        if (!isJustInTheHouse) {//不在当前聊天室，需要更新unreadnum
                            chatHouse.addUnreadNum();
                            chatHouse.setTs(DateUtil.getNowDateTimeShanghai());
                            houseDao.update(chatHouse);
                        }
                    }

                    switch (msgVO.getType()) {
                        case 1://日报
                            messagePushDaily = new MessagePushDaily(msgVO);
                            messagePushDaily.setHouse_id(chatHouse.getId());
                            messagePushDailyDao.add(messagePushDaily);
                            break;
                        case 2://报警
                            messagePushAlert = new MessagePushAlert(msgVO);
                            messagePushAlert.setHouse_id(chatHouse.getId());
                            messagePushAlertDao.add(messagePushAlert);
                            break;
                    }

                    //处理聊天用户
                    chatTalker = talkerDao.findByName(house_name);
                    if (chatTalker == null) {
                        chatTalker = new ChatTalker();
                        chatTalker.setTalker_name(house_name);
                        chatTalker.setTs(DateUtil.getNowDateTimeShanghai());
                        talkerDao.add(chatTalker);
                    }

                    //处理消息通知
                    if (gongXingApplication.getLifecycle().isForeground()) {//如果在前端运行
                        int i = 0;
                    } else {//如果是在后端
                        if (msgVO.getRule_name_value() != null) {//报警类型属性
                            sendNotification(messagePushAlert, chatHouse);
                        } else {//日报类型属性
                            sendNotification(messagePushDaily, chatHouse);
                        }
                    }

                    //发送刷新布局事件
//                                EventBus.getDefault().post(msgVO);
                    EventBus.getDefault().post(new RefreshMessageEvent("", ""));
                    EventBus.getDefault().post(new RefreshChatEvent("", ""));
                } catch (DbException e) {
                    e.printStackTrace();
                    Log.e(e.getMessage(), e.getLocalizedMessage());
                }
            } else {

            }
        } else {

        }

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//
//            }
//        }).start();
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken arg0) {

    }

    @Override
    public void connectionLost(Throwable arg0) {
        // 失去连接，重连
        service.doClientConnection();
    }

    private void sendNotification(MessagePushBase messagePushBase, ChatHouse chatHouse) {
        //定义一个PendingIntent点击Notification后启动一个Activity
        Intent it = new Intent(context, MessageActivity.class);
//        it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);//应用内只保留一个 MessageActivity
        it.putExtra(MessageActivity.intentStr, chatHouse);
        PendingIntent pit = PendingIntent.getActivity(context, messagePushBase.getType(), it, PendingIntent.FLAG_UPDATE_CURRENT);

        //设置图片,通知标题,发送时间,提示方式等属性
        Notification.Builder mBuilder = new Notification.Builder(context);
        mBuilder.setContentTitle(messagePushBase.getTitle_value())                        //标题
                .setContentText(messagePushBase.getRemark_value())      //内容
                .setSubText(DateUtil.getNowDateTimeShanghai())                    //内容下面的一小段文字
                .setTicker(chatHouse.getHouse_name())             //收到信息后状态栏显示的文字信息
                .setWhen(System.currentTimeMillis())           //设置通知时间
                .setSmallIcon(R.mipmap.ic_launcher_round)            //设置小图标
                .setLargeIcon(LargeBitmap)                     //设置大图标
                .setAutoCancel(true)                           //设置点击后取消Notification
                .setContentIntent(pit);                        //设置PendingIntent

        SharedPreferences mySharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
//                    Map mpsq = mySharedPreferences.getAll();
        Boolean isOpen = mySharedPreferences.getBoolean("notifications_new_message", true);
        Boolean vibrate = mySharedPreferences.getBoolean("notifications_new_message_vibrate", true);
        String ringtoneStr = mySharedPreferences.getString("notifications_new_message_ringtone", "");
        if (isOpen) {
            if (vibrate && StringUtil.isEmpty(ringtoneStr)) {//默认为系统声音
                mBuilder.setDefaults(Notification.DEFAULT_LIGHTS |
                        Notification.DEFAULT_VIBRATE);
//                                    | Notification.DEFAULT_SOUND);    //设置默认的三色灯与振动器与声音
            } else if (!vibrate && !StringUtil.isEmpty(ringtoneStr)) {//只声音
                mBuilder.setDefaults(Notification.DEFAULT_LIGHTS);    //设置默认的三色灯
//                            Ringtone ringtone = RingtoneManager.getRingtone(
//                                    preference.getContext(), Uri.parse(stringValue));
                mBuilder.setSound(Uri.parse(ringtoneStr));
//                            mBuilder.setSound(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.biaobiao));  //设置自定义的提示音
            } else if (vibrate && !StringUtil.isEmpty(ringtoneStr)) {//震动和声音
                mBuilder.setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE);    //设置默认的三色灯与振动器
                mBuilder.setSound(Uri.parse(ringtoneStr));
//                            mBuilder.setSound(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.biaobiao));  //设置自定义的提示音
            } else {

            }
        } else {

        }

        notify1 = mBuilder.build();
//                                mNManager.notify(NOTIFYID_1 + new Random().nextInt(), notify1);
        mNManager.notify(messagePushBase.getType(), notify1);
    }


    //清除通知
    public void cleanAllNotification() {
        if (mNManager != null) {
            mNManager.cancelAll();
        }
    }

}
