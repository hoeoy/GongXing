package com.houoy.www.gongxing.event;


import android.app.usage.UsageEvents;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

import com.houoy.www.gongxing.RegisterAndSignInActivity;
import com.houoy.www.gongxing.util.NetUtil;

import org.greenrobot.eventbus.EventBus;

/**
 * 自定义检查手机网络状态是否切换的广播接受器
 */
public class NetBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub
        // 如果相等的话就说明网络状态发生了变化
        if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            int netWorkState = NetUtil.getNetWorkState(context);
            // 接口回调传过去状态的类型
            EventBus.getDefault().post(new NetworkChangeEvent("logout", netWorkState));
        }
    }
}
