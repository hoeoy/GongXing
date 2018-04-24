package com.houoy.www.gongxing.service;

/**
 * Created by andyzhao on 4/23/2018.
 */


import android.app.Service;
import android.content.Intent;
import android.os.Environment;
import android.os.IBinder;

import com.houoy.www.gongxing.dao.AboutMenuDao;
import com.houoy.www.gongxing.event.UpdateEvent;
import com.houoy.www.gongxing.model.AboutMenu;
import com.houoy.www.gongxing.util.XUtil;

import org.greenrobot.eventbus.EventBus;
import org.xutils.common.Callback;
import org.xutils.ex.DbException;

import java.io.File;

public class DownLoadService extends Service {

    private AboutMenuDao aboutMenuDao;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        aboutMenuDao = AboutMenuDao.getInstant();
        //接收从Activity发来的数据
        String url = intent.getStringExtra("url");
        //启动下载任务
        xUtilsDownload(url);
        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * 利用xUtils进行断点续传下载
     */
    private void xUtilsDownload(String url) {
        String[] sps = url.split("/");
        String fileName = sps[sps.length - 1];

        String path = Environment.getExternalStorageDirectory().getPath() + "/GongXing/" + fileName;
        Callback.Cancelable cancelable = XUtil.DownLoadFile(url, path, new Callback.ProgressCallback<File>() {
            @Override
            public void onCancelled(CancelledException arg0) {
                System.out.println("onCancelled");
            }

            @Override
            public void onError(Throwable arg0, boolean arg1) {
                // TODO Auto-generated method stub
                System.out.println("onError" + arg0);
            }

            @Override
            public void onFinished() {
                // TODO Auto-generated method stub
                System.out.println("onFinished");
            }

            @Override
            public void onSuccess(File file) {
                EventBus.getDefault().post(new UpdateEvent(UpdateEvent.finish, file));
            }

            @Override
            public void onLoading(long total, long current, boolean isDownloading) {
                try {
                    AboutMenu aboutMenu = aboutMenuDao.findFirst();
                    aboutMenu.setDownloadingVersionCode(aboutMenu.getNewestVersionCode());
                    aboutMenu.setProgress((int) (current* 100 / total));
                    aboutMenu.setProgressSize(current);
                    aboutMenuDao.update(aboutMenu);

                    UpdateEvent updateEvent = new UpdateEvent(UpdateEvent.downloading, null);
                    updateEvent.setCurrent(current);
                    updateEvent.setTotal(total);
                    updateEvent.setIsDownloading(isDownloading);
                    EventBus.getDefault().post(updateEvent);
                } catch (DbException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onStarted() {
                // TODO Auto-generated method stub
            }

            @Override
            public void onWaiting() {
                // TODO Auto-generated method stub
            }
        });
    }

}