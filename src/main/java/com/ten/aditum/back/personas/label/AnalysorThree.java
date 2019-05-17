package com.ten.aditum.back.personas.label;

import com.ten.aditum.back.entity.PersonasLabel;
import com.ten.aditum.back.BaseAnalysor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 基于访问频次的一维分析
 */
@Slf4j
@Component
@EnableScheduling
public class AnalysorThree extends BaseAnalysor {

    @Override
    public void showModelLabel() {
        PersonasLabel label1 = new PersonasLabel()
                .setLabelId("6")
                .setLabelName("门禁达人")
                .setLabelDesc("每天访问门禁大于等于十次");
        PersonasLabel label2 = new PersonasLabel()
                .setLabelId("7")
                .setLabelName("不怎么使用")
                .setLabelDesc("每天访问门禁小于等于两次");
        PersonasLabel label3 = new PersonasLabel()
                .setLabelId("8")
                .setLabelName("门禁忠实用户")
                .setLabelDesc("门禁总访问量过100");
        PersonasLabel label4 = new PersonasLabel()
                .setLabelId("9")
                .setLabelName("门禁无感用户")
                .setLabelDesc("门禁总访问量小于10");
        PersonasLabel label5 = new PersonasLabel()
                .setLabelId("12")
                .setLabelName("潜在营销用户")
                .setLabelDesc("用户每天访问频率大于20次");
    }

//    @Scheduled(cron = TEST_TIME)

    /**
     * 每天4点20分更新
     */
    @Scheduled(cron = "0 20 4 1/1 * ?")
    public void analysis() {
        log.info("开始更新社区信息...");

        log.info("社区信息更新完成...");
    }
}
