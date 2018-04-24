package com.houoy.www.gongxing.dao;

import android.util.Log;

import com.houoy.www.gongxing.GongXingApplication;
import com.houoy.www.gongxing.model.AboutMenu;
import com.houoy.www.gongxing.model.ChatHouse;
import com.houoy.www.gongxing.model.ChatTalker;
import com.houoy.www.gongxing.model.ClientInfo;
import com.houoy.www.gongxing.model.MessagePushAlert;
import com.houoy.www.gongxing.model.MessagePushDaily;
import com.houoy.www.gongxing.vo.MessageVO;
import com.houoy.www.gongxing.model.Setting;

import org.xutils.DbManager;
import org.xutils.db.table.TableEntity;
import org.xutils.ex.DbException;
import org.xutils.x;

/**
 * Created by andyzhao on 1/26/2018.
 */

public class DBHelper {
    public static DbManager db;

    public static void initDb() {
        //本地数据的初始化
        DbManager.DaoConfig daoConfig = new DbManager.DaoConfig()
                .setDbName(GongXingApplication.DB_Name) //设置数据库名
                .setDbVersion(GongXingApplication.DB_Version) //设置数据库版本,每次启动应用时将会检查该版本号,
                //发现数据库版本低于这里设置的值将进行数据库升级并触发DbUpgradeListener
                .setAllowTransaction(false)//设置是否开启事务,默认为false关闭事务
                .setTableCreateListener(new DbManager.TableCreateListener() {
                    @Override
                    public void onTableCreated(DbManager db, TableEntity<?> table) {
                        db.getDatabase().enableWriteAheadLogging();
                        //balabala...
                    }
                })//设置数据库创建时的Listener
                .setDbUpgradeListener(new DbManager.DbUpgradeListener() {
                    @Override
                    public void onUpgrade(DbManager db, int oldVersion, int newVersion) {
                        try {
                            db.dropTable(ClientInfo.class);
                            db.dropTable(MessagePushAlert.class);
                            db.dropTable(MessagePushDaily.class);
                            db.dropTable(Setting.class);
                            db.dropTable(ChatHouse.class);
                            db.dropTable(ChatTalker.class);
                            db.dropTable(AboutMenu.class);
                        } catch (DbException e) {
                            Log.e(e.getMessage(), e.getLocalizedMessage());
                        }
                        //balabala...
                    }
                });//设置数据库升级时的Listener,这里可以执行相关数据库表的相关修改,比如alter语句增加字段等
        //.setDbDir(null);//设置数据库.db文件存放的目录,默认为包名下databases目录下
        db = x.getDb(daoConfig);
    }
}
