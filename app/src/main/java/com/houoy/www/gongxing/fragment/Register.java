package com.houoy.www.gongxing.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.houoy.www.gongxing.R;
import com.houoy.www.gongxing.event.RegisterEvent;

import org.greenrobot.eventbus.EventBus;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.x;

/**
 * Created by Jay on 2015/8/28 0028.
 */
@ContentView(R.layout.register_content)
public class Register extends Fragment {
    private boolean injected = false;

    public Register() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        injected = true;
        View view =  x.view().inject(this, inflater, container); //使用注解模块一定要注意初始化视图注解框架

        return view;
    }

    @Override
    public void onViewCreated(View v, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);
        if (!injected) {
            x.view().inject(this, this.getView());
        }
    }

    @Event(value = {R.id.btnNext})
    private void onNextClick(View view) {
        EventBus.getDefault().post(new RegisterEvent("1", "a"));
    }


}
