package com.ten.aditum.back.personas.model;

import com.ten.aditum.back.BaseAnalysor;
import com.ten.aditum.back.entity.AccessInterval;
import com.ten.aditum.back.entity.Person;
import com.ten.aditum.back.entity.PersonasLabel;
import com.ten.aditum.back.service.AccessIntervalService;
import com.ten.aditum.back.util.TimeGenerator;
import com.ten.aditum.back.vo.Personas;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 基于访问时长的分析
 */
@Slf4j
@Component
@EnableScheduling
@EnableAutoConfiguration
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

//    @Scheduled(cron = TEST_TIME)

    /**
     * 每天4点50分更新
     */
    @Scheduled(cron = "0 50 4 1/1 * ?")
    public void analysis() {
        log.info("基于访问时长的分析...开始");

        List<Person> personList = selectAllPerson();

        personList.forEach(this::analysisPerson);

        log.info("基于访问时长的分析...开始");
    }

    private static final long HOUR = 60 * 60;

    private void analysisPerson(Person person) {
        // 获取AccessInterval
        AccessInterval accessIntervalEntity = new AccessInterval()
                .setPersonnelId(person.getPersonnelId())
                .setIsDeleted(NO_DELETED);
        List<AccessInterval> select = accessIntervalService.select(accessIntervalEntity);
        if (select.size() < 1) {
            log.info("此用户还没有AccessInterval记录, {}", person.getPersonnelName());
            return;
        }

        // 获取AccessInterval
        AccessInterval theAccessInterval = select.get(0);
        // 平均在家滞留时间
        String meanTimeRetention = theAccessInterval.getMeanTimeRetention();
        // 平均外出工作时间
        String meanTimeOut = theAccessInterval.getMeanTimeOut();

        long in = TimeGenerator.getTotalSec(meanTimeRetention);
        long out = TimeGenerator.getTotalSec(meanTimeOut);

        if (in == 0 && out == 0) {
            log.warn("此用户 {} 访问间隔为空，退出", person.getPersonnelName());
        }

        // 标签集合
        List<String> labelSet = new ArrayList<>();
        // 标签删除集合
        List<String> removeSet = new ArrayList<>();

        // 每天工作时间超过十小时
        if (out > HOUR * 10) {
            labelSet.add("高购买力人群");
            Personas personas = new Personas()
                    .setPersonnelId(person.getPersonnelId())
                    .setLabelId("16");
            personasService.updatePersonas(personas);
        }
        // 每天从晚上到早上在家10个小时
        if (in > HOUR * 10) {
            labelSet.add("倾向于待在家里");
            Personas personas = new Personas()
                    .setPersonnelId(person.getPersonnelId())
                    .setLabelId("21");
            personasService.updatePersonas(personas);
        }
        // 每天在外面12个小时
        if (out > HOUR * 12) {
            labelSet.add("在外时间长");
            Personas personas = new Personas()
                    .setPersonnelId(person.getPersonnelId())
                    .setLabelId("22");
            personasService.updatePersonas(personas);
        }

        log.info("用户 {} 计算完成。添加 : {} , 删除 : {}",
                person.getPersonnelName(), String.join(",", labelSet), String.join(",", removeSet));
    }
}
