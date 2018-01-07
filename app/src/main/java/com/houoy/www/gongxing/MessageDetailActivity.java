package com.houoy.www.gongxing;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.houoy.www.gongxing.adapter.ItemClickListener;
import com.houoy.www.gongxing.adapter.Section;
import com.houoy.www.gongxing.adapter.SectionedExpandableLayoutHelper;
import com.houoy.www.gongxing.mock.MockData;
import com.houoy.www.gongxing.model.Data;
import com.houoy.www.gongxing.model.DeviceInfo;
import com.houoy.www.gongxing.model.MessagePush;
import com.houoy.www.gongxing.model.Place;
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

@ContentView(R.layout.activity_message_detail)
public class MessageDetailActivity extends AppCompatActivity implements ItemClickListener {

    @ViewInject(R.id.message_detail_recyclerview)
    private RecyclerView message_detail_recyclerview;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //onCreate方法中
        ActionBar actionBar = this.getSupportActionBar();
        actionBar.setTitle("消息详细");
        actionBar.setDisplayHomeAsUpEnabled(true);

        x.view().inject(this);
//        EventBus.getDefault().register(this);
        mContext = this;

        //setting the recycler view
        SectionedExpandableLayoutHelper sectionedExpandableLayoutHelper = new SectionedExpandableLayoutHelper(mContext,
                message_detail_recyclerview, this, 1);

        Intent intent = getIntent();
        MessagePush messagePush = (MessagePush) intent.getSerializableExtra("messagePush");
        Data data = initData();
        List<Place> places = data.getDataPart().getPlace();
        for (Place place : places) {
            sectionedExpandableLayoutHelper.addSection(place.getPlaceName(), place.getDeviceInfo());
        }

        sectionedExpandableLayoutHelper.notifyDataSetChanged();
    }

    private Data initData() {
        //random data
        //注册
        String url = "http://101.201.67.36:9011/CloudWeChatPlatServer/MessageDetail";
        Map<String, String> params = new HashMap();
        params.put("touser", "oSnZ8w5YmoNfZM4Fpix1gYLvGigs");
        params.put("RelationID", "123");
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

        //mock data
        Data data = MockData.getInfoData();
        return data;
    }


    @Override
    public void itemClicked(DeviceInfo deviceInfo) {
        Toast.makeText(mContext, "DeviceInfo: " + deviceInfo.getDeviceName() + " clicked", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void itemClicked(Section section) {
        Toast.makeText(mContext, "Section: " + section.getName() + " clicked", Toast.LENGTH_SHORT).show();
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
//        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
