package com.houoy.www.gongxing;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.TextView;

import com.houoy.www.gongxing.adapter.AboutAdapter;
import com.houoy.www.gongxing.event.UpdateEvent;
import com.houoy.www.gongxing.util.AppUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

@ContentView(R.layout.activity_about)
public class AboutActivity extends MyAppCompatActivity {
    private ActionBar actionBar;
    private AboutAdapter adapter;
    private LinearLayoutManager layoutManager;

    @ViewInject(R.id.aboutMenuList)
    private RecyclerView aboutMenuList;

    @ViewInject(R.id.aboutTitle)
    private TextView aboutTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        EventBus.getDefault().register(this);
        actionBar = this.getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(getString(R.string.about));
        String appCurrentName = AppUtil.getVersionName(GongXingApplication.gongXingApplication.getBaseContext());
        aboutTitle.setText("躬行监控" + appCurrentName);

        adapter = new AboutAdapter(this, "");
        layoutManager = new LinearLayoutManager(this);
        aboutMenuList.setLayoutManager(layoutManager);
        aboutMenuList.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    /**
     * 初始化数据
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUpdateEvent(UpdateEvent event) {
        switch (event.getType()) {
            case UpdateEvent.checkVersion:
                adapter.updateData();
                break;
        }
    }
}
