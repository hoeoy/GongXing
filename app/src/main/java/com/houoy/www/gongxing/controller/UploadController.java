package com.houoy.www.gongxing.controller;

import com.alibaba.fastjson.JSON;
import com.houoy.www.gongxing.GongXingApplication;
import com.houoy.www.gongxing.dao.AboutMenuDao;
import com.houoy.www.gongxing.event.UpdateEvent;
import com.houoy.www.gongxing.model.AboutMenu;
import com.houoy.www.gongxing.util.AppUtil;
import com.houoy.www.gongxing.util.XUtil;
import com.houoy.www.gongxing.util.XUtilCallBack;

import org.greenrobot.eventbus.EventBus;
import org.xutils.ex.DbException;

/**
 * 更新先关controller
 * Created by andyzhao on 1/14/2018.
 */
public class UploadController {

    private static UploadController uploadController = null;

    public static String ip = "http://appupdate.51jfjk.com";
    private static String defaultUrl = ip + ":9032/";

    private AboutMenuDao aboutMenuDao;

    private UploadController() {
        aboutMenuDao = AboutMenuDao.getInstant();
    }

    public static UploadController getInstant() {
        if (uploadController == null) {
            uploadController = new UploadController();
        }
        return uploadController;
    }

    public void initAboutMenu() {
        String url = defaultUrl + "/api/apk/check";
        XUtil.Get(url, null, new XUtilCallBack<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    int appCurrentCode = AppUtil.getVersionCode(GongXingApplication.gongXingApplication.getBaseContext());
                    String appCurrentName = AppUtil.getVersionName(GongXingApplication.gongXingApplication.getBaseContext());
                    AboutMenu daoAM = aboutMenuDao.findFirst();
                    AboutMenu resultVO = JSON.parseObject(result, AboutMenu.class);
                    if (resultVO == null) {
                        if (daoAM == null) {
                            //如果没有初始化数据，则初始化
                            daoAM = new AboutMenu();
                            daoAM.setMenu_name("版本");
                            daoAM.setHas_update(false);
                            daoAM.setMenu_code(appCurrentName);
                            daoAM.setCurrentVersionCode(appCurrentCode);
                            daoAM.setCurrentVersionName(appCurrentName);
                            aboutMenuDao.add(daoAM);
                        } else {
                            //如果已经存在数据，有可能正在下载
                            daoAM.setCurrentVersionName(appCurrentName);
                            daoAM.setCurrentVersionCode(appCurrentCode);
                            aboutMenuDao.update(daoAM);
                        }
                    } else {
                        if (appCurrentCode < resultVO.getNewestVersionCode()) {
                            //如果本机安装包版本小于服务器最新版本
                            resultVO.setMenu_name("版本");
                            resultVO.setMenu_code(resultVO.getNewestVersionName());
                            resultVO.setHas_update(true);
                            resultVO.setCurrentVersionCode(appCurrentCode);
                            resultVO.setCurrentVersionName(appCurrentName);
                            if (daoAM == null) {
                                //如果没有初始化数据，则初始化
                                aboutMenuDao.add(resultVO);
                            } else {
                                //如果已经存在数据
                                resultVO.setId(daoAM.getId());
                                if (daoAM.getDownloadingVersionCode() != null &&
                                        daoAM.getNewestVersionCode() >= resultVO.getNewestVersionCode()) {
                                    //如果本地缓存的最新版本等于服务器最新版本，则有可能正在下载，使用本地缓存状态
                                    resultVO.setDownloadingVersionCode(daoAM.getDownloadingVersionCode());
                                    resultVO.setProgress(daoAM.getProgress());
                                }else{
                                    //TODO 清空GongXing目录,或者删除文件

                                }
                                aboutMenuDao.update(resultVO);
                            }

                            EventBus.getDefault().post(new UpdateEvent(UpdateEvent.newVersion, null));
                        } else {
                            daoAM.setHas_update(false);
                            daoAM.setMenu_code(resultVO.getNewestVersionName());
                            daoAM.setCurrentVersionCode(resultVO.getNewestVersionCode());
                            daoAM.setCurrentVersionName(resultVO.getNewestVersionName());
                            //如果服务器不是最新版本
                            if (daoAM == null) {
                                //如果没有初始化数据，则初始化
                                daoAM = new AboutMenu();
                                daoAM.setMenu_name("版本");
                                aboutMenuDao.add(daoAM);
                            } else {
                                //如果已经存在数据
                                aboutMenuDao.update(daoAM);
                            }
                        }
                    }

                    EventBus.getDefault().post(new UpdateEvent(UpdateEvent.checkVersion, null));
                } catch (DbException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
            }
        });
    }
}
