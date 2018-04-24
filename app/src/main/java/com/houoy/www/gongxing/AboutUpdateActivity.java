package com.houoy.www.gongxing;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.houoy.www.gongxing.dao.AboutMenuDao;
import com.houoy.www.gongxing.event.UpdateEvent;
import com.houoy.www.gongxing.model.AboutMenu;
import com.houoy.www.gongxing.util.StringUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.xutils.ex.DbException;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.File;

@ContentView(R.layout.activity_about_update)
public class AboutUpdateActivity extends MyAppCompatActivity {
    private boolean injected = false;

    private ActionBar actionBar;

    @ViewInject(R.id.fileTitle)
    private TextView fileTitle;

    @ViewInject(R.id.fileDesc)
    private TextView fileDesc;

    @ViewInject(R.id.downloadProgress)
    private TextView downloadProgress;

    @ViewInject(R.id.progressBarHorizontal)
    private ProgressBar progressBarHorizontal;

    @ViewInject(R.id.installBtn)
    private Button installBtn;

    private AboutMenuDao aboutMenuDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        EventBus.getDefault().register(this);
        actionBar = this.getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(getString(R.string.about_update));
        aboutMenuDao = AboutMenuDao.getInstant();

        initView();
    }

    public void initView() {
        try {
            AboutMenu aboutMenu = aboutMenuDao.findFirst();
            fileTitle.setText("版本说明:" + aboutMenu.getNewestVersionName() + " " + aboutMenu.getComment());
            fileDesc.setText("安装包大小:" + aboutMenu.getSize());
            downloadProgress.setText("");
            if (aboutMenu.getHas_update()) {
                installBtn.setEnabled(true);
                installBtn.setText("立即更新");
                progressBarHorizontal.setProgress(0);
                //如果有下载任务则继续下载
                if (aboutMenu.getDownloadingVersionCode() == null) {
                    //还没有开始下载
                } else if (aboutMenu.getDownloadingVersionCode() >= aboutMenu.getNewestVersionCode()) {
                    //当前下载任务没有过时,继续下载
                    if (aboutMenu.getProgress() != null) {
                        progressBarHorizontal.setProgress(aboutMenu.getProgress());
                        downloadProgress.setText("已下载：" + StringUtil.byteToM(aboutMenu.getProgressSize())
                                + "M/" + aboutMenu.getSize());
                        if (aboutMenu.getProgress() >= 100) {
                            installBtn.setText("安装");
                            downloadProgress.setText("已下载");
                        }
                    }
                } else {
                    //有比当前下载的包还新的包，则删除下载任务，开始下载最新的任务
                }
            } else {
                installBtn.setText("已是最新");
                installBtn.setEnabled(false);
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
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

    /**
     * 监控进度
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUpdateEvent(UpdateEvent event) {
        switch (event.getType()) {
            case UpdateEvent.downloading:
                if (event.getIsDownloading()) {
                    progressBarHorizontal.setProgress((int) (event.getCurrent() * 100 / event.getTotal()));
                    downloadProgress.setText("已下载：" + StringUtil.byteToM(event.getCurrent())
                            + "M/" + (StringUtil.byteToM(event.getTotal()) + "M"));
                }
                break;
            case UpdateEvent.finish:
                installBtn.setText("安装");
                downloadProgress.setText("已下载");
                break;
            case UpdateEvent.error:
                break;
        }
    }

    @Event(value = {R.id.installBtn})
    private void onInstallClick(View view) {
        String btnState = installBtn.getText().toString();
        switch (btnState) {
            case "已是最新":
                break;
            case "立即更新":
                try {
                    AboutMenu aboutMenu = aboutMenuDao.findFirst();
                    EventBus.getDefault().post(new UpdateEvent(UpdateEvent.begin, aboutMenu));
                } catch (DbException e) {
                    e.printStackTrace();
                }
                break;
            case "安装":
                try {
                    AboutMenu aboutMenu = aboutMenuDao.findFirst();
                    String[] sps = aboutMenu.getUrl().split("/");
                    String fileName = sps[sps.length - 1];
                    String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/GongXing/" + fileName;
                    File file = new File(path);
                    if(file.exists()){
                        UpdateEvent updateEvent = new UpdateEvent(UpdateEvent.finish, file);
                        updateEvent.setContext(this);
                        EventBus.getDefault().post(updateEvent);
                    }
                } catch (DbException e) {
                    e.printStackTrace();
                }
                break;
        }
    }
}
