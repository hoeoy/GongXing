package com.houoy.www.gongxing.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.houoy.www.gongxing.R;
import com.houoy.www.gongxing.controller.GongXingController;
import com.houoy.www.gongxing.model.DeviceInfo;
import com.houoy.www.gongxing.model.OperateButton;
import com.houoy.www.gongxing.model.ParaInfo;
import com.houoy.www.gongxing.util.Constants;
import com.houoy.www.gongxing.util.StringUtil;

import org.xutils.ex.DbException;

import java.util.ArrayList;
import java.util.List;

import circletextimage.viviant.com.circletextimagelib.view.CircleTextImage;

/**
 * Created by lenovo on 2/23/2016.
 */
public class SectionedExpandableGridAdapter extends RecyclerView.Adapter<SectionedExpandableGridAdapter.ViewHolder> {

    //data array
    private ArrayList<Object> mDataArrayList;
    private SectiondFooter footer;
    //context
    private final Context mContext;

    private GongXingController gongXingController;
    //listeners
    private final ItemClickListener mItemClickListener;
    private final SectionStateChangeListener mSectionStateChangeListener;

    //view type
    private static final int VIEW_TYPE_SECTION = R.layout.layout_section;
    private static final int VIEW_TYPE_ITEM = R.layout.layout_item2; //TODO : change this
    private static final int VIEW_TYPE_FOOTER = R.layout.layout_section_footer;

    public SectionedExpandableGridAdapter(Context context, ArrayList<Object> dataArrayList, SectiondFooter _footer,
                                          final GridLayoutManager gridLayoutManager, ItemClickListener itemClickListener,
                                          SectionStateChangeListener sectionStateChangeListener) {
        mContext = context;
        mItemClickListener = itemClickListener;
        mSectionStateChangeListener = sectionStateChangeListener;
        mDataArrayList = dataArrayList;
        footer = _footer;
        gongXingController = GongXingController.getInstant();
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return isSection(position) ? gridLayoutManager.getSpanCount() : 1;
            }
        });
    }

    public void setFooter(SectiondFooter footer) {
        this.footer = footer;
    }

    private boolean isSection(int position) {
        if (position + 1 == getItemCount()) {
            return false;
        } else {
            return mDataArrayList.get(position) instanceof Section;
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(viewType, parent, false), viewType);
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        switch (holder.viewType) {
            case VIEW_TYPE_ITEM:
                final DeviceInfo item = (DeviceInfo) mDataArrayList.get(position);
                holder.itemTextView.setText(item.getDeviceName());

                holder.paraInfoContainer.removeAllViews();
                for (ParaInfo paraInfo : item.getParaInfo()) {
                    LayoutInflater mInflater = LayoutInflater.from(holder.paraInfoContainer.getContext());
                    LinearLayout cardView = (LinearLayout) mInflater.inflate(R.layout.layout_item2_parainfo, null);
                    TextView paraNameTextView = (TextView) cardView.findViewById(R.id.paraName);
                    TextView paraValueTextView = (TextView) cardView.findViewById(R.id.paraValue);
                    TextView paraStateTextView = (TextView) cardView.findViewById(R.id.paraState);
                    TextView paraStateName = (TextView) cardView.findViewById(R.id.paraStateName);

                    paraNameTextView.setText(paraInfo.getParaName().getName() + ":");
                    if (!StringUtil.isEmpty(paraInfo.getParaName().getFontColor())) {
                        paraNameTextView.setTextColor(Color.parseColor(paraInfo.getParaName().getFontColor()));
                    }

                    paraValueTextView.setText(paraInfo.getParaValue().getName());
                    if (!StringUtil.isEmpty(paraInfo.getParaValue().getFontColor())) {
                        paraValueTextView.setTextColor(Color.parseColor(paraInfo.getParaValue().getFontColor()));
                    }

                    paraStateTextView.setText(paraInfo.getParaState().getName());
                    if (!StringUtil.isEmpty(paraInfo.getParaState().getFontColor())) {
                        paraStateTextView.setTextColor(Color.parseColor(paraInfo.getParaState().getFontColor()));
                        paraStateName.setTextColor(Color.parseColor(paraInfo.getParaState().getFontColor()));
                    }

                    holder.paraInfoContainer.addView(cardView);
                }

//                ParaInfoAdapter paraInfoAdapter = new ParaInfoAdapter(item.getParaInfo());
//                holder.recycler_view2.setAdapter(paraInfoAdapter);
//                paraInfoAdapter.notifyDataSetChanged();
                holder.view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mItemClickListener.itemClicked(item);
                    }
                });
                break;
            case VIEW_TYPE_SECTION:
                final Section section = (Section) mDataArrayList.get(position);
                holder.text_icon.setText4CircleImage(section.getName().toCharArray()[0] + "");
                holder.text_date.setText(section.getTime());
                holder.text_state.setText(section.getState());
                if (section.getState().equals(Constants.State_normal)) {
                    holder.text_state.setTextColor(Color.parseColor("#09BB07"));
                } else {
                    holder.text_state.setTextColor(Color.RED);
                    holder.text_date.setTextColor(Color.RED);
                }

                holder.sectionTextView.setText(section.getName());
                holder.sectionLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mItemClickListener.itemClicked(section);
                    }
                });
                holder.sectionToggleButton.setChecked(section.isExpanded);
                holder.sectionToggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        mSectionStateChangeListener.onSectionStateChanged(section, isChecked);
                    }
                });
                break;
            case VIEW_TYPE_FOOTER:
                if (footer != null) {
                    holder.cardView.setVisibility(View.VISIBLE);
                    holder.sectionFooterLayout.setVisibility(View.VISIBLE);
                    if (footer.getRemarkPart() != null) {
                        holder.section_remark.setVisibility(View.VISIBLE);
//                        holder.sectionRemark.setVisibility(View.VISIBLE);
//                        switch (footer.getType()) {
//                            case "1"://查询
//                                holder.sectionRemark.setVisibility(View.GONE);
//                                break;
//                            case "2"://报警
//                                holder.sectionRemark.setText("报警信息:");
//                                break;
//                            case "3"://日报
//                                holder.sectionRemark.setText("日报摘要:");
//                                break;
//                        }
                        holder.section_remark.setText(footer.getRemarkPart().getRemark());

                    } else {
                        holder.section_remark.setVisibility(View.GONE);
//                        holder.sectionRemark.setVisibility(View.GONE);
                    }

                    if (footer.getOperatePart() != null) {
                        List<OperateButton> operateButtons = footer.getOperatePart().getOperateButton();
                        if (operateButtons != null && operateButtons.size() > 0) {
                            OperateButton button = operateButtons.get(0);
                            holder.sectionOperateLayoutButton.setText(button.getOperateName());
                            holder.sectionOperateLayout.setVisibility(View.VISIBLE);
                            if (button.getOperateTypeID().equals(OperateButton.OperateType_ENABLE)) {
                                holder.sectionOperateLayoutButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        try {
                                            gongXingController.affirmOperate(footer.getClientInfo());
                                        } catch (DbException e) {
                                            e.printStackTrace();
                                            Log.e(e.getMessage(), e.getLocalizedMessage());
                                        }
                                    }
                                });
                            }
                        } else {
                            holder.sectionOperateLayout.setVisibility(View.GONE);
                        }
                    } else {
                        holder.sectionOperateLayout.setVisibility(View.GONE);
                    }
                } else {
                    holder.sectionFooterLayout.setVisibility(View.GONE);
                    holder.cardView.setVisibility(View.GONE);
                }
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mDataArrayList.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        // 最后一个item设置为footerView
        if (position + 1 == getItemCount()) {
            return VIEW_TYPE_FOOTER;
        } else {
            if (isSection(position)) {
                return VIEW_TYPE_SECTION;
            } else {
                return VIEW_TYPE_ITEM;
            }
        }
    }

    protected static class ViewHolder extends RecyclerView.ViewHolder {

        //common
        View view;
        int viewType;

        //for section
        ConstraintLayout sectionLayout;
        TextView sectionTextView;
        ToggleButton sectionToggleButton;
        CircleTextImage text_icon;
        TextView text_date;
        TextView text_state;

        //for item
        TextView itemTextView;
        //        RecyclerView recycler_view2;
        LinearLayout paraInfoContainer;


        //for footer
        CardView cardView;//
        ConstraintLayout sectionFooterLayout;//
//        TextView sectionRemark;//描述信息名称
        TextView section_remark;//描述信息内容
        LinearLayout sectionOperateLayout;//按钮区域
        Button sectionOperateLayoutButton;//区域按钮

        public ViewHolder(View view, int viewType) {
            super(view);
            this.viewType = viewType;
            this.view = view;
            if (viewType == VIEW_TYPE_ITEM) {
                itemTextView = (TextView) view.findViewById(R.id.text_item2);
//                recycler_view2 = (RecyclerView) view.findViewById(R.id.recycler_view2);
                paraInfoContainer = (LinearLayout) view.findViewById(R.id.paraInfoContainer);
            } else if (viewType == VIEW_TYPE_SECTION) {
                sectionTextView = (TextView) view.findViewById(R.id.text_section);
                sectionLayout = (ConstraintLayout) view.findViewById(R.id.sectionLayout);
                text_icon = (CircleTextImage) view.findViewById(R.id.text_icon);
                text_date = (TextView) view.findViewById(R.id.text_date);
                text_state = (TextView) view.findViewById(R.id.text_state);
                sectionToggleButton = (ToggleButton) view.findViewById(R.id.toggle_button_section);
            } else {//footer
                cardView = (CardView) view.findViewById(R.id.cardView);
                sectionFooterLayout = (ConstraintLayout) view.findViewById(R.id.sectionFooterLayout);
//                sectionRemark = (TextView) view.findViewById(R.id.sectionRemark);
                section_remark = (TextView) view.findViewById(R.id.section_remark);
                sectionOperateLayout = (LinearLayout) view.findViewById(R.id.sectionOperateLayout);
                sectionOperateLayoutButton = (Button) view.findViewById(R.id.sectionOperateLayoutButton);
            }
        }
    }
}
