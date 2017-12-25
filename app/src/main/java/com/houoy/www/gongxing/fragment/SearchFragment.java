package com.houoy.www.gongxing.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.houoy.www.gongxing.R;
import com.houoy.www.gongxing.RegisterAndSignInActivity;
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

@ContentView(R.layout.fragment_search)
public class SearchFragment extends Fragment implements ItemClickListener {
    private boolean injected = false;
    @ViewInject(R.id.recycler_view)
    private RecyclerView mRecyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        injected = true;
        View view = x.view().inject(this, inflater, container); //使用注解模块一定要注意初始化视图注解框架
        //setting the recycler view
        SectionedExpandableLayoutHelper sectionedExpandableLayoutHelper = new SectionedExpandableLayoutHelper(this.getContext(),
                mRecyclerView, this, 1);


        Data data = initData();
        List<Place> places = data.getDataPart().getPlace();
        for (Place place : places) {
            sectionedExpandableLayoutHelper.addSection(place.getPlaceName(), place.getDeviceInfo());
        }

        sectionedExpandableLayoutHelper.notifyDataSetChanged();
        return view;
    }

    @Override
    public void onViewCreated(View v, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);
        if (!injected) {
            x.view().inject(this, this.getView());
        }
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
        final Context mContext = this.getContext();
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
        Toast.makeText(this.getContext(), "DeviceInfo: " + deviceInfo.getDeviceName() + " clicked", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void itemClicked(Section section) {
        Toast.makeText(this.getContext(), "Section: " + section.getName() + " clicked", Toast.LENGTH_SHORT).show();
    }
}
