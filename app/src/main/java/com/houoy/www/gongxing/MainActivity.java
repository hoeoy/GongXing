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
import com.houoy.www.gongxing.dao.GongXingDao;
import com.houoy.www.gongxing.event.LogoutEvent;
import com.houoy.www.gongxing.fragment.MessageFragment;
import com.houoy.www.gongxing.fragment.SearchFragment;
import com.houoy.www.gongxing.model.ClientInfo;
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


    @ViewInject(R.id.txt_channel)
    private TextView txt_channel;
    @ViewInject(R.id.txt_message)
    private TextView txt_message;
    @ViewInject(R.id.ly_content)
    private FrameLayout ly_content;

    //Fragment Object
    private MessageFragment messageFragment;
    private SearchFragment searchFragment;
    private FragmentManager fManager;

    private Context mContext;
    private long exitTime = 0;
    private GongXingDao gongXingDao;
    private GongXingController gongXingController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //注入view和事件
        x.view().inject(this);
        EventBus.getDefault().register(this);

        setSupportActionBar(toolbar);
        mContext = this;
        gongXingDao = GongXingDao.getInstant();
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
        txt_channel.setOnClickListener(this);
        txt_message.setOnClickListener(this);
        txt_channel.performClick();   //模拟一次点击，既进去后选择第一项

        try {
            ClientInfo clientInfo = gongXingDao.findUser();
            if(clientInfo != null){
//                View headerLayout = navigationView.getHeaderView(R.layout.nav_header_main);
                View headerLayout = navigationView.getHeaderView(0);
                ImageView nav_head_portal = (ImageView) headerLayout.findViewById(R.id.nav_head_portal);
                TextView nav_head_person_name = (TextView) headerLayout.findViewById(R.id.nav_head_person_name);

                if(!StringUtil.isEmpty(clientInfo.getHeadimgurl())){
                    ImageUtil.setImageToImageView(nav_head_portal,clientInfo.getHeadimgurl());
                }
                nav_head_person_name.setText(clientInfo.getName());
                Menu menu = navigationView.getMenu();
                menu.getItem(0).setTitle(clientInfo.getUserID());
                menu.getItem(1).setTitle(clientInfo.getPhoneNum());
                menu.getItem(2).setTitle(clientInfo.getIDCode());
            }
        } catch (DbException e) {
            Log.e(e.getMessage(),e.getLocalizedMessage());
        }
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
            case R.id.clear_cache:
                try {
                    gongXingDao.clearMessagePush();
                    Toast.makeText(getApplicationContext(), "清除缓存成功", Toast.LENGTH_SHORT).show();
                } catch (DbException e) {
                    e.printStackTrace();
                }
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
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    @Override
    public void onClick(View v) {
        FragmentTransaction fTransaction = fManager.beginTransaction();
        hideAllFragment(fTransaction);
        switch (v.getId()) {
            case R.id.txt_channel:
                setSelected();
                txt_channel.setSelected(true);
                if (messageFragment == null) {
                    messageFragment = new MessageFragment();
                    fTransaction.add(R.id.ly_content, messageFragment);
                } else {
                    fTransaction.show(messageFragment);
                }
                break;
            case R.id.txt_message:
                setSelected();
                txt_message.setSelected(true);
                if (searchFragment == null) {
                    searchFragment = new SearchFragment();
                    fTransaction.add(R.id.ly_content, searchFragment);
                } else {
                    fTransaction.show(searchFragment);
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
    }

    //重置所有文本的选中状态
    private void setSelected() {
        txt_channel.setSelected(false);
        txt_message.setSelected(false);
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
