package com.ten.aditum.back.util;

import org.springframework.stereotype.Component;

import java.sql.Timestamp;

@Component
public class TimeGenerator {

    public String currentTime() {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        return String.valueOf(timestamp);
    }
}
