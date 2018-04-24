package com.houoy.www.gongxing.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.houoy.www.gongxing.AboutUpdateActivity;
import com.houoy.www.gongxing.GongXingApplication;
import com.houoy.www.gongxing.R;
import com.houoy.www.gongxing.controller.UploadController;
import com.houoy.www.gongxing.dao.AboutMenuDao;
import com.houoy.www.gongxing.model.AboutMenu;
import com.houoy.www.gongxing.util.AppUtil;

import org.xutils.ex.DbException;

import java.util.ArrayList;
import java.util.List;

public class AboutAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public Context context;
    private List<AboutMenu> menuList = new ArrayList<>();
    private AboutMenuDao aboutMenuDao;
    private UploadController uploadController;

    public AboutAdapter(Context context, String a) {
        super();
        this.context = context;
        aboutMenuDao = AboutMenuDao.getInstant();
        uploadController = UploadController.getInstant();
        uploadController.initAboutMenu();
        initData();
    }

    private void initData() {
        try {
            menuList = aboutMenuDao.findAll();
            if (menuList == null || menuList.size() < 1) {
                AboutMenu daoAM = new AboutMenu();
                int appCurrentCode = AppUtil.getVersionCode(GongXingApplication.gongXingApplication.getBaseContext());
                String appCurrentName = AppUtil.getVersionName(GongXingApplication.gongXingApplication.getBaseContext());
                daoAM.setMenu_name("版本");
                daoAM.setHas_update(false);
                daoAM.setMenu_code(appCurrentName);
                daoAM.setCurrentVersionCode(appCurrentCode);
                daoAM.setCurrentVersionName(appCurrentName);
                aboutMenuDao.add(daoAM);
                menuList = new ArrayList<>();
                menuList.add(daoAM);
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    public void updateData() {
        try {
            menuList = aboutMenuDao.findAll();
            notifyDataSetChanged();
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    //自定义ViewHolder类
    static class AboutMenusViewHolder extends RecyclerView.ViewHolder {
        TextView menu_name;
        ImageView menu_shape;
        TextView menu_code;
        ToggleButton menu_toggle;
        ConstraintLayout menu;

        public AboutMenusViewHolder(final View itemView) {
            super(itemView);
            menu_name = itemView.findViewById(R.id.menu_name);
            menu_shape = itemView.findViewById(R.id.menu_shape);
            menu_code = itemView.findViewById(R.id.menu_code);
            menu_toggle = itemView.findViewById(R.id.menu_toggle);
            menu = itemView.findViewById(R.id.menu);
        }
    }

    @Override
    public AboutMenusViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.fragment_about_menu_item, viewGroup, false);
        AboutMenusViewHolder nvh = new AboutMenusViewHolder(v);
        return nvh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int i) {
        final AboutMenu aboutMenu = menuList.get(i);
        if (aboutMenu.getHas_update()) {
            ((AboutMenusViewHolder) holder).menu_shape.setVisibility(View.VISIBLE);
        } else {
            ((AboutMenusViewHolder) holder).menu_shape.setVisibility(View.INVISIBLE);
        }
        ((AboutMenusViewHolder) holder).menu_code.setText(aboutMenu.getMenu_code());
        ((AboutMenusViewHolder) holder).menu_name.setText(aboutMenu.getMenu_name());
        ((AboutMenusViewHolder) holder).menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (menuList.get(0).getHas_update()) {
                    Intent intent = new Intent(context, AboutUpdateActivity.class);
                    context.startActivity(intent);
                } else {
                    Toast.makeText(context, "当前已经是最新版本", Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return menuList.size();
    }
}
