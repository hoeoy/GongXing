package com.houoy.www.gongxing;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;

import com.houoy.www.gongxing.element.ClearEditText;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

@ContentView(R.layout.activity_change_pw)
public class ChangePWActivity extends MyAppCompatActivity {
    private ActionBar actionBar;
    @ViewInject(R.id.change_userid)
    private ClearEditText change_userid;
    @ViewInject(R.id.change_old)
    private ClearEditText change_old;
    @ViewInject(R.id.change_new)
    private ClearEditText change_new;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        actionBar = this.getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(getString(R.string.changePW));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
