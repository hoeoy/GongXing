package com.houoy.www.gongxing.util;

import android.os.Environment;

import java.io.File;

public class FileHelper {
    /*
    Environment 常用方法：
        * 方法：getDataDirectory()
   解释：返回 File ，获取 Android 数据目录。
           * 方法：getDownloadCacheDirectory()
   解释：返回 File ，获取 Android 下载/缓存内容目录。
           * 方法：getExternalStorageDirectory()
   解释：返回 File ，获取外部存储目录即 SDCard
   * 方法：getExternalStoragePublicDirectory(String type)
   解释：返回 File ，取一个高端的公用的外部存储器目录来摆放某些类型的文件
   * 方法：getExternalStorageState()
   解释：返回 File ，获取外部存储设备的当前状态
   * 方法：getRootDirectory()
   解释：返回 File ，获取 Android 的根目录
   */
    public static void getSDPath() {
        File sdDir = null;
        File sdDir1 = null;
        File sdDir2 = null;
        boolean sdCardExist = Environment.getExternalStorageState()
                .equals(Environment.MEDIA_MOUNTED); //判断sd卡是否存在
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();//获取跟目录
            sdDir1 = Environment.getDataDirectory();
            sdDir2 = Environment.getRootDirectory();
        }
        System.out.println("getExternalStorageDirectory(): " + sdDir.toString());
        System.out.println("getDataDirectory(): " + sdDir1.toString());
        System.out.println("getRootDirectory(): " + sdDir2.toString());
    }

    public static String getRootPath() {
        File root = Environment.getRootDirectory();//获取跟目录
        return root.getName();
    }

    public static String getESDPath() {
        File root = Environment.getExternalStorageDirectory();
        File root1 = Environment.getDataDirectory();
        File root2 = Environment.getDownloadCacheDirectory();
        File root4 = Environment.getExternalStoragePublicDirectory("sdf");
        return root.getName();
    }

    //判断一个路径下的文件（文件夹）是否存在
    public static void main(String[] args) {
        isExist("e://12");
    }

    /**
     * @param path 文件夹路径
     */
    public static void isExist(String path) {
        File file = new File(path);
        //判断文件夹是否存在,如果不存在则创建文件夹
        if (!file.exists()) {
            file.mkdir();
        }
    }

    private String searchFile(String keyword) {
        String result = "";
        File[] files = new File("/").listFiles();
        for (File file : files) {
            if (file.getName().indexOf(keyword) >= 0) {
                result += file.getPath() + "\n";
            }
        }
        if (result.equals("")) {
            result = "找不到文件!!";
        }
        return result;
    }
}