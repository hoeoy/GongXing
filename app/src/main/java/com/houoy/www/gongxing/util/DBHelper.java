package com.houoy.www.gongxing.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;

import java.io.File;

public class DBHelper extends SQLiteOpenHelper {

    //数据库版本号
    private static final int DATABASE_VERSION = 1;

    //数据库名称
    private static final String DATABASE_NAME = Environment.getExternalStorageDirectory() + File.separator + "gongxing.db";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
//        String messaga_push = "create table messaga_push(" +
//                "    touser                CHAR(100)," +
//                "    title_value            VARCHAR2(200)," +
//                "    title_color            VARCHAR2(20)," +
//                "    rule_name_value            VARCHAR2(200)," +
//                "    rule_name_color            VARCHAR2(20)," +
//                "    trigger_time_value            VARCHAR2(200)," +
//                "    trigger_time_color            VARCHAR2(20)," +
//                "    device_name_value            VARCHAR2(200)," +
//                "    device_name_color            VARCHAR2(20)," +
//                "    subkey_name_value            VARCHAR2(200)," +
//                "    subkey_name_color            VARCHAR2(20)," +
//                "    current_parameter_value            VARCHAR2(200)," +
//                "    current_parameter_color            VARCHAR2(20)," +
//                "    remark_value            VARCHAR2(200)," +
//                "    remark_color            VARCHAR2(20)," +
//                "    RelationID            VARCHAR2(100)," +
//                "    date TimeStamp not null default (datetime('now','localtime'))," +
//                ")";
//
//        String messaga_daily_push = "create table messaga_daily_push(" +
//                "    touser                CHAR(100)," +
//                "    title_value            VARCHAR2(200)," +
//                "    title_color            VARCHAR2(20)," +
//                "    temperature_value            VARCHAR2(200)," +
//                "    temperature_color            VARCHAR2(20)," +
//                "    humidity_value            VARCHAR2(200)," +
//                "    humidity_color            VARCHAR2(20)," +
//                "    state_value            VARCHAR2(200)," +
//                "    state_color            VARCHAR2(20)," +
//                "    alarm_num_value            VARCHAR2(200)," +
//                "    alarm_num_color            VARCHAR2(20)," +
//                "    remark_value            VARCHAR2(200)," +
//                "    remark_color            VARCHAR2(20)," +
//                "    RelationID            VARCHAR2(100)," +
//                "    date TimeStamp not null default (datetime('now','localtime'))," +
//                ")";
//
//        db.execSQL(messaga_push);
//        db.execSQL(messaga_daily_push);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //如果旧表存在，删除，所以数据将会消失
        db.execSQL("DROP TABLE IF EXISTS messaga_push");
        db.execSQL("DROP TABLE IF EXISTS messaga_daily_push");
        //再次创建表
        onCreate(db);
    }

}



