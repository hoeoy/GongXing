package com.houoy.www.gongxing;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.MenuItem;

import com.houoy.www.gongxing.adapter.MessageAdapter;
import com.houoy.www.gongxing.dao.HouseDao;
import com.houoy.www.gongxing.dao.UserDao;
import com.houoy.www.gongxing.event.RefreshChatEvent;
import com.houoy.www.gongxing.event.RefreshMessageEvent;
import com.houoy.www.gongxing.model.ChatHouse;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.xutils.ex.DbException;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

@ContentView(R.layout.activity_message)
public class MessageActivity extends MyAppCompatActivity {
    public static final String intentStr = "house";
    @ViewInject(R.id.notice_messageList)
    private RecyclerView notice_messageList;

    @ViewInject(R.id.notice_swiperefreshlayout)
    private SwipeRefreshLayout notice_swiperefreshlayout;

    private MessageAdapter adapter;

    private Integer lastVisibleItem;

    private Context mContext;
    private ActionBar actionBar;
    private ChatHouse chatHouse;
    private HouseDao houseDao;
    private UserDao userDao;
    private LinearLayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        EventBus.getDefault().register(this);
        mContext = this;
        houseDao = HouseDao.getInstant();
        userDao = UserDao.getInstant();
        actionBar = this.getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        chatHouse = (ChatHouse) getIntent().getSerializableExtra(intentStr);
        actionBar.setTitle(chatHouse.getHouse_name());


        layoutManager = new LinearLayoutManager(mContext);
//        layoutManager.setStackFromEnd(true);//列表再底部开始展示，反转后由上面开始展示
        layoutManager.setReverseLayout(true);//列表翻转
        layoutManager.setAutoMeasureEnabled(true);

        notice_messageList.setLayoutManager(layoutManager);
        adapter = new MessageAdapter(mContext, chatHouse);
        notice_messageList.setAdapter(adapter);



        notice_swiperefreshlayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                notice_swiperefreshlayout.setRefreshing(false);
//                refresh(null);
//                adapter.initData(0);
//                notice_swiperefreshlayout.setRefreshing(false);
//                Toast.makeText(container.getContext(), "获取消息成功", Toast.LENGTH_SHORT).show();
            }
        });

        notice_messageList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (lastVisibleItem + 1 == adapter.getItemCount()) {
                        //如果到最后一个则加载更多
                        adapter.changeMoreStatus(MessageAdapter.LOADING_MORE);
                        adapter.pushData(lastVisibleItem);
                        adapter.changeMoreStatus(MessageAdapter.NOTHING);
                    } else if (lastVisibleItem <= 1) {
                        //如果到第一个则刷新
                        refresh(null);
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = layoutManager.findLastVisibleItemPosition();
            }
        });

//        Intent intent = getIntent();
//        ChatHouse chatHouse = (ChatHouse) intent.getSerializableExtra(MessageActivity.intentStr);
//        if (chatHouse != null) {
//            intent = new Intent();
//            intent.setClass(mContext, MessageActivity.class);
//            intent.putExtra(MessageActivity.intentStr, chatHouse);
//            mContext.startActivity(intent);
//        }
    }

//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void getMqttMessage(MessageVO messageVO) {
//        Toast.makeText(adapter.context, "收到消息:" + messageVO.getTitle_value(), Toast.LENGTH_SHORT).show();
//        //刷新
//        refresh(null);
//    }

    @Override
    protected void onResume() {
        overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
        super.onResume();
        chatHouse = (ChatHouse) getIntent().getSerializableExtra(intentStr);
        adapter.setChatHouse(chatHouse);
        actionBar.setTitle(chatHouse.getHouse_name());
        //更新unreadnum
        try {
            chatHouse = houseDao.findByNameAndUserid(chatHouse.getHouse_name(), chatHouse.getUserid());
            chatHouse.setUnread_num(0);
            houseDao.update(chatHouse);
            EventBus.getDefault().post(new RefreshChatEvent("", ""));
        } catch (DbException e) {
            e.printStackTrace();
        }
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
            gotoMain();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            gotoMain();
            return false;
        }

        return super.onKeyUp(keyCode, event);
    }

    private void gotoMain() {
//    Stack<Activity> activities = activityPool.getActivityStack();
        Intent intent = new Intent();
        intent.setClass(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
        finish();
//        overridePendingTransition(R.anim.slide_left_out, R.anim.slide_right_in);
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    public ChatHouse getChatHouse() {
        return chatHouse;
    }

    public LinearLayoutManager getLayoutManager() {
        return layoutManager;
    }
}
