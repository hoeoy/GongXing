package com.houoy.www.gongxing.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.houoy.www.gongxing.MessageActivity;
import com.houoy.www.gongxing.MessageDetailActivity;
import com.houoy.www.gongxing.R;
import com.houoy.www.gongxing.dao.MessagePushAlertDao;
import com.houoy.www.gongxing.dao.MessagePushDailyDao;
import com.houoy.www.gongxing.model.ChatHouse;
import com.houoy.www.gongxing.model.MessagePushAlert;
import com.houoy.www.gongxing.model.MessagePushBase;
import com.houoy.www.gongxing.model.MessagePushDaily;

import org.xutils.ex.DbException;

import java.util.ArrayList;
import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

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


    private MessagePushAlertDao messagePushAlertDao;
    private MessagePushDailyDao messagePushDailyDao;

    private List<MessagePushAlert> messagePushAlerts;
    private List<MessagePushDaily> messagePushDailies;

    private ChatHouse chatHouse;
    private Integer limit = 10;

    public MessageAdapter(Context context, ChatHouse _chatHouse) {
        this.context = context;
        messagePushAlertDao = MessagePushAlertDao.getInstant();
        messagePushDailyDao = MessagePushDailyDao.getInstant();

        chatHouse = _chatHouse;
        initData(0);
    }

    public void initData(int start) {
        try {
            switch (chatHouse.getHouse_type()) {
                case ChatHouse.HouseTypeSystemDaily:
                    messagePushDailies = messagePushDailyDao.findPage(start, limit, "time");
                    if (messagePushDailies == null) {
                        messagePushDailies = new ArrayList();
                    }
                    break;
                case ChatHouse.HouseTypeSystemAlert:
                    messagePushAlerts = messagePushAlertDao.findPage(start, limit, "time");
                    if (messagePushAlerts == null) {
                        messagePushAlerts = new ArrayList();
                    }
                    break;
                default:
                    break;
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    public void pushData(int start) {
        try {
            switch (chatHouse.getHouse_type()) {
                case ChatHouse.HouseTypeSystemDaily:
                    if (messagePushDailies == null) {
                        messagePushDailies = new ArrayList();
                    }
                    List<MessagePushDaily> tempMessages = messagePushDailyDao.findPage(start, limit, "time");
                    if (tempMessages != null) {
                        messagePushDailies.addAll(tempMessages);
                    }
                    break;
                case ChatHouse.HouseTypeSystemAlert:
                    if (messagePushAlerts == null) {
                        messagePushAlerts = new ArrayList();
                    }
                    List<MessagePushAlert> tempMessages2 = messagePushAlertDao.findPage(start, limit, "time");
                    if (tempMessages2 != null) {
                        messagePushAlerts.addAll(tempMessages2);
                    }
                    break;
                default:
                    break;
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    //自定义ViewHolder类
    static class NewsViewHolder extends RecyclerView.ViewHolder {

        ConstraintLayout constraintLayout1;
        ConstraintLayout constraintLayout2;
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

        TextView title_value2;
        TextView date2;
        TextView temperature_value;
        TextView humidity_value;
        TextView state_value;
        TextView alarm_num_value;
        TextView remark2;
        Button btn_more2;

        public NewsViewHolder(final View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.card_view);
            constraintLayout1 = (ConstraintLayout) itemView.findViewById(R.id.constraintLayout1);
            constraintLayout2 = (ConstraintLayout) itemView.findViewById(R.id.constraintLayout2);
            title_value = (TextView) itemView.findViewById(R.id.title_value);
            date = (TextView) itemView.findViewById(R.id.date);
            rule_name = (TextView) itemView.findViewById(R.id.rule_name);
            trigger_time = (TextView) itemView.findViewById(R.id.trigger_time);
            device_name = (TextView) itemView.findViewById(R.id.device_name);
            subkey_name = (TextView) itemView.findViewById(R.id.subkey_name);
            current_parameter = (TextView) itemView.findViewById(R.id.current_parameter);
            remark = (TextView) itemView.findViewById(R.id.remark);
            readMore = (Button) itemView.findViewById(R.id.btn_more);

            title_value2 = (TextView) itemView.findViewById(R.id.title_value2);
            date2 = (TextView) itemView.findViewById(R.id.date2);
            temperature_value = (TextView) itemView.findViewById(R.id.temperature_value);
            humidity_value = (TextView) itemView.findViewById(R.id.humidity_value);
            state_value = (TextView) itemView.findViewById(R.id.state_value);
            alarm_num_value = (TextView) itemView.findViewById(R.id.alarm_num_value);
            remark2 = (TextView) itemView.findViewById(R.id.remark2);
            btn_more2 = (Button) itemView.findViewById(R.id.btn_more2);
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
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int i) {
        if (holder instanceof NewsViewHolder) {
            switch (chatHouse.getHouse_type()) {
                case ChatHouse.HouseTypeSystemDaily:
                    final MessagePushDaily mpd = messagePushDailies.get(i);
                    ((NewsViewHolder) holder).constraintLayout1.setVisibility(View.GONE);
                    ((NewsViewHolder) holder).constraintLayout2.setVisibility(View.VISIBLE);
                    ((NewsViewHolder) holder).title_value2.setText(mpd.getTitle_value());
                    ((NewsViewHolder) holder).date2.setText(mpd.getTime());
                    ((NewsViewHolder) holder).temperature_value.setText(mpd.getTemperature_value());
                    ((NewsViewHolder) holder).humidity_value.setText(mpd.getHumidity_value());
                    ((NewsViewHolder) holder).state_value.setText(mpd.getState_value());
                    ((NewsViewHolder) holder).alarm_num_value.setText(mpd.getAlarm_num_value());
                    ((NewsViewHolder) holder).remark2.setText(mpd.getRemark_value());

                    ((NewsViewHolder) holder).btn_more2.setText("日报详细");
                    ((NewsViewHolder) holder).btn_more2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
//                            Intent intent = new Intent(context, MessageDetailActivity.class);
//                            intent.putExtra("messagePush", messagePushDailies.get(i));
//                            context.startActivity(intent);
                            gotoDetail(mpd);
                        }
                    });
                    //为btn_share btn_readMore cardView设置点击事件
                    ((NewsViewHolder) holder).cardView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            gotoDetail(mpd);
                        }
                    });
                    break;
                case ChatHouse.HouseTypeSystemAlert:
                    final MessagePushAlert mpa = messagePushAlerts.get(i);
                    ((NewsViewHolder) holder).constraintLayout1.setVisibility(View.VISIBLE);
                    ((NewsViewHolder) holder).constraintLayout2.setVisibility(View.GONE);
                    ((NewsViewHolder) holder).title_value.setText(mpa.getTitle_value());
                    ((NewsViewHolder) holder).date.setText(mpa.getTime());
                    ((NewsViewHolder) holder).rule_name.setText(mpa.getRule_name_value());
                    ((NewsViewHolder) holder).trigger_time.setText(mpa.getTrigger_time_value());
                    ((NewsViewHolder) holder).device_name.setText(mpa.getDevice_name_value());
                    ((NewsViewHolder) holder).subkey_name.setText(mpa.getSubkey_name_value());
                    ((NewsViewHolder) holder).current_parameter.setText(mpa.getCurrent_parameter_value());
                    ((NewsViewHolder) holder).remark.setText(mpa.getRemark_value());
                    ((NewsViewHolder) holder).readMore.setText("报警详细");
                    ((NewsViewHolder) holder).readMore.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            gotoDetail(mpa);
                        }
                    });
                    //为btn_share btn_readMore cardView设置点击事件
                    ((NewsViewHolder) holder).cardView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            gotoDetail(mpa);
                        }
                    });
                    break;
                default:
                    break;
            }
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

    private void gotoDetail(MessagePushBase mpa) {
        Intent intent = new Intent(context, MessageDetailActivity.class);
        intent.putExtra("messagePush", mpa);
        intent.putExtra(MessageActivity.intentStr, chatHouse);
        context.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        switch (chatHouse.getHouse_type()) {
            case ChatHouse.HouseTypeSystemDaily:
                if (messagePushDailies == null) {
                    return 1;
                }

                return messagePushDailies.size() + 1;//默认又一个foot
            case ChatHouse.HouseTypeSystemAlert:
                if (messagePushAlerts == null) {
                    return 1;
                }

                return messagePushAlerts.size() + 1;//默认又一个foot
            default:
                break;
        }

        return 1;
    }

    public void changeMoreStatus(int status) {
        load_more_status = status;
        notifyDataSetChanged();
    }
}
