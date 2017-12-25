package com.houoy.www.gongxing;

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
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.houoy.www.gongxing.adapter.ItemClickListener;
import com.houoy.www.gongxing.adapter.Section;
import com.houoy.www.gongxing.adapter.SectionedExpandableLayoutHelper;
import com.houoy.www.gongxing.mock.MockData;
import com.houoy.www.gongxing.model.messageInfo.Data;
import com.houoy.www.gongxing.model.messageInfo.DeviceInfo;
import com.houoy.www.gongxing.model.messageInfo.Place;
import com.houoy.www.gongxing.util.XUtil;
import com.houoy.www.gongxing.util.XUtilCallBack;
import com.houoy.www.gongxing.vo.RequestVO;
import com.houoy.www.gongxing.vo.ResultVO;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ContentView(R.layout.activity_main)
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    @ViewInject(R.id.recycler_view)
    private RecyclerView mRecyclerView;

    @ViewInject(R.id.toolbar)
    private Toolbar toolbar;

    @ViewInject(R.id.drawer_layout)
    private DrawerLayout drawer;

    @ViewInject(R.id.nav_view)
    private NavigationView navigationView;

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //注入view和事件
        x.view().inject(this);
//        EventBus.getDefault().register(this);

        setSupportActionBar(toolbar);
        mContext = this;
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
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

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
        if (id == R.id.action_out) {
            //注册
            String url = "http://101.201.67.36:9011/CloudWeChatPlatServer/Logout";
            Map<String, String> params = new HashMap();
            params.put("UserID", "zhangsan1");
            params.put("openid", "oSnZ8w5YmoNfZM4Fpix1gYLvGigs");
            RequestVO requestVO = new RequestVO("dff687bbfd840d3484e2091b09c8c424", params);
            String paramStr = JSON.toJSONString(requestVO);


            XUtil.Post(url, paramStr, new XUtilCallBack<String>() {
                @Override
                public void onSuccess(String result) {
                    ResultVO resultVO = JSON.parseObject(result, ResultVO.class);
                    if (resultVO.getCode().equals("success")) {
                        Toast.makeText(x.app(), resultVO.getMessage() + ":登出", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent();
                        intent.setClass(mContext, RegisterAndSignInActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(x.app(), resultVO.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            });
            return true;
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

        } else if (id == R.id.nav_group) {

        }

//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
