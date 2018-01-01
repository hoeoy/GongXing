package com.houoy.www.gongxing.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.houoy.www.gongxing.R;
import com.houoy.www.gongxing.model.MessagePush;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by QiWangming on 2015/6/13.
 */
public class MessageListAdapter extends RecyclerView.Adapter<MessageListAdapter.NewsViewHolder> {

    private List<MessagePush> messagePushes;
    private Context context;

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
            messagePushes.add(messagePush);
        }
    }

    //自定义ViewHolder类
    static class NewsViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        ImageView news_photo;
        TextView news_title;
        TextView news_desc;
        Button readMore;

        public NewsViewHolder(final View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.card_view);
            news_photo = (ImageView) itemView.findViewById(R.id.news_photo);
            news_title = (TextView) itemView.findViewById(R.id.news_title);
            news_desc = (TextView) itemView.findViewById(R.id.news_desc);
            readMore = (Button) itemView.findViewById(R.id.btn_more);
            //设置TextView背景为半透明
            news_title.setBackgroundColor(Color.argb(20, 0, 0, 0));

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

        personViewHolder.news_photo.setImageResource(0);
        personViewHolder.news_title.setText(messagePushes.get(i).getCurrent_parameter_value());
        personViewHolder.news_desc.setText(messagePushes.get(i).getCurrent_parameter_value());

        //为btn_share btn_readMore cardView设置点击事件
        personViewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent=new Intent(context,NewsActivity.class);
//                intent.putExtra("News",messagePushes.get(j));
//                context.startActivity(intent);
                Toast.makeText(context, "click", Toast.LENGTH_LONG).show();
            }
        });

        personViewHolder.readMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(context, NewsActivity.class);
//                intent.putExtra("News", messagePushes.get(j));
//                context.startActivity(intent);
                Toast.makeText(context, "click", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return messagePushes.size();
    }
}
