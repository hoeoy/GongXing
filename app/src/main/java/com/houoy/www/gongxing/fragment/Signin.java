package com.houoy.www.gongxing.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.houoy.www.gongxing.R;
import com.houoy.www.gongxing.event.LoginEvent;
import com.houoy.www.gongxing.util.XUtil;
import com.houoy.www.gongxing.util.XUtilCallBack;
import com.houoy.www.gongxing.vo.RequestVO;
import com.houoy.www.gongxing.vo.ResultVO;

import org.greenrobot.eventbus.EventBus;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.x;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jay on 2015/8/28 0028.
 */
@ContentView(R.layout.signin_content)
public class Signin extends Fragment {
    private boolean injected = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        injected = true;
        View view = x.view().inject(this, inflater, container); //使用注解模块一定要注意初始化视图注解框架

        return view;
    }

    @Override
    public void onViewCreated(View v, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);
        if (!injected) {
            x.view().inject(this, this.getView());
        }
    }

    /**
     * 1. 方法必须私有限定,
     * 2. 方法参数形式必须和type对应的Listener接口一致.
     * 3. 注解参数value支持数组: value={id1, id2, id3}
     * 4. 其它参数说明见{ org.xutils.event.annotation.Event}类的说明.
     * 长按事件 type = View.OnLongClickListener.class
     **/
    @Event(value = {R.id.btnLogin})
    private void onLoginClick(View view) {
        String url = "http://101.201.67.36:9011/CloudWeChatPlatServer/Login";
        Map<String, String> params = new HashMap();
        params.put("UserID", "zhaozhao");
        params.put("Password", "123456");
        RequestVO requestVO = new RequestVO("dff687bbfd840d3484e2091b09c8c424", params);
        String paramStr = JSON.toJSONString(requestVO);

        XUtil.Post(url, paramStr, new XUtilCallBack<String>() {
            @Override
            public void onSuccess(String result) {
                ResultVO resultVO = JSON.parseObject(result, ResultVO.class);
//                if (resultVO.getCode().equals("success")) {
//                    EventBus.getDefault().post(new LoginEvent("login", resultVO));
//                } else {
//                    Toast.makeText(x.app(), resultVO.getMessage(), Toast.LENGTH_SHORT).show();
//                }

                Toast.makeText(x.app(), resultVO.getMessage(), Toast.LENGTH_SHORT).show();
                EventBus.getDefault().post(new LoginEvent("login", resultVO));
            }
        });
    }
}
