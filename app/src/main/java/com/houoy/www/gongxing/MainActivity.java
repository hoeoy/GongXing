package com.houoy.www.gongxing;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.sdk.android.push.CommonCallback;
import com.houoy.www.gongxing.controller.GongXingController;
import com.houoy.www.gongxing.controller.UploadController;
import com.houoy.www.gongxing.dao.UserDao;
import com.houoy.www.gongxing.event.LogoutEvent;
import com.houoy.www.gongxing.event.NetBroadcastReceiver;
import com.houoy.www.gongxing.event.NetworkChangeEvent;
import com.houoy.www.gongxing.event.UpdateEvent;
import com.houoy.www.gongxing.fragment.ChatFragment;
import com.houoy.www.gongxing.fragment.SearchFragment;
import com.houoy.www.gongxing.model.AboutMenu;
import com.houoy.www.gongxing.model.ClientInfo;
import com.houoy.www.gongxing.service.DownLoadService;
import com.houoy.www.gongxing.util.AppUtil;
import com.houoy.www.gongxing.util.ImageUtil;
import com.houoy.www.gongxing.util.StringUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.xutils.ex.DbException;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.File;

@ContentView(R.layout.activity_main)
public class MainActivity extends MyAppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {
    private static final String TAG = "MainActivity";

    @ViewInject(R.id.recycler_view)
    private RecyclerView mRecyclerView;

    @ViewInject(R.id.toolbar)
    private Toolbar toolbar;

    @ViewInject(R.id.drawer_layout)
    private DrawerLayout drawer;

    @ViewInject(R.id.nav_view)
    private NavigationView navigationView;


    @ViewInject(R.id.txt_chat)
    private TextView txt_chat;
    //    @ViewInject(R.id.txt_message)
//    private TextView txt_message;
    @ViewInject(R.id.txt_search)
    private TextView txt_search;
    @ViewInject(R.id.txt_record)
    private TextView txt_record;
    //    @ViewInject(R.id.txt_notice)
//    private TextView txt_notice;
    @ViewInject(R.id.ly_content)
    private FrameLayout ly_content;

    //Fragment Object
//    private MessageFragment messageFragment;
    private ChatFragment chatFragment;
    //    private NoticeFragment noticeFragment;
    private SearchFragment searchFragment;
    private FragmentManager fManager;

    private Context mContext;
    private long exitTime = 0;
    private UserDao userDao;
    private GongXingController gongXingController;

    private UploadController uploadController;
//    private MQTTService mqttService;
//    private MyMqttCallback myMqttCallback = null;

    NetBroadcastReceiver netWorkStateReceiver;

    Intent serviceIntent = null;
    private Intent intentOne;

    private Button btnChangePW;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
        super.onCreate(savedInstanceState);
        //注入view和事件
        x.view().inject(this);
        EventBus.getDefault().register(this);

//        startService(new Intent(getApplicationContext(), MQTTService.class));//启动service
//        serviceIntent = new Intent(getApplicationContext(), MQTTService.class);
//        getApplication().startService(serviceIntent);//启动service
//
//        mqttService = MQTTService.getInstant();
//        myMqttCallback = MyMqttCallback.getInstant();

        setSupportActionBar(toolbar);
        mContext = this;
        userDao = UserDao.getInstant();
        gongXingController = GongXingController.getInstant();
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);


        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        fManager = getFragmentManager();
//        txt_message.setOnClickListener(this);
//
//        txt_notice.setOnClickListener(this);
        txt_search.setOnClickListener(this);
        txt_chat.setOnClickListener(this);
        txt_record.setOnClickListener(this);

        txt_chat.performClick();   //模拟一次点击，既进去后选择第一项

        try {
            ClientInfo clientInfo = userDao.findUser();
            if (clientInfo != null) {
                //绑定消息通道
                GongXingApplication application = (GongXingApplication) getApplication();
                application.pushService.bindAccount(clientInfo.getTopic(), new CommonCallback() {
                    @Override
                    public void onSuccess(String s) {
                        Log.d(TAG, "bind cloudchannel success");
                    }

                    @Override
                    public void onFailed(String s, String s1) {
                        Log.d(TAG, "bind cloudchannel fail");
                    }
                });

//                View headerLayout = navigationView.getHeaderView(R.layout.nav_header_main);
                View headerLayout = navigationView.getHeaderView(0);
                ImageView nav_head_portal = (ImageView) headerLayout.findViewById(R.id.nav_head_portal);
                TextView nav_head_person_name = (TextView) headerLayout.findViewById(R.id.nav_head_person_name);
                btnChangePW = (Button) headerLayout.findViewById(R.id.btnChangePW);
                btnChangePW.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent1 = new Intent(MainActivity.this, ChangePWActivity.class);
                        startActivity(intent1);
                    }
                });

                if (!StringUtil.isEmpty(clientInfo.getHeadimgurl())) {
                    ImageUtil.setImageToImageView(nav_head_portal, clientInfo.getHeadimgurl());
                }
                nav_head_person_name.setText(clientInfo.getName());
                Menu menu = navigationView.getMenu();
                navigationView.setItemTextColor(null);//@null则icon的颜色还是会随菜单的状态改变而改变
                navigationView.setItemIconTintList(null);//@null则icon的颜色还是会随菜单的状态改变而改变
                menu.getItem(0).setTitle(clientInfo.getUserID());
                menu.getItem(1).setTitle(clientInfo.getPhoneNum());
                menu.getItem(2).setTitle(clientInfo.getIDCode());
            }
        } catch (DbException e) {
            Log.e(e.getMessage(), e.getLocalizedMessage());
        }

        AppUtil.verifyStoragePermissions(this);
        intentOne = new Intent(this, DownLoadService.class);
//
//        //删除上一个mainActivity
//        GongXingApplication application = (GongXingApplication) getApplication();
//        if (application.getLastMainActivity() != null) {
//            application.getLastMainActivity().finish();
//        }
//
//        application.setLastMainActivity(this);

        uploadController = UploadController.getInstant();
        uploadController.initAboutMenu();
    }

    @Override
    protected void onResume() {
        overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
//        if (myMqttCallback != null) {
//            myMqttCallback.cleanAllNotification();
//        }
        if (netWorkStateReceiver == null) {
            netWorkStateReceiver = new NetBroadcastReceiver();
        }
//        if (oSMSBroadcastReceiver == null) {
//            oSMSBroadcastReceiver = new SMSBroadcastReceiver();
//        }
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(netWorkStateReceiver, filter);

        super.onResume();
    }

    @Override
    public void onBackPressed() {
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.action_out:
                //登出
                showMsgDialog("确定退出登录", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        gongXingController.logout();
                    }
                }, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                return true;
            case R.id.action_share:
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_SUBJECT, "分享");
                intent.putExtra(Intent.EXTRA_TEXT, "躬行监控");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                this.startActivity(Intent.createChooser(intent, "躬行监控"));
                break;
            case R.id.help:
                Intent intent1 = new Intent(MainActivity.this, HelpActivity.class);
                startActivity(intent1);
                break;
            case R.id.about:
                Intent intent3 = new Intent(MainActivity.this, AboutActivity.class);
                startActivity(intent3);
                break;
            case R.id.clear_setting:
                Intent intent2 = new Intent(MainActivity.this, SettingActivity.class);
                startActivity(intent2);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_person_name) {
            // Handle the camera action
        } else if (id == R.id.nav_mobile) {

        } else if (id == R.id.nav_idcode) {

        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View v) {
        FragmentTransaction fTransaction = fManager.beginTransaction();
        hideAllFragment(fTransaction);
        switch (v.getId()) {
            case R.id.txt_search:
                setSelected();
                txt_search.setSelected(true);
                if (searchFragment == null) {
                    searchFragment = new SearchFragment();
                    fTransaction.add(R.id.ly_content, searchFragment);
                } else {
                    fTransaction.show(searchFragment);
                }
                break;

//            case R.id.txt_message:
//                setSelected();
//                txt_message.setSelected(true);
//                if (messageFragment == null) {
//                    messageFragment = new MessageFragment();
//                    fTransaction.add(R.id.ly_content, messageFragment);
//                } else {
//                    fTransaction.show(messageFragment);
//                }
//                break;
//            case R.id.txt_notice:
//                setSelected();
//                txt_notice.setSelected(true);
//                if (noticeFragment == null) {
//                    noticeFragment = new NoticeFragment();
//                    fTransaction.add(R.id.ly_content, noticeFragment);
//                } else {
//                    fTransaction.show(noticeFragment);
//                }
//                break;

            case R.id.txt_chat:
                setSelected();
                txt_chat.setSelected(true);
                if (chatFragment == null) {
                    chatFragment = new ChatFragment();
                    fTransaction.add(R.id.ly_content, chatFragment);
                } else {
                    fTransaction.show(chatFragment);
                }
                break;
            case R.id.txt_record:
                Toast.makeText(getApplicationContext(), "报警查询页面", Toast.LENGTH_SHORT).show();
                break;
        }
        fTransaction.commit();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLogout(LogoutEvent event) {
        ClientInfo clientInfo = (ClientInfo) event.getData();
        //取消订阅
//        mqttService.unSubscribe(clientInfo.getTopic());
        if (clientInfo != null) {
            //绑定消息通道
            GongXingApplication application = (GongXingApplication) getApplication();
            application.pushService.unbindAccount(new CommonCallback() {
                @Override
                public void onSuccess(String s) {
                    Log.d(TAG, "unbindAccount cloudchannel success");
                }

                @Override
                public void onFailed(String s, String s1) {
                    Log.d(TAG, "unbindAccount cloudchannel fail");
                }
            });
        }
        Intent intent = new Intent();
        intent.setClass(mContext, RegisterAndSignInActivity.class);
        startActivity(intent);
        finish();
    }

    //隐藏所有Fragment
    private void hideAllFragment(FragmentTransaction fragmentTransaction) {
        if (searchFragment != null) fragmentTransaction.hide(searchFragment);
//        if (messageFragment != null) fragmentTransaction.hide(messageFragment);
//        if (noticeFragment != null) fragmentTransaction.hide(noticeFragment);
        if (chatFragment != null) fragmentTransaction.hide(chatFragment);
    }

    //重置所有文本的选中状态
    private void setSelected() {
//        txt_message.setSelected(false);
//
//        txt_notice.setSelected(false);
        txt_search.setSelected(false);
        txt_chat.setSelected(false);
        txt_record.setSelected(false);
    }

/*
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - exitTime) > 1000) {
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
//                System.exit(0);
            }
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
*/

    @Override
    protected void onDestroy() {
//        ClientInfo clientInfo = null;
//        try {
//            clientInfo = userDao.findUser();
//            //取消订阅
//            if(clientInfo!=null){
//                mqttService.unSubscribe(clientInfo.getTopic());
//            }
//        } catch (DbException e) {
//            e.printStackTrace();
//        }
//
//        if (serviceIntent != null) {
//            getApplication().stopService(serviceIntent);
//        }
//
//        getApplication().startService(new Intent(getApplicationContext(), MQTTService.class));//启动service
        EventBus.getDefault().unregister(this);
        unregisterReceiver(netWorkStateReceiver);
        super.onDestroy();
    }

    /**
     * 下载安装包，安装
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUpdateEvent(UpdateEvent event) {
        switch (event.getType()) {
            case UpdateEvent.newVersion:
                showMsgDialog("有新版本,请到关于页面进行下载安装",null,null);
                break;
            case UpdateEvent.begin:
                if (AppUtil.isServiceRunning(this, "com.houoy.www.gongxing.service.DownLoadService")) {
                    Toast.makeText(mContext, "已经在下载...", Toast.LENGTH_SHORT).show();
                } else {
                    intentOne.putExtra("url", ((AboutMenu) event.getData()).getUrl());
                    //连续启动Service
                    startService(intentOne);
                }
                break;
            case UpdateEvent.finish:
                stopService(intentOne);
                installApkNew(Uri.fromFile((File) event.getData()), event.getContext());
                break;
        }
    }

    //安装apk
    protected void installApkNew(Uri uri, Context context) {
        Intent intent = new Intent();
        //执行动作
        intent.setAction(Intent.ACTION_VIEW);
        //执行的数据类型
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        //不加下面这句话是可以的，查考的里面说如果不加上这句的话在apk安装完成之后点击单开会崩溃
//        android.os.Process.killProcess(android.os.Process.myPid());
        if (context != null) {
            context.startActivity(intent);
        } else {
            this.startService(intent);
        }
    }

    /**
     * 网络变化之后的类型
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNetChange(NetworkChangeEvent event) {
        Boolean hasNet = isNetConnect((Integer) event.getData());
//        if (hasNet) {
//            //如果有网络，尝试重新连接
//            mqttService.doClientConnection();
//        }
    }

    /**
     * 判断有无网络 。
     *
     * @return true 有网, false 没有网络.
     */
    public boolean isNetConnect(int netMobile) {
        if (netMobile == 1) {
            return true;
        } else if (netMobile == 0) {
            return true;
        } else if (netMobile == -1) {
            return false;

        }
        return false;
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
