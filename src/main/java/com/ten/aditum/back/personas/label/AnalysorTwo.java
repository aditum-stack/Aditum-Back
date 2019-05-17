package com.ten.aditum.back.personas.label;

import com.ten.aditum.back.entity.PersonasLabel;
import com.ten.aditum.back.BaseAnalysor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 基于早或晚时间的一维分析
 */
@Slf4j
@Component
@EnableScheduling
public class AnalysorTwo extends BaseAnalysor {

    @Override
    public void showModelLabel() {
        PersonasLabel label1 = new PersonasLabel()
                .setLabelId("4")
                .setLabelName("加班狂")
                .setLabelDesc("晚上访问时间晚于十点");
        PersonasLabel label2 = new PersonasLabel()
                .setLabelId("5")
                .setLabelName("早起达人")
                .setLabelDesc("早上访问时间早于八点");
        PersonasLabel label3 = new PersonasLabel()
                .setLabelId("17")
                .setLabelName("夜猫子")
                .setLabelDesc("晚上时间晚于11点");
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
