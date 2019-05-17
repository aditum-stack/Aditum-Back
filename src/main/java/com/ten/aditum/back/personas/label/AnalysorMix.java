package com.ten.aditum.back.personas.label;

import com.ten.aditum.back.entity.PersonasLabel;
import com.ten.aditum.back.BaseAnalysor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 混合分析，标签结合
 */
@Slf4j
@Component
@EnableScheduling
@EnableAutoConfiguration
public class AnalysorMix extends BaseAnalysor {

    @Override
    public void showModelLabel() {
        PersonasLabel label1 = new PersonasLabel()
                .setLabelId("15")
                .setLabelType(35)
                .setLabelName("居家达人")
                .setLabelDesc("每天访问一个地方的频率大于十次");
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
