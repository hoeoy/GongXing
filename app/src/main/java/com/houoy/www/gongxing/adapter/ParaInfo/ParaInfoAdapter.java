package com.houoy.www.gongxing.adapter.ParaInfo;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.houoy.www.gongxing.R;
import com.houoy.www.gongxing.model.ParaInfo;

import java.util.List;

/**
 * Created by lenovo on 2/23/2016.
 */
public class ParaInfoAdapter extends RecyclerView.Adapter<ParaInfoAdapter.ViewHolder> {

    //data array
    private List<ParaInfo> mDataArrayList;


    public ParaInfoAdapter(List<ParaInfo> dataArrayList) {
        mDataArrayList = dataArrayList;

//        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
//            @Override
//            public int getSpanSize(int position) {
//                return isSection(position) ? gridLayoutManager.getSpanCount() : 1;
//            }
//        });
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item2_parainfo, parent, false), viewType);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final ParaInfo item = (ParaInfo) mDataArrayList.get(position);
        holder.paraNameTextView.setText(item.getParaName().getName());
        holder.paraValueTextView.setText(item.getParaValue().getName());
        holder.paraStateTextView.setText(item.getParaState().getName());
    }

    @Override
    public int getItemCount() {
        return mDataArrayList.size();
    }


    protected static class ViewHolder extends RecyclerView.ViewHolder {

        //common
        View view;

        //for section
        TextView paraNameTextView;
        TextView paraValueTextView;
        TextView paraStateTextView;

        public ViewHolder(View view, int viewType) {
            super(view);
            this.view = view;
            paraNameTextView = (TextView) view.findViewById(R.id.paraName);
            paraValueTextView = (TextView) view.findViewById(R.id.paraValue);
            paraStateTextView = (TextView) view.findViewById(R.id.paraState);
        }
    }
}
