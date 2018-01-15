package com.houoy.www.gongxing.fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.houoy.www.gongxing.R;
import com.houoy.www.gongxing.adapter.ItemClickListener;
import com.houoy.www.gongxing.adapter.Section;
import com.houoy.www.gongxing.adapter.SectionedExpandableLayoutHelper;
import com.houoy.www.gongxing.controller.GongXingController;
import com.houoy.www.gongxing.dao.GongXingDao;
import com.houoy.www.gongxing.event.SearchMessageDataEvent;
import com.houoy.www.gongxing.model.Data;
import com.houoy.www.gongxing.model.DeviceInfo;
import com.houoy.www.gongxing.model.Place;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
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

    private GongXingDao gongXingDao;
    private GongXingController gongXingController;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        injected = true;
        View view = x.view().inject(this, inflater, container); //使用注解模块一定要注意初始化视图注解框架
        EventBus.getDefault().register(this);
        mContext = container.getContext();
        //setting the recycler view
        sectionedExpandableLayoutHelper = new SectionedExpandableLayoutHelper(mContext, mRecyclerView, this, 1);
        gongXingController = GongXingController.getInstant();
        gongXingController.queryData();
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
            List<Place> places = data.getDataPart().getPlace();
            for (Place place : places) {
                sectionedExpandableLayoutHelper.addSection(place.getPlaceName(), place.getDeviceInfo());
            }

            sectionedExpandableLayoutHelper.notifyDataSetChanged();
        }
    }

    @Override
    public void itemClicked(DeviceInfo deviceInfo) {
        Toast.makeText(mContext, "DeviceInfo: " + deviceInfo.getDeviceName() + " clicked", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void itemClicked(Section section) {
        section.isExpanded = !section.isExpanded;
        sectionedExpandableLayoutHelper.onSectionStateChanged(section, section.isExpanded);
    }
}
