package com.ten.aditum.back.schedule;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public interface Analyzer {

    String TEST_TIME = "0 0/1 * * * ?";

    int NO_DELETED = 0;

    String DATE_FORMAT = "yyyy-MM-dd";

    default String formatDate(String value) {
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

    String TIME_FORMAT = "hh:mm:ss";

    default String formatTime(String value) {
        if (value == null) {
            return "";
        }
        // 去掉日期，保留时间
        return value.substring(11);
    }

    String DATE_TIME_FORMAT = "yyyy-MM-dd hh:mm:ss";

    default String currentDateTime() {
        DateFormat fmt = new SimpleDateFormat(DATE_TIME_FORMAT);
        return fmt.format(new Date().getHours());
    }

    default String hourBeforeDateTime() {
        Calendar calendar = Calendar.getInstance();
        /* HOUR_OF_DAY 指示一天中的小时 */
        calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) - 1);
        SimpleDateFormat df = new SimpleDateFormat(DATE_TIME_FORMAT);
        return df.format(calendar.getTime());
    }
}
