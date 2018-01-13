package com.houoy.www.gongxing.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.houoy.www.gongxing.MessageDetailActivity;
import com.houoy.www.gongxing.R;
import com.houoy.www.gongxing.dao.GongXingDao;
import com.houoy.www.gongxing.model.MessagePush;

import org.xutils.ex.DbException;

import java.util.ArrayList;
import java.util.List;

public class MessageListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<MessagePush> messagePushes;
    public Context context;
    private static final int TYPE_ITEM = 0;  //普通Item View
    private static final int TYPE_FOOTER = 1;  //顶部FootView
    //上拉加载更多状态-默认为0
    private int load_more_status = -1;
    //什么都不显示
    public static final int NOTHING = -1;
    //上拉加载更多
    public static final int PULLUP_LOAD_MORE = 0;
    //正在加载中
    public static final int LOADING_MORE = 1;
    //没有更多数据了
    public static final int NO_MORE_DATA = 2;
    private GongXingDao gongXingDao;

    private Integer limit = 10;
    public MessageListAdapter(Context context) {
        this.context = context;
        gongXingDao = GongXingDao.getInstant();
        initData(0);
    }

    public void initData(int start) {
        try {
            messagePushes = gongXingDao.findMessagePush(start,limit);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    public void pushData(int start) {
        try {
            List<MessagePush> tempMessages = gongXingDao.findMessagePush(start,limit);
            if (messagePushes == null) {
                messagePushes = new ArrayList();
            }

            messagePushes.addAll(tempMessages);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    //自定义ViewHolder类
    static class NewsViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        TextView title_value;
        TextView date;
        TextView rule_name;
        TextView trigger_time;
        TextView device_name;
        TextView subkey_name;
        TextView current_parameter;
        TextView remark;
        Button readMore;

        public NewsViewHolder(final View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.card_view);
            title_value = (TextView) itemView.findViewById(R.id.title_value);
            date = (TextView) itemView.findViewById(R.id.date);
            rule_name = (TextView) itemView.findViewById(R.id.rule_name);
            trigger_time = (TextView) itemView.findViewById(R.id.trigger_time);
            device_name = (TextView) itemView.findViewById(R.id.device_name);
            subkey_name = (TextView) itemView.findViewById(R.id.subkey_name);
            current_parameter = (TextView) itemView.findViewById(R.id.current_parameter);
            remark = (TextView) itemView.findViewById(R.id.remark);
            readMore = (Button) itemView.findViewById(R.id.btn_more);
            //设置TextView背景为半透明
            title_value.setBackgroundColor(Color.argb(20, 0, 0, 0));
        }
    }

    /**
     * 底部FootView布局
     */
    public static class FootViewHolder extends RecyclerView.ViewHolder {
        private TextView foot_view_item_tv;
        private LinearLayout message_foot;

        public FootViewHolder(View view) {
            super(view);
            foot_view_item_tv = (TextView) view.findViewById(R.id.foot_view_item_tv);
            message_foot = (LinearLayout) view.findViewById(R.id.message_foot);
        }
    }

    @Override
    public int getItemViewType(int position) {
        // 最后一个item设置为footerView
        if (position + 1 == getItemCount()) {
            return TYPE_FOOTER;
        } else {
            return TYPE_ITEM;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        //进行判断显示类型，来创建返回不同的View
        if (viewType == TYPE_ITEM) {
            View v = LayoutInflater.from(context).inflate(R.layout.fragment_message_item, viewGroup, false);
            NewsViewHolder nvh = new NewsViewHolder(v);
            return nvh;
        } else if (viewType == TYPE_FOOTER) {
            View foot_view = LayoutInflater.from(context).inflate(R.layout.fragment_message_foot, viewGroup, false);
            //这边可以做一些属性设置，甚至事件监听绑定
            //view.setBackgroundColor(Color.RED);
            FootViewHolder footViewHolder = new FootViewHolder(foot_view);
            return footViewHolder;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int i) {
        final int j = i;
        if (holder instanceof NewsViewHolder) {
            ((NewsViewHolder) holder).title_value.setText(messagePushes.get(i).getTitle_value());
            ((NewsViewHolder) holder).date.setText(messagePushes.get(i).getTime());
            ((NewsViewHolder) holder).rule_name.setText(messagePushes.get(i).getRule_name_value());
            ((NewsViewHolder) holder).trigger_time.setText(messagePushes.get(i).getTrigger_time_value());
            ((NewsViewHolder) holder).device_name.setText(messagePushes.get(i).getDevice_name_value());
            ((NewsViewHolder) holder).subkey_name.setText(messagePushes.get(i).getSubkey_name_value());
            ((NewsViewHolder) holder).current_parameter.setText(messagePushes.get(i).getCurrent_parameter_value());
            ((NewsViewHolder) holder).remark.setText(messagePushes.get(i).getRemark_value());

            //为btn_share btn_readMore cardView设置点击事件
            ((NewsViewHolder) holder).cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, MessageDetailActivity.class);
                    intent.putExtra("messagePush", messagePushes.get(j));
                    context.startActivity(intent);
                }
            });

            ((NewsViewHolder) holder).readMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, MessageDetailActivity.class);
                    intent.putExtra("messagePush", messagePushes.get(j));
                    context.startActivity(intent);
                }
            });
        } else if (holder instanceof FootViewHolder) {
            FootViewHolder footViewHolder = (FootViewHolder) holder;
            switch (load_more_status) {
                case NOTHING:
                    footViewHolder.message_foot.setVisibility(View.GONE);
                    break;
                case PULLUP_LOAD_MORE:
                    footViewHolder.message_foot.setVisibility(View.VISIBLE);
                    footViewHolder.foot_view_item_tv.setText("上拉加载更多...");
                    break;
                case LOADING_MORE:
                    footViewHolder.message_foot.setVisibility(View.VISIBLE);
                    footViewHolder.foot_view_item_tv.setText("正在加载更多数据...");
                    break;
                case NO_MORE_DATA:
                    footViewHolder.message_foot.setVisibility(View.VISIBLE);
                    footViewHolder.foot_view_item_tv.setText("没有更多数据了");
                    break;
            }
        }
    }

    @Override
    public int getItemCount() {
        return messagePushes.size() + 1;//默认又一个foot
    }

    public void changeMoreStatus(int status) {
        load_more_status = status;
        notifyDataSetChanged();
    }
}
