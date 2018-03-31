package com.houoy.www.gongxing.service;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.sdk.android.push.MessageReceiver;
import com.alibaba.sdk.android.push.notification.CPushMessage;
import com.houoy.www.gongxing.ActivityPool;
import com.houoy.www.gongxing.MessageActivity;
import com.houoy.www.gongxing.MessageDetailActivity;
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
import com.houoy.www.gongxing.model.MessagePushDaily;
import com.houoy.www.gongxing.util.DateUtil;
import com.houoy.www.gongxing.vo.MessageVO;

import org.greenrobot.eventbus.EventBus;
import org.xutils.ex.DbException;

import java.util.Map;

/**
 * @author: 正纬
 * @since: 15/4/9
 * @version: 1.1
 * @feature: 用于接收推送的通知和消息
 */
public class MyMessageReceiver extends MessageReceiver {

    // 消息接收部分的LOG_TAG
    public static final String REC_TAG = "receiver";

    /**
     * 推送通知的回调方法
     *
     * @param context
     * @param title
     * @param summary
     * @param extraMap
     */
    @Override
    public void onNotification(Context context, String title, String summary, Map<String, String> extraMap) {
        // TODO 处理推送通知
        if (null != extraMap) {
            for (Map.Entry<String, String> entry : extraMap.entrySet()) {
                Log.i(REC_TAG, "@Get diy param : Key=" + entry.getKey() + " , Value=" + entry.getValue());
            }
        } else {
            Log.i(REC_TAG, "@收到通知 && 自定义消息为空");
        }
        Log.i(REC_TAG, "收到一条推送通知 ： " + title + ", summary:" + summary);
//        GongXingApplication.setConsoleText("收到一条推送通知 ： " + title + ", summary:" + summary);
    }

    /**
     * 应用处于前台时通知到达回调。注意:该方法仅对自定义样式通知有效,相关详情请参考https://help.aliyun.com/document_detail/30066.html?spm=5176.product30047.6.620.wjcC87#h3-3-4-basiccustompushnotification-api
     *
     * @param context
     * @param title
     * @param summary
     * @param extraMap
     * @param openType
     * @param openActivity
     * @param openUrl
     */
    @Override
    protected void onNotificationReceivedInApp(Context context, String title, String summary, Map<String, String> extraMap, int openType, String openActivity, String openUrl) {
        Log.i(REC_TAG, "onNotificationReceivedInApp ： " + " : " + title + " : " + summary + "  " + extraMap + " : " + openType + " : " + openActivity + " : " + openUrl);
//        GongXingApplication.setConsoleText("onNotificationReceivedInApp ： " + " : " + title + " : " + summary);
    }

    /**
     * 推送消息的回调方法
     *
     * @param context
     * @param cPushMessage
     */
    @Override
    public void onMessage(Context context, CPushMessage cPushMessage) {
        Log.i(REC_TAG, "收到一条推送消息 ： " + cPushMessage.getTitle() + ", content:" + cPushMessage.getContent());
        handleMessage(cPushMessage.getContent());
//        GongXingApplication.setConsoleText(cPushMessage.getTitle() + ", content:" + cPushMessage.getContent());
    }

    /**
     * 从通知栏打开通知的扩展处理
     *
     * @param context
     * @param title
     * @param summary
     * @param extraMap
     */
    @Override
    public void onNotificationOpened(Context context, String title, String summary, String extraMap) {
        Log.i(REC_TAG, "onNotificationOpened ： " + " : " + title + " : " + summary + " : " + extraMap);
//        GongXingApplication.setConsoleText("onNotificationOpened ： " + " : " + title + " : " + summary + " : " + extraMap);
    }

    /**
     * 通知删除回调
     *
     * @param context
     * @param messageId
     */
    @Override
    public void onNotificationRemoved(Context context, String messageId) {
        Log.i(REC_TAG, "onNotificationRemoved ： " + messageId);
//        GongXingApplication.setConsoleText("onNotificationRemoved ： " + messageId);
    }

    /**
     * 无动作通知点击回调。当在后台或阿里云控制台指定的通知动作为无逻辑跳转时,通知点击回调为onNotificationClickedWithNoAction而不是onNotificationOpened
     *
     * @param context
     * @param title
     * @param summary
     * @param extraMap
     */
    @Override
    protected void onNotificationClickedWithNoAction(Context context, String title, String summary, String extraMap) {
        Log.i(REC_TAG, "onNotificationClickedWithNoAction ： " + " : " + title + " : " + summary + " : " + extraMap);
//        GongXingApplication.setConsoleText("onNotificationClickedWithNoAction ： " + " : " + title + " : " + summary + " : " + extraMap);
    }

    private void handleMessage(String str1) {
        try {
            Message message = JSON.parseObject(str1, Message.class);
            if (message != null) {
                MessagePushAlertDao messagePushAlertDao = MessagePushAlertDao.getInstant();
                MessagePushDailyDao messagePushDailyDao = MessagePushDailyDao.getInstant();
                TalkerDao talkerDao = TalkerDao.getInstant();
                HouseDao houseDao = HouseDao.getInstant();
                UserDao userDao = UserDao.getInstant();
                ActivityPool activityPool = ActivityPool.getInstant();

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

//                        //处理消息通知
//                        if (gongXingApplication.getLifecycle().isForeground()) {//如果在前端运行
//                            int i = 0;
//                        } else {//如果是在后端
//                            if (msgVO.getRule_name_value() != null) {//报警类型属性
//                                sendNotification(messagePushAlert, chatHouse);
//                            } else {//日报类型属性
//                                sendNotification(messagePushDaily, chatHouse);
//                            }
//                        }

                        //发送刷新布局事件
//                                EventBus.getDefault().post(msgVO);
                        EventBus.getDefault().post(new RefreshMessageEvent("", ""));
                        EventBus.getDefault().post(new RefreshChatEvent("", ""));
                    } catch (DbException e) {
                        e.printStackTrace();
                        Log.e(e.getMessage(), e.getLocalizedMessage());
                    }
                } else {
                    Log.i("message是转vo失败", "message是转vo失败");
                }
            } else {
                Log.i("message是空的", "message是空的");
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (e != null) {
                Log.e("消费消息出错", e.toString());
            }
        }
    }
}