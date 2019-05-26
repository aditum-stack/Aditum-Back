package com.ten.aditum.back.util;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * 时间工具类
 */
public class TimeGenerator {

    private static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String TIME_FORMAT = "HH:mm:ss";
    private static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * 获取当前时间戳
     */
    public static String currentTime() {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        return String.valueOf(timestamp);
    }

    /**
     * 当前时间的年月日时分秒
     */
    public static String currentDateTime() {
        DateFormat fmt = new SimpleDateFormat(DATE_TIME_FORMAT);
        return fmt.format(new Date());
    }

    /**
     * 返回当前时间的前一个小时的年月日时分秒
     */
    public static String hourBeforeDateTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) - 1);
        SimpleDateFormat df = new SimpleDateFormat(DATE_TIME_FORMAT);
        return df.format(calendar.getTime());
    }

    /**
     * 昨天的的年月日时分秒
     */
    public static String yesterdayDateTime() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        DateFormat fmt = new SimpleDateFormat(DATE_TIME_FORMAT);
        return fmt.format(cal.getTime());
    }

    /**
     * 判断年月日时分秒是不是昨天
     */
    public static boolean isYesterday(String dateTime) {
        String yesterday = yesterdayDateTime();
        String today = currentDateTime();
        return dateTime.compareTo(yesterday) > 0 && today.compareTo(dateTime) > 0;
    }

    /**
     * 年月日时分秒时间字符串转年月日日期字符串
     */
    public static String formatDate(String value) {
        if (value == null) {
            return "";
        }
        DateFormat fmt = new SimpleDateFormat(DATE_FORMAT);
        Date date;
        try {
            date = fmt.parse(value);
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
        return new SimpleDateFormat(DATE_FORMAT).format(date);
    }

    /**
     * 年月日时分秒时间字符串转时分秒时间字符串
     */
    public static String formatTime(String value) {
        if (value == null) {
            return "";
        }
        // 去掉日期，保留时间
        return value.substring(11);
    }

    /**
     * 时分秒时间转秒
     */
    public static long getTotalSec(String s) {
        String[] my = s.split(":");
        int hour = Integer.parseInt(my[0]);
        int min = Integer.parseInt(my[1]);
        int sec = Integer.parseInt(my[2]);
        return (long) (hour * 3600 + min * 60 + sec);
    }

    /**
     * 秒转时间时分秒
     */
    public static String getTimeFromSec(long sec) {
        // 毫秒数
        long ms = sec * 1000;
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        // 设置时区，防止+8小时
        formatter.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));
        return formatter.format(ms);
    }

    /**
     * 计算时分秒的平均时间
     */
    public static String averageTime(List<String> timeList) {
        if (timeList.size() == 0) {
            return "";
        }
        long[] times = new long[timeList.size()];
        for (int i = 0; i < timeList.size(); i++) {
            long totalSec = getTotalSec(timeList.get(i));
            times[i] = totalSec;
        }
        long total = 0;
        for (long time1 : times) {
            total += time1;
        }
        long mean = total / times.length;
        return getTimeFromSec(mean);
    }

}
