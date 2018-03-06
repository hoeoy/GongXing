package com.houoy.www.gongxing.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.houoy.www.gongxing.R;
import com.houoy.www.gongxing.adapter.ChatListAdapter;
import com.houoy.www.gongxing.element.SwipeItemLayout;
import com.houoy.www.gongxing.event.RefreshMessageEvent;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

@ContentView(R.layout.fragment_chat)
public class ChatFragment extends Fragment {
    private boolean injected = false;

    @ViewInject(R.id.chatList)
    private RecyclerView chatList;

    @ViewInject(R.id.chat_swiperefreshlayout)
    private SwipeRefreshLayout chat_swiperefreshlayout;

    private ChatListAdapter chatListAdapter;

    private Integer lastVisibleItem;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        injected = true;
        View view = x.view().inject(this, inflater, container); //使用注解模块一定要注意初始化视图注解框架

        chatListAdapter = new ChatListAdapter(container.getContext());
        final LinearLayoutManager layoutManager = new LinearLayoutManager(container.getContext());
        chatList.setLayoutManager(layoutManager);

        chatList.addOnItemTouchListener(new SwipeItemLayout.OnSwipeItemTouchListener(container.getContext()));
        chatList.setAdapter(chatListAdapter);
        chatList.addItemDecoration(new DividerItemDecoration(container.getContext(), LinearLayoutManager.VERTICAL));

        chat_swiperefreshlayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh(null);
            }
        });

        return view;
    }

    //    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refresh(RefreshMessageEvent refreshMessageEvent) {
        chatListAdapter.initData(0);
        chat_swiperefreshlayout.setRefreshing(false);
        chatListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onViewCreated(View v, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);
        // EventBus.getDefault().register(this);
        if (!injected) {
            x.view().inject(this, this.getView());
        }
    }

    @Override
    public void onDestroyView() {
        //  EventBus.getDefault().unregister(this);
        super.onDestroyView();
    }
}
