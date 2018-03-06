package com.houoy.www.gongxing;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.Toast;

import com.houoy.www.gongxing.adapter.MessageAdapter;
import com.houoy.www.gongxing.event.RefreshMessageEvent;
import com.houoy.www.gongxing.model.ChatHouse;
import com.houoy.www.gongxing.vo.MessageVO;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

@ContentView(R.layout.activity_message)
public class MessageActivity extends AppCompatActivity {
    public static final String intentStr = "house";
    @ViewInject(R.id.notice_messageList)
    private RecyclerView notice_messageList;

    @ViewInject(R.id.notice_swiperefreshlayout)
    private SwipeRefreshLayout notice_swiperefreshlayout;

    private MessageAdapter adapter;

    private Integer lastVisibleItem;

    private Context mContext;
    private ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        EventBus.getDefault().register(this);
        mContext = this;

        actionBar = this.getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        ChatHouse chatHouse = (ChatHouse) getIntent().getSerializableExtra(intentStr);
        actionBar.setTitle(chatHouse.getHouse_name());

        adapter = new MessageAdapter(mContext, chatHouse);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        notice_messageList.setLayoutManager(layoutManager);
        notice_messageList.setAdapter(adapter);

        notice_swiperefreshlayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh(null);
//                adapter.initData(0);
//                notice_swiperefreshlayout.setRefreshing(false);
//                Toast.makeText(container.getContext(), "获取消息成功", Toast.LENGTH_SHORT).show();
            }
        });

        notice_messageList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 == adapter.getItemCount()) {
                    adapter.changeMoreStatus(MessageAdapter.LOADING_MORE);
                    adapter.pushData(lastVisibleItem);
                    adapter.changeMoreStatus(MessageAdapter.NOTHING);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = layoutManager.findLastVisibleItemPosition();
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getMqttMessage(MessageVO messageVO) {
        Toast.makeText(adapter.context, "收到消息:" + messageVO.getTitle_value(), Toast.LENGTH_SHORT).show();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refresh(RefreshMessageEvent refreshMessageEvent) {
        adapter.initData(0);
        notice_swiperefreshlayout.setRefreshing(false);
        adapter.notifyDataSetChanged();
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
