package com.ten.aditum.back.personas.label;

import com.ten.aditum.back.BaseAnalysor;
import com.ten.aditum.back.entity.PersonasLabel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 基于排名的数据分析
 */
@Slf4j
@Component
@EnableScheduling
@EnableAutoConfiguration
public class AnalysorSeven extends BaseAnalysor {

    @Override
    public void showModelLabel() {
        PersonasLabel label1 = new PersonasLabel()
                .setLabelId("19")
                .setLabelName("高级会员")
                .setLabelDesc("访问量前10%");
        PersonasLabel label2 = new PersonasLabel()
                .setLabelId("20")
                .setLabelName("普通会员")
                .setLabelDesc("访问量10-50%");
    }

//    @Scheduled(cron = TEST_TIME)

    /**
     * 每天5点00分更新
     */
    @Scheduled(cron = "0 00 5 1/1 * ?")
    public void analysis() {
        log.info("开始更新社区信息...");

        log.info("社区信息更新完成...");
    }
}
