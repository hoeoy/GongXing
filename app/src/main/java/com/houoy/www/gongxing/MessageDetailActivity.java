package com.houoy.www.gongxing;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;

import com.houoy.www.gongxing.adapter.ItemClickListener;
import com.houoy.www.gongxing.adapter.Section;
import com.houoy.www.gongxing.adapter.SectiondFooter;
import com.houoy.www.gongxing.adapter.SectionedExpandableLayoutHelper;
import com.houoy.www.gongxing.controller.GongXingController;
import com.houoy.www.gongxing.event.AffirmOperateEvent;
import com.houoy.www.gongxing.event.SearchDailyMessageDataEvent;
import com.houoy.www.gongxing.event.SearchWarningMessageDataEvent;
import com.houoy.www.gongxing.model.Data;
import com.houoy.www.gongxing.model.DeviceInfo;
import com.houoy.www.gongxing.model.MessagePush;
import com.houoy.www.gongxing.model.Place;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.xutils.ex.DbException;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;

@ContentView(R.layout.activity_message_detail)
public class MessageDetailActivity extends AppCompatActivity implements ItemClickListener {

    @ViewInject(R.id.message_detail_recyclerview)
    private RecyclerView message_detail_recyclerview;
    private Context mContext;

    private GongXingController gongXingController;
    private SectionedExpandableLayoutHelper sectionedExpandableLayoutHelper;
    private ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        EventBus.getDefault().register(this);
        mContext = this;

        //setting the recycler view
        sectionedExpandableLayoutHelper = new SectionedExpandableLayoutHelper(mContext,
                message_detail_recyclerview, this, 1);
        actionBar = this.getSupportActionBar();
        gongXingController = GongXingController.getInstant();
        actionBar.setDisplayHomeAsUpEnabled(true);

        refreshData();
    }

    private void refreshData() {
        Intent intent = getIntent();
        MessagePush messagePush = (MessagePush) intent.getSerializableExtra("messagePush");

        try {
            switch (messagePush.getType()) {
                case "1"://日报
                    actionBar.setTitle("日报");
                    gongXingController.queryDailyData(messagePush);
                    break;
                case "2"://告警
                    actionBar.setTitle("报警");
                    gongXingController.queryWarningData(messagePush);
                    break;
            }
        } catch (DbException e) {
            Log.e(e.getMessage(), e.getLocalizedMessage());
            e.printStackTrace();
        }
    }

    //当回复报警消息成功，需要刷新界面
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAffirmOperateEvent(AffirmOperateEvent event) {
        refreshData();
    }

    //接收报警类消息
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onData(SearchWarningMessageDataEvent event) {
        Data data = (Data) event.getData();
        onData(data, "2");
    }

    //接收日报类消息
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onData(SearchDailyMessageDataEvent event) {
        Data data = (Data) event.getData();
        onData(data, "3");
    }

    private void onData(Data data, String type) {
        if (data == null) {

        } else {
            //清空数据
            sectionedExpandableLayoutHelper.clearData();
            //重新加载
            List<Place> places = data.getDataPart().getPlace();
            for (Place place : places) {
                sectionedExpandableLayoutHelper.addSection(place, place.getDeviceInfo());
            }
            SectiondFooter sectiondFooter = new SectiondFooter();
            sectiondFooter.setOperatePart(data.getOperatePart());
            sectiondFooter.setRemarkPart(data.getRemarkPart());
            sectiondFooter.setClientInfo(data.getClientInfo());
            sectiondFooter.setType(type);
            sectionedExpandableLayoutHelper.setFooter(sectiondFooter);

            sectionedExpandableLayoutHelper.notifyDataSetChanged();
        }
    }

    @Override
    public void itemClicked(DeviceInfo deviceInfo) {
//        Toast.makeText(mContext, "DeviceInfo: " + deviceInfo.getDeviceName() + " clicked", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void itemClicked(Section section) {
        section.isExpanded = !section.isExpanded;
        sectionedExpandableLayoutHelper.onSectionStateChanged(section, section.isExpanded);
//        Toast.makeText(mContext, "Section: " + section.getName() + " clicked", Toast.LENGTH_SHORT).show();
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
}
