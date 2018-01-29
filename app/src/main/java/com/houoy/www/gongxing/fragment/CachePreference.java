package com.houoy.www.gongxing.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.houoy.www.gongxing.R;
import com.houoy.www.gongxing.dao.MessagePushDao;
import com.houoy.www.gongxing.event.RefreshMessageEvent;

import org.greenrobot.eventbus.EventBus;
import org.xutils.ex.DbException;

/**
 * Created by andyzhao on 1/27/2018.
 */
public class CachePreference extends Preference {

    private Context mContext;
    public Button btnCleanCache = null;
    private CheckBoxPreference key_message = null;
    private CheckBoxPreference key_user_cache = null;
    private MessagePushDao messagePushDao;

    public CachePreference(Context context, AttributeSet attrs) {
        super(context, attrs);    // 注：必须是2个参数的，否则会报错
        mContext = context;
        messagePushDao = MessagePushDao.getInstant();
    }

    // 重点来了，复写onCreateView方法，就可以对自定义布局做各种操作啦
    @Override
    protected View onCreateView(ViewGroup parent) {
        // TODO Auto-generated method stub
        super.onCreateView(parent);
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.pre_cache_button_layout, null);  // 加载自定义布局.test_linear_layout
        btnCleanCache = (Button) layout.findViewById(R.id.btnCleanCache);// 现在就findViewById 了
        btnCleanCache.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Boolean bm = key_message.isChecked();
                Boolean um = key_user_cache.isChecked();
                showMsgDialog("确定清除所选缓存", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        try {
                            if (bm) {
                                messagePushDao.clearMessagePush();
                            }
                            EventBus.getDefault().post(new RefreshMessageEvent("", ""));
                            Toast.makeText(mContext, "清除缓存成功", Toast.LENGTH_SHORT).show();
                        } catch (DbException e) {
                            e.printStackTrace();
                        }
                    }
                }, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //取消退出
                        int a = 0;
                    }
                });
            }
        });
        return layout;
    }


    public CheckBoxPreference getKey_message() {
        return key_message;
    }

    public void setKey_message(CheckBoxPreference key_message) {
        this.key_message = key_message;
    }

    public CheckBoxPreference getKey_user_cache() {
        return key_user_cache;
    }

    public void setKey_user_cache(CheckBoxPreference key_user_cache) {
        this.key_user_cache = key_user_cache;
    }

    //显示基本的AlertDialog
    public void showMsgDialog(String message, DialogInterface.OnClickListener sure, DialogInterface.OnClickListener cancel) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("系统提示");
        builder.setMessage(message);
        if (sure != null) {
            builder.setPositiveButton("确定", sure);
        }
        if (cancel != null) {
            builder.setNegativeButton("取消", cancel);
        }
        AlertDialog dialog = null;
        dialog = builder.show();
    }

}