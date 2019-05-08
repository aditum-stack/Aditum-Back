package com.ten.aditum.back.schedule;


import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Slf4j
@Configuration
@EnableScheduling
@EnableAutoConfiguration
public class IntervalAnalyzer {

    /**
     * 每天2点分析用户访问间隔
     */
    @Scheduled(cron = "0 0 2 1/1 * ?")
    public void analysis() {

    }

}
