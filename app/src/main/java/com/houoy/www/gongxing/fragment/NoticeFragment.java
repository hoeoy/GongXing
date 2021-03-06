//package com.houoy.www.gongxing.fragment;
//
//import android.app.Fragment;
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.support.v4.widget.SwipeRefreshLayout;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Toast;
//
//import com.houoy.www.gongxing.R;
//import com.houoy.www.gongxing.adapter.MessageListAdapter;
//import com.houoy.www.gongxing.event.RefreshMessageEvent;
//import com.houoy.www.gongxing.vo.MessageVO;
//
//import org.greenrobot.eventbus.EventBus;
//import org.greenrobot.eventbus.Subscribe;
//import org.greenrobot.eventbus.ThreadMode;
//import org.xutils.view.annotation.ContentView;
//import org.xutils.view.annotation.ViewInject;
//import org.xutils.x;
//
//@ContentView(R.layout.fragment_notice)
//public class NoticeFragment extends Fragment {
//    private boolean injected = false;
//
//    @ViewInject(R.id.notice_messageList)
//    private RecyclerView notice_messageList;
//
//    @ViewInject(R.id.notice_swiperefreshlayout)
//    private SwipeRefreshLayout notice_swiperefreshlayout;
//
//    private MessageListAdapter adapter;
//
//    private Integer lastVisibleItem;
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
//        injected = true;
//        View view = x.view().inject(this, inflater, container); //使用注解模块一定要注意初始化视图注解框架
//
//        adapter = new MessageListAdapter(container.getContext());
//        final LinearLayoutManager layoutManager = new LinearLayoutManager(container.getContext());
//        notice_messageList.setLayoutManager(layoutManager);
//        notice_messageList.setAdapter(adapter);
//
//        notice_swiperefreshlayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                refresh(null);
////                adapter.initData(0);
////                notice_swiperefreshlayout.setRefreshing(false);
////                Toast.makeText(container.getContext(), "获取消息成功", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        notice_messageList.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 == adapter.getItemCount()) {
//                    adapter.changeMoreStatus(MessageListAdapter.LOADING_MORE);
//                    adapter.pushData(lastVisibleItem);
//                    adapter.changeMoreStatus(MessageListAdapter.NOTHING);
//                }
//            }
//
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//                lastVisibleItem = layoutManager.findLastVisibleItemPosition();
//            }
//        });
//
//        return view;
//    }
//
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void getMqttMessage(MessageVO messageVO) {
//        Toast.makeText(adapter.context, "收到消息:" + messageVO.getTitle_value(), Toast.LENGTH_SHORT).show();
//    }
//
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void refresh(RefreshMessageEvent refreshMessageEvent) {
//        adapter.initData(0);
//        notice_swiperefreshlayout.setRefreshing(false);
//        adapter.notifyDataSetChanged();
//    }
//
//    @Override
//    public void onViewCreated(View v, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(v, savedInstanceState);
//        EventBus.getDefault().register(this);
//        if (!injected) {
//            x.view().inject(this, this.getView());
//        }
//    }
//
//    @Override
//    public void onDestroyView() {
//        EventBus.getDefault().unregister(this);
//        super.onDestroyView();
//    }
//}
