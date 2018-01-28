package com.houoy.www.gongxing.adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.houoy.www.gongxing.GongXingApplication;
import com.houoy.www.gongxing.model.DeviceInfo;
import com.houoy.www.gongxing.model.Place;
import com.houoy.www.gongxing.util.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by bpncool on 2/23/2016.
 */
public class SectionedExpandableLayoutHelper implements SectionStateChangeListener {

    //data list
    private Map<Section, List<DeviceInfo>> mSectionDataMap = new LinkedHashMap();

    private ArrayList<Object> mDataArrayList = new ArrayList<Object>();

    //section map
    //TODO : look for a way to avoid this
    private HashMap<String, Section> mSectionMap = new HashMap<String, Section>();

    private SectiondFooter footer = new SectiondFooter();

    //adapter
    private SectionedExpandableGridAdapter mSectionedExpandableGridAdapter;

    //recycler view
    RecyclerView mRecyclerView;

    public SectionedExpandableLayoutHelper(Context context, RecyclerView recyclerView, ItemClickListener itemClickListener,
                                           int gridSpanCount) {

        //setting the recycler view
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, gridSpanCount);
        recyclerView.setLayoutManager(gridLayoutManager);
        mSectionedExpandableGridAdapter = new SectionedExpandableGridAdapter(context, mDataArrayList, footer,
                gridLayoutManager, itemClickListener, this);
        recyclerView.setAdapter(mSectionedExpandableGridAdapter);

        mRecyclerView = recyclerView;
    }

    public void notifyDataSetChanged() {
        //TODO : handle this condition such that these functions won't be called if the recycler view is on scroll
        generateDataList();
        mSectionedExpandableGridAdapter.setFooter(footer);
        mSectionedExpandableGridAdapter.notifyDataSetChanged();
    }

    public void addItem(String section, DeviceInfo item) {
        mSectionDataMap.get(mSectionMap.get(section)).add(item);
    }

    public void removeItem(String section, DeviceInfo item) {
        mSectionDataMap.get(mSectionMap.get(section)).remove(item);
    }

    public void addSection(Place place, List<DeviceInfo> deviceInfos) {
        Section newSection;
        String state = GongXingApplication.State_normal;
        String wnum = place.getAlarmNum();
//        for (DeviceInfo deviceInfo : deviceInfos) {
//            if (deviceInfo.getState() != null && deviceInfo.getState().equals(GongXingApplication.State_normal)) {
//
//            } else {
//                wnum++;
//            }
//            List<ParaInfo> paraInfos = deviceInfo.getParaInfo();
//            for (ParaInfo paraInfo : paraInfos) {
//                if (paraInfo.getParaState() != null && paraInfo.getParaState().getName() != null
//                        && paraInfo.getParaState().getName().contains(GongXingApplication.State_warningName)) {
//                    wnum++;
//                }
//            }
//        }

        if (!StringUtil.isEmpty(wnum) && Integer.parseInt(wnum) > 0) {
            state = Integer.parseInt(wnum) + GongXingApplication.State_warning;
        }

        mSectionMap.put(place.getPlaceName(), (newSection = new Section(place.getPlaceName(), place.getTime(), state)));
        mSectionDataMap.put(newSection, deviceInfos);
    }

    public void removeSection(String section) {
        mSectionDataMap.remove(mSectionMap.get(section));
        mSectionMap.remove(section);
    }

    public void setFooter(SectiondFooter footer) {
        this.footer = footer;
    }

    public void removeFooter(String section) {
        footer = new SectiondFooter();
    }

    public void clearData() {
        mSectionDataMap.clear();
        mSectionMap.clear();
        footer = new SectiondFooter();
    }

    private void generateDataList() {
        mDataArrayList.clear();
        for (Map.Entry<Section, List<DeviceInfo>> entry : mSectionDataMap.entrySet()) {
            Section key;
            mDataArrayList.add((key = entry.getKey()));
            if (key.isExpanded) {
                mDataArrayList.addAll(entry.getValue());
            }
        }
    }

    @Override
    public void onSectionStateChanged(Section section, boolean isOpen) {
        if (!mRecyclerView.isComputingLayout()) {
            section.isExpanded = isOpen;
            notifyDataSetChanged();
        }
    }
}
