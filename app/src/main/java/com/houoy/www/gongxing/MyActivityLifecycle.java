package com.houoy.www.gongxing;

/**
 * Created by andyzhao on 3/6/2018.
 */

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

public class MyActivityLifecycle implements Application.ActivityLifecycleCallbacks {

    private boolean isForeground = false;//应用是否处于前端

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {
        isForeground = true;
    }

    @Override
    public void onActivityPaused(Activity activity) {
        isForeground = false;
    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }

    public boolean isForeground() {
        return isForeground;
    }
}