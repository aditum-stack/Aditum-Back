package com.ten.aditum.back.personas.label;

import com.ten.aditum.back.entity.PersonasLabel;
import com.ten.aditum.back.BaseAnalysor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 基于用户信息的数据分析
 */
@Slf4j
@Component
@EnableScheduling
public class AnalysorFour extends BaseAnalysor {

    @Override
    public void showModelLabel() {
        PersonasLabel label1 = new PersonasLabel()
                .setLabelId("10")
                .setLabelName("新用户")
                .setLabelDesc("用户创建时间小于一星期");
        PersonasLabel label2 = new PersonasLabel()
                .setLabelId("11")
                .setLabelName("老用户")
                .setLabelDesc("用户创建时间大于一个月");
    }

    /**
     *
     */
    @Scheduled(cron = "0 0 0 1/1 * ?")
    public void analysis() {
        log.info("开始更新社区信息...");

        log.info("社区信息更新完成...");
    }
}
