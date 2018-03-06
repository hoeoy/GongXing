package com.houoy.www.gongxing.service;

/**
 * Created by andyzhao on 1/7/2018.
 */

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.houoy.www.gongxing.MainActivity;
import com.houoy.www.gongxing.R;
import com.houoy.www.gongxing.dao.HouseDao;
import com.houoy.www.gongxing.dao.MessagePushAlertDao;
import com.houoy.www.gongxing.dao.MessagePushDailyDao;
import com.houoy.www.gongxing.dao.TalkerDao;
import com.houoy.www.gongxing.dao.UserDao;
import com.houoy.www.gongxing.model.ChatHouse;
import com.houoy.www.gongxing.model.ChatTalker;
import com.houoy.www.gongxing.model.ClientInfo;
import com.houoy.www.gongxing.model.Message;
import com.houoy.www.gongxing.model.MessagePushAlert;
import com.houoy.www.gongxing.model.MessagePushDaily;
import com.houoy.www.gongxing.util.DateUtil;
import com.houoy.www.gongxing.util.StringUtil;
import com.houoy.www.gongxing.vo.MessageVO;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.greenrobot.eventbus.EventBus;
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
//    private String host = "tcp://192.168.0.102:61613";
    private String host = "tcp://101.201.67.36:61613";
    private String userName = "admin";
    private String passWord = "password";
    //    private static String myTopic = "topic";
//    private String clientId = "test123456789";
    private ClientInfo clientInfo;
    private NotificationManager mNManager;
    private Notification notify1;
    private static final int NOTIFYID_1 = 1;
    Bitmap LargeBitmap = null;
    private MessagePushAlertDao messagePushAlertDao;
    private MessagePushDailyDao messagePushDailyDao;
    private TalkerDao talkerDao;
    private HouseDao houseDao;
    private UserDao userDao;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        messagePushAlertDao = MessagePushAlertDao.getInstant();
        messagePushDailyDao = MessagePushDailyDao.getInstant();
        talkerDao = TalkerDao.getInstant();
        houseDao = HouseDao.getInstant();

        userDao = UserDao.getInstant();

        try {
            clientInfo = userDao.findUser();
            if (clientInfo == null || StringUtil.isEmpty(clientInfo.getTopic())) {
                Toast.makeText(x.app(), "无法获得用户的Topic，无法接收到推送消息，请尝试清空缓存后重启应用。", Toast.LENGTH_LONG).show();
            }
            init();
            mNManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            //创建大图标的Bitmap
            LargeBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher_round);
        } catch (DbException e) {
            e.printStackTrace();
            Toast.makeText(x.app(), e.getMessage(), Toast.LENGTH_LONG).show();
        }

        return super.onStartCommand(intent, flags, startId);
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

    private void init() {
        try {
            String clientId = clientInfo.getTopic();

            // 服务器地址（协议+地址+端口号）
            String uri = host;
            client = new MqttAndroidClient(this, uri, clientId);
            // 设置MQTT监听并且接受消息
            client.setCallback(mqttCallback);

            conOpt = new MqttConnectOptions();
            // 清除缓存
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
            ClientInfo clientInfo = null;
            clientInfo = userDao.findUser();
            if (clientInfo != null) {
                String topic = clientInfo.getTopic();
                if (!StringUtil.isEmpty(topic)) {
                    Integer qos = 0;
                    Boolean retained = false;
                    if ((!message.trim().equals("")) || (!topic.trim().equals(""))) {
                        try {
                            conOpt.setWill(topic, message.getBytes(), qos.intValue(), retained.booleanValue());
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
        }
    }

    @Override
    public void onDestroy() {
        try {
            client.disconnect();
        } catch (MqttException e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }

    /**
     * 连接MQTT服务器
     */
    private void doClientConnection() {
        if (!client.isConnected() && isConnectIsNomarl()) {
            try {
                client.connect(conOpt, null, iMqttActionListener);
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }

    }

    // MQTT是否连接成功
    private IMqttActionListener iMqttActionListener = new IMqttActionListener() {

        @Override
        public void onSuccess(IMqttToken arg0) {
            Log.i(TAG, "连接成功 ");
            try {
                // 订阅myTopic话题
//                client.subscribe(clientInfo.getTopic(), 1);
                client.subscribe(clientInfo.getTopic(), 2);//只接受一次,确定到达
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(IMqttToken arg0, Throwable arg1) {
            arg1.printStackTrace();
            // 连接失败，重连
        }
    };

    // MQTT监听并且接受消息
    private MqttCallback mqttCallback = new MqttCallback() {

        @Override
        public void messageArrived(String topic, final MqttMessage message) throws Exception {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String str1 = new String(message.getPayload());
                    Message message = JSON.parseObject(str1, Message.class);
                    if (message != null) {
                        MessageVO msgVO = message.getParams();
                        String ticker = "";
                        if (msgVO != null) {
                            try {
                                msgVO.setTime(DateUtil.getNowDateTimeShanghai());

                                ChatHouse chatHouse = null;
                                ChatTalker chatTalker = null;
                                String house_name = "";
                                Integer house_type = -1;
                                if (msgVO.getRule_name_value() != null) {//报警类型属性
                                    msgVO.setType("2");
                                    ticker = "来自躬行监控的报警消息";
                                    MessagePushAlert messagePushAlert = new MessagePushAlert(msgVO);
                                    messagePushAlertDao.add(messagePushAlert);
                                    house_name = "报警";
                                    house_type = ChatHouse.HouseTypeSystemAlert;
                                } else {//日报类型属性
                                    msgVO.setType("1");
                                    ticker = "来自躬行监控的日报消息";
                                    MessagePushDaily messagePushDaily = new MessagePushDaily(msgVO);
                                    messagePushDailyDao.add(messagePushDaily);
                                    house_name = "日报";
                                    house_type = ChatHouse.HouseTypeSystemDaily;
                                }

                                //chathouse,chatUser
                                chatHouse = houseDao.findByName(house_name);
                                if (chatHouse == null) {
                                    chatHouse = new ChatHouse();
                                    chatHouse.setHouse_name(house_name);
                                    chatHouse.setTs(DateUtil.getNowDateShanghai());
                                    chatHouse.setLast_essage(ticker);
                                    chatHouse.setHouse_type(house_type);
                                    chatHouse.addUnreadNum();
                                    houseDao.add(chatHouse);
                                } else {
                                    chatHouse.addUnreadNum();
                                    houseDao.update(chatHouse);
                                }
                                chatTalker = talkerDao.findByName(house_name);
                                if (chatTalker == null) {
                                    chatTalker = new ChatTalker();
                                    chatTalker.setTalker_name(house_name);
                                    chatTalker.setTs(DateUtil.getNowDateShanghai());
                                    talkerDao.add(chatTalker);
                                }

                                EventBus.getDefault().post(msgVO);

                                //定义一个PendingIntent点击Notification后启动一个Activity
                                Intent it = new Intent(getBaseContext(), MainActivity.class);
//                    it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);//应用内只保留一个mainActivity
                                PendingIntent pit = PendingIntent.getActivity(getBaseContext(), 0, it, 0);


                                //设置图片,通知标题,发送时间,提示方式等属性
                                Notification.Builder mBuilder = new Notification.Builder(getBaseContext());
                                mBuilder.setContentTitle(msgVO.getTitle_value())                        //标题
                                        .setContentText(msgVO.getRemark_value())      //内容
                                        .setSubText(DateUtil.getNowDateTimeShanghai())                    //内容下面的一小段文字
                                        .setTicker(ticker)             //收到信息后状态栏显示的文字信息
                                        .setWhen(System.currentTimeMillis())           //设置通知时间
                                        .setSmallIcon(R.drawable.ic_menu_send)            //设置小图标
                                        .setLargeIcon(LargeBitmap)                     //设置大图标
                                        .setAutoCancel(true)                           //设置点击后取消Notification
                                        .setContentIntent(pit);                        //设置PendingIntent


                                SharedPreferences mySharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
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
                                mNManager.notify(NOTIFYID_1, notify1);
                            } catch (DbException e) {
                                e.printStackTrace();
                                Log.e(e.getMessage(), e.getLocalizedMessage());
                            }
                        } else {

                        }
                    } else {

                    }
                }
            }).start();
        }

        @Override
        public void deliveryComplete(IMqttDeliveryToken arg0) {

        }

        @Override
        public void connectionLost(Throwable arg0) {
            // 失去连接，重连
        }
    };

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

    //清除通知
    public void cleanAllNotification() {
        mNManager.cancelAll();
    }
}