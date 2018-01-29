package com.houoy.www.gongxing.fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.houoy.www.gongxing.R;
import com.houoy.www.gongxing.adapter.ItemClickListener;
import com.houoy.www.gongxing.adapter.Section;
import com.houoy.www.gongxing.adapter.SectiondFooter;
import com.houoy.www.gongxing.adapter.SectionedExpandableLayoutHelper;
import com.houoy.www.gongxing.controller.GongXingController;
import com.houoy.www.gongxing.event.SearchMessageDataEvent;
import com.houoy.www.gongxing.model.Data;
import com.houoy.www.gongxing.model.DeviceInfo;
import com.houoy.www.gongxing.model.Place;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.xutils.ex.DbException;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;

@ContentView(R.layout.fragment_search)
public class SearchFragment extends Fragment implements ItemClickListener {
    private boolean injected = false;
    @ViewInject(R.id.recycler_view)
    private RecyclerView mRecyclerView;
    private Context mContext;
    private SectionedExpandableLayoutHelper sectionedExpandableLayoutHelper;

    private GongXingController gongXingController;

    @ViewInject(R.id.searchSwipeRefreshLayout)
    private SwipeRefreshLayout searchSwipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        injected = true;
        View view = x.view().inject(this, inflater, container); //使用注解模块一定要注意初始化视图注解框架
        EventBus.getDefault().register(this);
        mContext = container.getContext();
        //setting the recycler view
        sectionedExpandableLayoutHelper = new SectionedExpandableLayoutHelper(mContext, mRecyclerView, this, 1);
        gongXingController = GongXingController.getInstant();
        try {
            gongXingController.queryData();
        } catch (DbException e) {
            Log.e(e.getMessage(), e.getLocalizedMessage());
            e.printStackTrace();
        }

        searchSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                try {
                    gongXingController.queryData();
//                    searchSwipeRefreshLayout.setRefreshing(false);
//                    Toast.makeText(container.getContext(), "查询成功", Toast.LENGTH_SHORT).show();
                } catch (DbException e) {
                    Log.e(e.getMessage(), e.getLocalizedMessage());
                    e.printStackTrace();
                }
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(View v, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);
        if (!injected) {
            x.view().inject(this, this.getView());
        }
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onData(SearchMessageDataEvent event) {
        Data data = (Data) event.getData();
        if (data == null) {

        } else {
            //清空数据
            sectionedExpandableLayoutHelper.clearData();
            //重新加载
            List<Place> places = data.getDataPart().getPlace();
            if (places != null) {
                if (places.size() == 1) {
                    Place place = places.get(0);
                    place.setIsExpanded(true);//只有一个区域，默认展开
                    sectionedExpandableLayoutHelper.addSection(place, place.getDeviceInfo());
                } else if (places.size() > 1) {
                    for (Place place : places) {//有多个区域的话，只展开所有报警的。
                        sectionedExpandableLayoutHelper.addSection(place, place.getDeviceInfo());
                    }
                } else {//为0

                }
            } else {

            }

            SectiondFooter sectiondFooter = new SectiondFooter();
            sectiondFooter.setOperatePart(data.getOperatePart());
            sectiondFooter.setRemarkPart(data.getRemarkPart());
            sectiondFooter.setClientInfo(data.getClientInfo());
            sectiondFooter.setType("1");
            sectionedExpandableLayoutHelper.setFooter(sectiondFooter);

            sectionedExpandableLayoutHelper.notifyDataSetChanged();
        }searchSwipeRefreshLayout.setRefreshing(false);

    }

    @Override
    public void itemClicked(DeviceInfo deviceInfo) {
//        Toast.makeText(mContext, "DeviceInfo: " + deviceInfo.getDeviceName() + " clicked", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void itemClicked(Section section) {
        section.isExpanded = !section.isExpanded;
        sectionedExpandableLayoutHelper.onSectionStateChanged(section, section.isExpanded);
    }



}
