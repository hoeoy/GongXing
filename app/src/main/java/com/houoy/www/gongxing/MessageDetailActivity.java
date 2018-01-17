package com.houoy.www.gongxing;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.houoy.www.gongxing.adapter.ItemClickListener;
import com.houoy.www.gongxing.adapter.Section;
import com.houoy.www.gongxing.adapter.SectionedExpandableLayoutHelper;
import com.houoy.www.gongxing.controller.GongXingController;
import com.houoy.www.gongxing.event.SearchDailyMessageDataEvent;
import com.houoy.www.gongxing.event.SearchMessageDataEvent;
import com.houoy.www.gongxing.event.SearchWarningMessageDataEvent;
import com.houoy.www.gongxing.mock.MockData;
import com.houoy.www.gongxing.model.Data;
import com.houoy.www.gongxing.model.DeviceInfo;
import com.houoy.www.gongxing.model.MessagePush;
import com.houoy.www.gongxing.model.Place;
import com.houoy.www.gongxing.util.XUtil;
import com.houoy.www.gongxing.util.XUtilCallBack;
import com.houoy.www.gongxing.vo.RequestVO;
import com.houoy.www.gongxing.vo.ResultVO;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.xutils.ex.DbException;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ContentView(R.layout.activity_message_detail)
public class MessageDetailActivity extends AppCompatActivity implements ItemClickListener {

    @ViewInject(R.id.message_detail_recyclerview)
    private RecyclerView message_detail_recyclerview;
    private Context mContext;

    private GongXingController gongXingController;
    private SectionedExpandableLayoutHelper sectionedExpandableLayoutHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        EventBus.getDefault().register(this);
        mContext = this;

        //setting the recycler view
        sectionedExpandableLayoutHelper = new SectionedExpandableLayoutHelper(mContext,
                message_detail_recyclerview, this, 1);

        Intent intent = getIntent();
        MessagePush messagePush = (MessagePush) intent.getSerializableExtra("messagePush");
        ActionBar actionBar = this.getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        gongXingController = GongXingController.getInstant();
        try {
            switch (messagePush.getType()){
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
            Log.e(e.getMessage(),e.getLocalizedMessage());
            e.printStackTrace();
        }
    }


    //接收报警类消息
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onData(SearchWarningMessageDataEvent event) {
        Data data = (Data) event.getData();
        if (data == null) {

        } else {
            //清空数据
            sectionedExpandableLayoutHelper.clearData();
            //重新加载
            List<Place> places = data.getDataPart().getPlace();
            for (Place place : places) {
                sectionedExpandableLayoutHelper.addSection(place.getPlaceName(), place.getDeviceInfo());
            }

            sectionedExpandableLayoutHelper.notifyDataSetChanged();
        }
    }

    //接收日报类消息
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onData(SearchDailyMessageDataEvent event) {
        Data data = (Data) event.getData();
        if (data == null) {

        } else {
            //清空数据
            sectionedExpandableLayoutHelper.clearData();
            //重新加载
            List<Place> places = data.getDataPart().getPlace();
            for (Place place : places) {
                sectionedExpandableLayoutHelper.addSection(place.getPlaceName(), place.getDeviceInfo());
            }

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
