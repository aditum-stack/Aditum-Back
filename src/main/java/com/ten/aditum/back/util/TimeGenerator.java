package com.ten.aditum.back.util;

import java.sql.Timestamp;

public class TimeGenerator {

    public static String currentTime() {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        return String.valueOf(timestamp);
    }

}
