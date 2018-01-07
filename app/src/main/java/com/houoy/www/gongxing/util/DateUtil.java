package com.houoy.www.gongxing.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import lombok.extern.java.Log;

/**
 * 日期相关计算辅助类<br/>
 */
@Log
public class DateUtil {

    public static final String FORMAT_STR = "yyyy-MM-dd HH:mm:ss";

    /**
     * 获得当前日期对应的上海所在时区的日期
     *
     * @return 日期字符串
     */
    public static String getNowDateShanghai() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        TimeZone tz = TimeZone.getTimeZone("Asia/Shanghai");
        sdf.setTimeZone(tz);
        String dateStr = sdf.format(new Date());
        return dateStr;
    }

    /**
     * 获得当前时间对应的上海所在时区的时间
     *
     * @return 日期字符串
     */
    public static String getNowDateTimeShanghai() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        TimeZone tz = TimeZone.getTimeZone("Asia/Shanghai");
        sdf.setTimeZone(tz);
        String dateStr = sdf.format(new Date());
        return dateStr;
    }

    /**
     * 获得指定日期的上海所在时区日期
     *
     * @param date
     * @return
     */
    public static String getDateShanghai(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        TimeZone tz = TimeZone.getTimeZone("Asia/Shanghai");
        sdf.setTimeZone(tz);
        String dateStr = sdf.format(date);
        return dateStr;
    }

    /**
     * 获得指定时间的上海所在时区时间
     *
     * @param date
     * @return
     */
    public static String getDateTimeShanghai(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        TimeZone tz = TimeZone.getTimeZone("Asia/Shanghai");
        sdf.setTimeZone(tz);
        String dateStr = sdf.format(date);
        return dateStr;
    }


}
