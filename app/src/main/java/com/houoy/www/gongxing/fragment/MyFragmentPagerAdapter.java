package com.houoy.www.gongxing.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;
import android.widget.Toast;

import com.houoy.www.gongxing.RegisterAndSignInActivity;
import com.houoy.www.gongxing.event.RegisterEvent;
import com.houoy.www.gongxing.model.ClientInfo;
import com.houoy.www.gongxing.util.StringUtil;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by Jay on 2015/8/31 0031.
 */
public class MyFragmentPagerAdapter extends FragmentPagerAdapter {

    private final int PAGER_COUNT = 3;
    private Signin myFragment1 = null;
    private Register myFragment2 = null;
    private Register2 myFragment3 = null;


    public MyFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
        myFragment1 = new Signin();
        myFragment2 = new Register();
        myFragment3 = new Register2();
    }


    @Override
    public int getCount() {
        return PAGER_COUNT;
    }

    @Override
    public Object instantiateItem(ViewGroup vg, int position) {
        return super.instantiateItem(vg, position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        System.out.println("position Destory" + position);
        super.destroyItem(container, position, object);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case RegisterAndSignInActivity.PAGE_ONE:
                fragment = myFragment1;
                break;
            case RegisterAndSignInActivity.PAGE_TWO:
                fragment = myFragment2;
                break;
            case RegisterAndSignInActivity.PAGE_TWO2:
                fragment = myFragment3;
                break;
        }
        return fragment;
    }

    public ClientInfo getClientInfo(){
        ClientInfo clientInfo3 = myFragment3.getInputClientInfo();
        ClientInfo clientInfo2 = myFragment2.getInputClientInfo();
        clientInfo3.setPassword(clientInfo2.getPassword());
        clientInfo3.setUserID(clientInfo2.getUserID());
        return clientInfo3;
    }

}

