package com.houoy.www.gongxing.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.houoy.www.gongxing.R;
import com.houoy.www.gongxing.adapter.MessageListAdapter;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

@ContentView(R.layout.fragment_message)
public class MessageFragment extends Fragment {
    private boolean injected = false;

    @ViewInject(R.id.messageList)
    private RecyclerView messageList;

    private MessageListAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        injected = true;
        View view = x.view().inject(this, inflater, container); //使用注解模块一定要注意初始化视图注解框架

        adapter=new MessageListAdapter(container.getContext());
        LinearLayoutManager layoutManager=new LinearLayoutManager(container.getContext());
        messageList.setLayoutManager(layoutManager);
        messageList.setAdapter(adapter);


        return view;
    }

    @Override
    public void onViewCreated(View v, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);
        if (!injected) {
            x.view().inject(this, this.getView());
        }
    }
}
