package com.houoy.www.gongxing;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.houoy.www.gongxing.controller.GongXingController;
import com.houoy.www.gongxing.dao.UserDao;
import com.houoy.www.gongxing.event.LogoutEvent;
import com.houoy.www.gongxing.fragment.ChatFragment;
import com.houoy.www.gongxing.fragment.MessageFragment;
import com.houoy.www.gongxing.fragment.NoticeFragment;
import com.houoy.www.gongxing.fragment.SearchFragment;
import com.houoy.www.gongxing.model.ClientInfo;
import com.houoy.www.gongxing.service.MQTTService;
import com.houoy.www.gongxing.util.ImageUtil;
import com.houoy.www.gongxing.util.StringUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.xutils.ex.DbException;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

@ContentView(R.layout.activity_main)
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

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
    @ViewInject(R.id.txt_message)
    private TextView txt_message;
    @ViewInject(R.id.txt_search)
    private TextView txt_search;
    @ViewInject(R.id.txt_notice)
    private TextView txt_notice;
    @ViewInject(R.id.ly_content)
    private FrameLayout ly_content;

    //Fragment Object
    private MessageFragment messageFragment;
    private ChatFragment chatFragment;
    private NoticeFragment noticeFragment;
    private SearchFragment searchFragment;
    private FragmentManager fManager;

    private Context mContext;
    private long exitTime = 0;
    private UserDao userDao;
    private GongXingController gongXingController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
        super.onCreate(savedInstanceState);
        //注入view和事件
        x.view().inject(this);
        EventBus.getDefault().register(this);

        getApplication().startService(new Intent(getApplicationContext(), MQTTService.class));//启动service

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
        txt_message.setOnClickListener(this);
        txt_search.setOnClickListener(this);
        txt_notice.setOnClickListener(this);
        txt_chat.setOnClickListener(this);

        txt_search.performClick();   //模拟一次点击，既进去后选择第一项

        try {
            ClientInfo clientInfo = userDao.findUser();
            if (clientInfo != null) {
//                View headerLayout = navigationView.getHeaderView(R.layout.nav_header_main);
                View headerLayout = navigationView.getHeaderView(0);
                ImageView nav_head_portal = (ImageView) headerLayout.findViewById(R.id.nav_head_portal);
                TextView nav_head_person_name = (TextView) headerLayout.findViewById(R.id.nav_head_person_name);

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

        //删除上一个mainActivity
        GongXingApplication application = (GongXingApplication) getApplication();
        if (application.getLastMainActivity() != null) {
            application.getLastMainActivity().finish();
        }

        application.setLastMainActivity(this);
    }

    @Override
    protected void onResume() {
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
                gongXingController.logout();
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
            case R.id.txt_message:
                setSelected();
                txt_message.setSelected(true);
                if (messageFragment == null) {
                    messageFragment = new MessageFragment();
                    fTransaction.add(R.id.ly_content, messageFragment);
                } else {
                    fTransaction.show(messageFragment);
                }
                break;
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
            case R.id.txt_notice:
                setSelected();
                txt_notice.setSelected(true);
                if (noticeFragment == null) {
                    noticeFragment = new NoticeFragment();
                    fTransaction.add(R.id.ly_content, noticeFragment);
                } else {
                    fTransaction.show(noticeFragment);
                }
                break;
        }
        fTransaction.commit();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLogout(LogoutEvent event) {
        Intent intent = new Intent();
        intent.setClass(mContext, RegisterAndSignInActivity.class);
        startActivity(intent);
        this.finish();
    }

    //隐藏所有Fragment
    private void hideAllFragment(FragmentTransaction fragmentTransaction) {
        if (searchFragment != null) fragmentTransaction.hide(searchFragment);
        if (messageFragment != null) fragmentTransaction.hide(messageFragment);
        if (noticeFragment != null) fragmentTransaction.hide(noticeFragment);
        if (chatFragment != null) fragmentTransaction.hide(chatFragment);
    }

    //重置所有文本的选中状态
    private void setSelected() {
        txt_message.setSelected(false);
        txt_search.setSelected(false);
        txt_notice.setSelected(false);
        txt_chat.setSelected(false);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - exitTime) > 1000) {
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
