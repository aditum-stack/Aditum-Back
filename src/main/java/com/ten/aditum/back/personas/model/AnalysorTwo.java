package com.ten.aditum.back.personas.model;

import com.ten.aditum.back.BaseAnalysor;
import com.ten.aditum.back.entity.AccessTime;
import com.ten.aditum.back.entity.Person;
import com.ten.aditum.back.entity.PersonasLabel;
import com.ten.aditum.back.service.AccessTimeService;
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
 * 基于早或晚时间的一维分析
 */
@Slf4j
@Component
@EnableScheduling
@EnableAutoConfiguration
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

//    @Scheduled(cron = TEST_TIME)

    /**
     * 每天4点10分更新
     */
    @Scheduled(cron = "0 10 4 1/1 * ?")
    public void analysis() {
        log.info("基于早或晚时间的一维分析...开始");
        List<Person> personList = selectAllPerson();
        personList.forEach(this::analysisPerson);
        log.info("基于早或晚时间的一维分析...结束");
    }

    private long label1 = TimeGenerator.getTotalSec("22:00:00");
    private long label2 = TimeGenerator.getTotalSec("8:00:00");
    private long label3 = TimeGenerator.getTotalSec("23:00:00");

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
        String earliest = theAccessTime.getAverageEarliestAccessTime();
        String latest = theAccessTime.getAverageLatestAccessTime();
        long es = TimeGenerator.getTotalSec(earliest);
        long ls = TimeGenerator.getTotalSec(latest);

        List<String> labelSet = new ArrayList<>();

        // 晚上访问时间晚于十点
        if (ls > label1) {
            labelSet.add("加班狂");
            Personas personas = new Personas()
                    .setPersonnelId(person.getPersonnelId())
                    .setLabelId("4");
            personasService.updatePersonas(personas);
        }
        // 早上访问时间早于八点
        if (es < label2) {
            labelSet.add("早起达人");
            Personas personas = new Personas()
                    .setPersonnelId(person.getPersonnelId())
                    .setLabelId("5");
            personasService.updatePersonas(personas);
        }
        // 晚上时间晚于11点
        if (ls > label3) {
            labelSet.add("夜猫子");
            Personas personas = new Personas()
                    .setPersonnelId(person.getPersonnelId())
                    .setLabelId("17");
            personasService.updatePersonas(personas);
        }

        log.info("用户 {} 计算完成，{}", person.getPersonnelName(), String.join(",", labelSet));
    }

}
