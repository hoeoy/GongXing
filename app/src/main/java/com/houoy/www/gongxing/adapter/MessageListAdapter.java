//package com.houoy.www.gongxing.adapter;
//
//import android.content.Context;
//import android.content.Intent;
//import android.support.constraint.ConstraintLayout;
//import android.support.v7.widget.CardView;
//import android.support.v7.widget.RecyclerView;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//import com.houoy.www.gongxing.MessageDetailActivity;
//import com.houoy.www.gongxing.R;
//import com.houoy.www.gongxing.dao.MessagePushDao;
//import com.houoy.www.gongxing.vo.MessageVO;
//
//import org.xutils.ex.DbException;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class MessageListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
//
//    private List<MessageVO> messageVOs;
//    public Context context;
//    private static final int TYPE_ITEM = 0;  //普通Item View
//    private static final int TYPE_FOOTER = 1;  //顶部FootView
//    //上拉加载更多状态-默认为0
//    private int load_more_status = -1;
//    //什么都不显示
//    public static final int NOTHING = -1;
//    //上拉加载更多
//    public static final int PULLUP_LOAD_MORE = 0;
//    //正在加载中
//    public static final int LOADING_MORE = 1;
//    //没有更多数据了
//    public static final int NO_MORE_DATA = 2;
//
//    private MessagePushDao messagePushDao;
//
//    private Integer limit = 10;
//
//    public MessageListAdapter(Context context) {
//        this.context = context;
//        messagePushDao = MessagePushDao.getInstant();
//        initData(0);
//    }
//
//    public void initData(int start) {
//        try {
//            messageVOs = messagePushDao.findMessagePush(start, limit);
//            if (messageVOs == null) {
//                messageVOs = new ArrayList();
//            }
//        } catch (DbException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void pushData(int start) {
//        try {
//            List<MessageVO> tempMessages = messagePushDao.findMessagePush(start, limit);
//            if (messageVOs == null) {
//                messageVOs = new ArrayList();
//            }
//
//            if (tempMessages != null) {
//                messageVOs.addAll(tempMessages);
//            }
//        } catch (DbException e) {
//            e.printStackTrace();
//        }
//    }
//
//    //自定义ViewHolder类
//    static class NewsViewHolder extends RecyclerView.ViewHolder {
//
//        ConstraintLayout constraintLayout1;
//        ConstraintLayout constraintLayout2;
//        CardView cardView;
//        TextView title_value;
//        TextView date;
//        TextView rule_name;
//        TextView trigger_time;
//        TextView device_name;
//        TextView subkey_name;
//        TextView current_parameter;
//        TextView remark;
//        Button readMore;
//
//        TextView title_value2;
//        TextView date2;
//        TextView temperature_value;
//        TextView humidity_value;
//        TextView state_value;
//        TextView alarm_num_value;
//        TextView remark2;
//        Button btn_more2;
//
//        public NewsViewHolder(final View itemView) {
//            super(itemView);
//            cardView = (CardView) itemView.findViewById(R.id.card_view);
//            constraintLayout1 = (ConstraintLayout) itemView.findViewById(R.id.constraintLayout1);
//            constraintLayout2 = (ConstraintLayout) itemView.findViewById(R.id.constraintLayout2);
//            title_value = (TextView) itemView.findViewById(R.id.title_value);
//            date = (TextView) itemView.findViewById(R.id.date);
//            rule_name = (TextView) itemView.findViewById(R.id.rule_name);
//            trigger_time = (TextView) itemView.findViewById(R.id.trigger_time);
//            device_name = (TextView) itemView.findViewById(R.id.device_name);
//            subkey_name = (TextView) itemView.findViewById(R.id.subkey_name);
//            current_parameter = (TextView) itemView.findViewById(R.id.current_parameter);
//            remark = (TextView) itemView.findViewById(R.id.remark);
//            readMore = (Button) itemView.findViewById(R.id.btn_more);
//
//            title_value2 = (TextView) itemView.findViewById(R.id.title_value2);
//            date2 = (TextView) itemView.findViewById(R.id.date2);
//            temperature_value = (TextView) itemView.findViewById(R.id.temperature_value);
//            humidity_value = (TextView) itemView.findViewById(R.id.humidity_value);
//            state_value = (TextView) itemView.findViewById(R.id.state_value);
//            alarm_num_value = (TextView) itemView.findViewById(R.id.alarm_num_value);
//            remark2 = (TextView) itemView.findViewById(R.id.remark2);
//            btn_more2 = (Button) itemView.findViewById(R.id.btn_more2);
//        }
//    }
//
//    /**
//     * 底部FootView布局
//     */
//    public static class FootViewHolder extends RecyclerView.ViewHolder {
//        private TextView foot_view_item_tv;
//        private LinearLayout message_foot;
//
//        public FootViewHolder(View view) {
//            super(view);
//            foot_view_item_tv = (TextView) view.findViewById(R.id.foot_view_item_tv);
//            message_foot = (LinearLayout) view.findViewById(R.id.message_foot);
//        }
//    }
//
//    @Override
//    public int getItemViewType(int position) {
//        // 最后一个item设置为footerView
//        if (position + 1 == getItemCount()) {
//            return TYPE_FOOTER;
//        } else {
//            return TYPE_ITEM;
//        }
//    }
//
//    @Override
//    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
//        //进行判断显示类型，来创建返回不同的View
//        if (viewType == TYPE_ITEM) {
//            View v = LayoutInflater.from(context).inflate(R.layout.fragment_message_item, viewGroup, false);
//            NewsViewHolder nvh = new NewsViewHolder(v);
//            return nvh;
//        } else if (viewType == TYPE_FOOTER) {
//            View foot_view = LayoutInflater.from(context).inflate(R.layout.fragment_message_foot, viewGroup, false);
//            //这边可以做一些属性设置，甚至事件监听绑定
//            //view.setBackgroundColor(Color.RED);
//            FootViewHolder footViewHolder = new FootViewHolder(foot_view);
//            return footViewHolder;
//        }
//        return null;
//    }
//
//    @Override
//    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int i) {
//        if (holder instanceof NewsViewHolder) {
//            String type = messageVOs.get(i).getType();
//            if(type == null){//默认为报警信息
//                type = "2" ;
//            }
//
//            switch (type) {
//                case "1"://日报
//                    ((NewsViewHolder) holder).constraintLayout1.setVisibility(View.GONE);
//                    ((NewsViewHolder) holder).constraintLayout2.setVisibility(View.VISIBLE);
//                    ((NewsViewHolder) holder).title_value2.setText(messageVOs.get(i).getTitle_value());
//                    ((NewsViewHolder) holder).date2.setText(messageVOs.get(i).getTime());
//                    ((NewsViewHolder) holder).temperature_value.setText(messageVOs.get(i).getTemperature_value());
//                    ((NewsViewHolder) holder).humidity_value.setText(messageVOs.get(i).getHumidity_value());
//                    ((NewsViewHolder) holder).state_value.setText(messageVOs.get(i).getState_value());
//                    ((NewsViewHolder) holder).alarm_num_value.setText(messageVOs.get(i).getAlarm_num_value());
//                    ((NewsViewHolder) holder).remark2.setText(messageVOs.get(i).getRemark_value());
//
//                    ((NewsViewHolder) holder).btn_more2.setText("日报详细");
//                    ((NewsViewHolder) holder).btn_more2.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            Intent intent = new Intent(context, MessageDetailActivity.class);
//                            intent.putExtra("messagePush", messageVOs.get(i));
//                            context.startActivity(intent);
//                        }
//                    });
//                    break;
//                case "2"://报警
//                    ((NewsViewHolder) holder).constraintLayout1.setVisibility(View.VISIBLE);
//                    ((NewsViewHolder) holder).constraintLayout2.setVisibility(View.GONE);
//                    ((NewsViewHolder) holder).title_value.setText(messageVOs.get(i).getTitle_value());
//                    ((NewsViewHolder) holder).date.setText(messageVOs.get(i).getTime());
//                    ((NewsViewHolder) holder).rule_name.setText(messageVOs.get(i).getRule_name_value());
//                    ((NewsViewHolder) holder).trigger_time.setText(messageVOs.get(i).getTrigger_time_value());
//                    ((NewsViewHolder) holder).device_name.setText(messageVOs.get(i).getDevice_name_value());
//                    ((NewsViewHolder) holder).subkey_name.setText(messageVOs.get(i).getSubkey_name_value());
//                    ((NewsViewHolder) holder).current_parameter.setText(messageVOs.get(i).getCurrent_parameter_value());
//                    ((NewsViewHolder) holder).remark.setText(messageVOs.get(i).getRemark_value());
//                    ((NewsViewHolder) holder).readMore.setText("报警详细");
//                    ((NewsViewHolder) holder).readMore.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            Intent intent = new Intent(context, MessageDetailActivity.class);
//                            intent.putExtra("messagePush", messageVOs.get(i));
//                            context.startActivity(intent);
//                        }
//                    });
//                    break;
//                default:
//                    break;
//            }
//
//            //为btn_share btn_readMore cardView设置点击事件
//            ((NewsViewHolder) holder).cardView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent(context, MessageDetailActivity.class);
//                    intent.putExtra("messagePush", messageVOs.get(i));
//                    context.startActivity(intent);
//                }
//            });
//        } else if (holder instanceof FootViewHolder) {
//            FootViewHolder footViewHolder = (FootViewHolder) holder;
//            switch (load_more_status) {
//                case NOTHING:
//                    footViewHolder.message_foot.setVisibility(View.GONE);
//                    break;
//                case PULLUP_LOAD_MORE:
//                    footViewHolder.message_foot.setVisibility(View.VISIBLE);
//                    footViewHolder.foot_view_item_tv.setText("上拉加载更多...");
//                    break;
//                case LOADING_MORE:
//                    footViewHolder.message_foot.setVisibility(View.VISIBLE);
//                    footViewHolder.foot_view_item_tv.setText("正在加载更多数据...");
//                    break;
//                case NO_MORE_DATA:
//                    footViewHolder.message_foot.setVisibility(View.VISIBLE);
//                    footViewHolder.foot_view_item_tv.setText("没有更多数据了");
//                    break;
//            }
//        }
//    }
//
//    @Override
//    public int getItemCount() {
//        if (messageVOs == null) {
//            return 1;
//        }
//
//        return messageVOs.size() + 1;//默认又一个foot
//    }
//
//    public void changeMoreStatus(int status) {
//        load_more_status = status;
//        notifyDataSetChanged();
//    }
//}
