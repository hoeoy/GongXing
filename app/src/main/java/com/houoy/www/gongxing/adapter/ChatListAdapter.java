package com.houoy.www.gongxing.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.houoy.www.gongxing.MessageActivity;
import com.houoy.www.gongxing.R;
import com.houoy.www.gongxing.dao.HouseDao;
import com.houoy.www.gongxing.model.ChatHouse;
import com.houoy.www.gongxing.util.StringUtil;

import org.xutils.ex.DbException;

import java.util.List;

import circletextimage.viviant.com.circletextimagelib.view.CircleTextImage;
import cn.bingoogolapple.badgeview.BGABadgeTextView;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ChatHolder> {
    private HouseDao dao;
    private List<ChatHouse> houses;
    public Context context;

    public ChatListAdapter(Context context) {
        this.context = context;
        dao = HouseDao.getInstant();
        initData(0);
    }

    public void initData(int start) {
//        dao.clear();
//        for (int i = 0; i < 25; i++) {
//            ChatHouse chatHouse = new ChatHouse();
//            chatHouse.setHouse_name("名称" + new Date().toString());
//            chatHouse.setTs(DateUtil.getNowDateShanghai());
//            chatHouse.setUnread_num(new Random().nextInt(200));
//            chatHouse.setLast_essage("最新消息" + i + new Random(10).nextInt());
//            dao.add(chatHouse);
//        }
        try {
            houses = dao.findAll();
        } catch (DbException e) {
            e.printStackTrace();
        }
    }


    //自定义ViewHolder类
    class ChatHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        CircleTextImage houseIcon;
        View main;
        TextView chat_item_name;
        TextView chat_item_time;
        TextView chat_item_content;
        BGABadgeTextView chat_item_message_num;


        public ChatHolder(final View itemView) {
            super(itemView);
            houseIcon = (CircleTextImage) itemView.findViewById(R.id.houseIcon);
            chat_item_name = (TextView) itemView.findViewById(R.id.chat_item_name);
            chat_item_time = (TextView) itemView.findViewById(R.id.chat_item_time);
            chat_item_content = (TextView) itemView.findViewById(R.id.chat_item_content);
            chat_item_message_num = (BGABadgeTextView) itemView.findViewById(R.id.chat_item_message_num);
            main = itemView.findViewById(R.id.main);

            main.setOnClickListener(this);
            main.setOnLongClickListener(this);

            View delete = itemView.findViewById(R.id.chat_delete);
            delete.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.main:
                    Intent intent = new Intent();
                    intent.setClass(context, MessageActivity.class);
                    intent.putExtra(MessageActivity.intentStr, houses.get(getAdapterPosition()));
                    context.startActivity(intent);
                    break;

                case R.id.chat_delete:
                    int pos = getAdapterPosition();
                    houses.remove(pos);
                    notifyItemRemoved(pos);
                    break;
            }
        }

        @Override
        public boolean onLongClick(View v) {
            switch (v.getId()) {
                case R.id.main:
//                    Toast.makeText(v.getContext(), "长按了main，位置为：" + getAdapterPosition(), Toast.LENGTH_SHORT).show();
                    break;
            }
            return false;
        }
    }

    @Override
    public ChatHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View root = LayoutInflater.from(context).inflate(R.layout.fragment_chat_item, viewGroup, false);
        return new ChatHolder(root);
    }

    @Override
    public void onBindViewHolder(ChatHolder holder, final int i) {
        String name = houses.get(i).getHouse_name();
        if (StringUtil.isEmpty(name)) {
            name = "";
        }
        if (name.length() > 10) {
            name = name.substring(0, 10) + "...";
        }

        holder.houseIcon.setText4CircleImage(name.toCharArray()[0] + "");
        holder.chat_item_content.setText(houses.get(i).getLast_essage());
        holder.chat_item_name.setText(name);
        holder.chat_item_time.setText(houses.get(i).getTs());


        Integer unread = houses.get(i).getUnread_num();
        if (unread != null && unread > 0) {
            holder.chat_item_message_num.showCirclePointBadge();
            String ur = unread + "";
            if (unread > 100) {
                ur = "+99";
            }
            holder.chat_item_message_num.showTextBadge(ur);
        }
    }

    @Override
    public int getItemCount() {
        if (houses == null) {
            return 0;
        }

        return houses.size();
    }
}
