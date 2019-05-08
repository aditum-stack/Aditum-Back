package com.ten.aditum.back.schedule;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public interface Analyzer {
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

}
