package com.ten.aditum.back.personas.label;

import com.ten.aditum.back.entity.PersonasLabel;
import com.ten.aditum.back.BaseAnalysor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 基于排名的数据分析
 */
@Slf4j
@Component
@EnableScheduling
public class AnalysorSix extends BaseAnalysor {

    @Override
    public void showModelLabel() {
        PersonasLabel label1 = new PersonasLabel()
                .setLabelId("16")
                .setLabelName("高购买力人群")
                .setLabelDesc("每天工作时间超过十小时");
        PersonasLabel label2 = new PersonasLabel()
                .setLabelId("21")
                .setLabelName("倾向于待在家里")
                .setLabelDesc("每天从晚上到早上在家10个小时");
        PersonasLabel label3 = new PersonasLabel()
                .setLabelId("22")
                .setLabelName("在外时间长")
                .setLabelDesc("每天在外面12个小时");
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
