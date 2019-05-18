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
 * 基于用户信息的数据分析
 */
@Slf4j
@Component
@EnableScheduling
@EnableAutoConfiguration
public class AnalysorFour extends BaseAnalysor {

    private final AccessTimeService accessTimeService;

    @Autowired
    public AnalysorFour(AccessTimeService accessTimeService) {
        this.accessTimeService = accessTimeService;
    }

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

//    @Scheduled(cron = TEST_TIME)

    /**
     * 每天4点30分更新
     */
    @Scheduled(cron = "0 30 4 1/1 * ?")
    public void analysis() {
        log.info("基于用户信息的数据分析...开始");

        List<Person> personList = selectAllPerson();

        personList.forEach(this::analysisPerson);

        log.info("基于用户信息的数据分析...结束");
    }

    private void analysisPerson(Person person) {
        // 获取AccessTime
        AccessTime accessTimeEntity = new AccessTime()
                .setPersonnelId(person.getPersonnelId())
                .setIsDeleted(NO_DELETED);
        List<AccessTime> select = accessTimeService.select(accessTimeEntity);
        if (select.size() < 1) {
            log.info("此用户还没有AccessTime记录, {}", person.getPersonnelName());
            return;
        }

        // 获取AccessTime
        AccessTime theAccessTime = select.get(0);
        int totalDay = theAccessTime.getAverageDailyFrequencyCount();

        // 标签集合
        List<String> labelSet = new ArrayList<>();
        // 标签删除集合
        List<String> removeSet = new ArrayList<>();

        // 用户创建时间小于一星期
        if (totalDay < 7) {
            labelSet.add("新用户");
            Personas personas = new Personas()
                    .setPersonnelId(person.getPersonnelId())
                    .setLabelId("10");
            personasController.updatePersonas(personas);

            removeSet.add("老用户");
            Personas remove = new Personas()
                    .setPersonnelId(person.getPersonnelId())
                    .setLabelId("11");
            personasController.removePersonas(remove);
        }
        // 用户创建时间大于一个月
        if (totalDay > 30) {
            labelSet.add("老用户");
            Personas personas = new Personas()
                    .setPersonnelId(person.getPersonnelId())
                    .setLabelId("11");
            personasController.updatePersonas(personas);

            removeSet.add("新用户");
            Personas remove = new Personas()
                    .setPersonnelId(person.getPersonnelId())
                    .setLabelId("10");
            personasController.removePersonas(remove);
        }

        log.info("用户 {} 计算完成。添加 : {} , 删除 : {}",
                person.getPersonnelName(), String.join(",", labelSet), String.join(",", removeSet));
    }

}
