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
import android.widget.TextView;

import com.houoy.www.gongxing.MessageDetailActivity;
import com.houoy.www.gongxing.R;
import com.houoy.www.gongxing.model.MessagePush;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by QiWangming on 2015/6/13.
 */
public class MessageListAdapter extends RecyclerView.Adapter<MessageListAdapter.NewsViewHolder> {

    private List<MessagePush> messagePushes;
    public Context context;

    public MessageListAdapter(Context context) {
        this.context = context;
        initData();
    }

    public void initData() {
        messagePushes = new ArrayList();
        for (int i = 0; i < 5; i++) {
            MessagePush messagePush = new MessagePush();
            messagePush.setCurrent_parameter_color(i + i + i + "000");
            messagePush.setCurrent_parameter_value(i + "mssage");
            messagePush.setTime(new Date().toString());
            messagePush.setDevice_name_value("device" + i);
            messagePush.setRemark_value("remark" + i);
            messagePush.setRule_name_value("rule" + i);
            messagePush.setSubkey_name_value("subkey" + i);
            messagePush.setTrigger_time_value("trigger" + i);
            messagePushes.add(messagePush);
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

    @Override
    public MessageListAdapter.NewsViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(context).inflate(R.layout.fragment_message_item, viewGroup, false);
        NewsViewHolder nvh = new NewsViewHolder(v);
        return nvh;
    }

    @Override
    public void onBindViewHolder(MessageListAdapter.NewsViewHolder personViewHolder, int i) {
        final int j = i;
        personViewHolder.title_value.setText(messagePushes.get(i).getTitle_value());
        personViewHolder.date.setText(messagePushes.get(i).getTime());
        personViewHolder.rule_name.setText(messagePushes.get(i).getRule_name_value());
        personViewHolder.trigger_time.setText(messagePushes.get(i).getTrigger_time_value());
        personViewHolder.device_name.setText(messagePushes.get(i).getDevice_name_value());
        personViewHolder.subkey_name.setText(messagePushes.get(i).getSubkey_name_value());
        personViewHolder.current_parameter.setText(messagePushes.get(i).getCurrent_parameter_value());
        personViewHolder.remark.setText(messagePushes.get(i).getRemark_value());

        //为btn_share btn_readMore cardView设置点击事件
        personViewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MessageDetailActivity.class);
                intent.putExtra("messagePush", messagePushes.get(j));
                context.startActivity(intent);
            }
        });

        personViewHolder.readMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MessageDetailActivity.class);
                intent.putExtra("messagePush", messagePushes.get(j));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return messagePushes.size();
    }
}
