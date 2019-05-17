package com.ten.aditum.back.personas.label;

import com.ten.aditum.back.entity.PersonasLabel;
import com.ten.aditum.back.BaseAnalysor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 基于用户访问地理信息的分析
 */
@Slf4j
@Component
@EnableScheduling
public class AnalysorFive extends BaseAnalysor {

    @Override
    public void showModelLabel() {
        PersonasLabel label1 = new PersonasLabel()
                .setLabelId("13")
                .setLabelName("出差狂魔")
                .setLabelDesc("去过5个以上的地方");
        PersonasLabel label2 = new PersonasLabel()
                .setLabelId("14")
                .setLabelName("宅")
                .setLabelDesc("只去过一个地方");
        PersonasLabel label3 = new PersonasLabel()
                .setLabelId("18")
                .setLabelName("朋友圈")
                .setLabelDesc("最常访问的社区次数大于100次");
    }

//    @Scheduled(cron = TEST_TIME)

    /**
     * 每天4点40分更新
     */
    @Scheduled(cron = "0 40 4 1/1 * ?")
    public void analysis() {
        log.info("开始更新社区信息...");

        log.info("社区信息更新完成...");
    }
}
