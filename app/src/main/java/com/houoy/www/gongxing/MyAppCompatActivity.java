package com.houoy.www.gongxing;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by andyzhao on 3/6/2018.
 */
public class MyAppCompatActivity extends AppCompatActivity {
    private ActivityPool activityPool;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityPool = ActivityPool.getInstant();
        activityPool.push(this);
    }

    @Override
    protected void onDestroy() {
        Class classs = getClass();
        activityPool.finishActivity(classs);
        super.onDestroy();
    }
}
