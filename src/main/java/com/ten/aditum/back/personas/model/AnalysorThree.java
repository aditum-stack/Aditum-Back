package com.ten.aditum.back.personas.model;

import com.ten.aditum.back.BaseAnalysor;
import com.ten.aditum.back.entity.AccessTime;
import com.ten.aditum.back.entity.Person;
import com.ten.aditum.back.entity.PersonasLabel;
import com.ten.aditum.back.service.AccessTimeService;
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
 * 基于访问频次的一维分析
 */
@Slf4j
@Component
@EnableScheduling
@EnableAutoConfiguration
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
        log.info("基于访问频次的一维分析...开始");
        List<Person> personList = selectAllPerson();
        personList.forEach(this::analysisPerson);
        log.info("基于访问频次的一维分析...结束");
    }

    private void analysisPerson(Person person) {
        AccessTime accessTimeEntity = new AccessTime()
                .setPersonnelId(person.getPersonnelId())
                .setIsDeleted(NO_DELETED);
        List<AccessTime> select = accessTimeService.select(accessTimeEntity);
        if (select.size() < 1) {
            log.info("此用户还没有AccessTime记录, {}", person.getPersonnelName());
            return;
        }

        AccessTime theAccessTime = select.get(0);
        int totalDay = theAccessTime.getAverageDailyFrequencyCount();
        int everyDayAccess = theAccessTime.getAverageDailyFrequency();

        // 标签集合
        List<String> labelSet = new ArrayList<>();
        // 标签删除集合
        List<String> removeSet = new ArrayList<>();

        // 访问总量
        int totalCount = totalDay * everyDayAccess;

        // 门禁总访问量过100
        if (totalCount > 100) {
            labelSet.add("门禁忠实用户");
            Personas personas = new Personas()
                    .setPersonnelId(person.getPersonnelId())
                    .setLabelId("8");
            personasService.updatePersonas(personas);

            removeSet.add("门禁无感用户");
            Personas remove = new Personas()
                    .setPersonnelId(person.getPersonnelId())
                    .setLabelId("9");
            personasService.removePersonas(remove);
        }
        // 门禁总访问量小于10
        if (totalCount < 10) {
            labelSet.add("门禁无感用户");
            Personas personas = new Personas()
                    .setPersonnelId(person.getPersonnelId())
                    .setLabelId("9");
            personasService.updatePersonas(personas);

            removeSet.add("门禁忠实用户");
            Personas remove = new Personas()
                    .setPersonnelId(person.getPersonnelId())
                    .setLabelId("8");
            personasService.removePersonas(remove);
        }

        // 访问频率

        // 每天访问门禁大于等于十次
        if (everyDayAccess >= 10) {
            labelSet.add("门禁达人");
            Personas personas = new Personas()
                    .setPersonnelId(person.getPersonnelId())
                    .setLabelId("6");
            personasService.updatePersonas(personas);

            removeSet.add("不怎么使用");
            Personas remove = new Personas()
                    .setPersonnelId(person.getPersonnelId())
                    .setLabelId("9");
            personasService.removePersonas(remove);
        }
        // 每天访问门禁小于等于两次
        if (everyDayAccess <= 2) {
            labelSet.add("不怎么使用");
            Personas personas = new Personas()
                    .setPersonnelId(person.getPersonnelId())
                    .setLabelId("9");
            personasService.updatePersonas(personas);

            removeSet.add("门禁达人");
            Personas remove2 = new Personas()
                    .setPersonnelId(person.getPersonnelId())
                    .setLabelId("6");
            personasService.removePersonas(remove2);
        }
        // 用户每天访问频率大于20次
        if (everyDayAccess >= 20) {
            labelSet.add("潜在营销用户");
            Personas personas = new Personas()
                    .setPersonnelId(person.getPersonnelId())
                    .setLabelId("12");
            personasService.updatePersonas(personas);
        }

        log.info("用户 {} 计算完成。添加 : {} , 删除 : {}",
                person.getPersonnelName(), String.join(",", labelSet), String.join(",", removeSet));
    }

}
