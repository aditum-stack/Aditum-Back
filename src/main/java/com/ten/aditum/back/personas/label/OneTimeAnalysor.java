package com.ten.aditum.back.personas.label;


import com.ten.aditum.back.statistic.BaseAnalysor;
import com.ten.aditum.back.entity.Person;
import com.ten.aditum.back.vo.Personas;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 做一些其他的操作，例如一次性整理等
 */
@Slf4j
@Component
@EnableScheduling
@EnableAutoConfiguration
public class OneTimeAnalysor extends BaseAnalysor {

    /**
     * 一次性任务
     */
//    @Scheduled(cron = TEST_TIME)
    public void analysis() {
        log.info("一次性任务...开始");
        List<Person> personList = selectAllPerson();
        personList.forEach(this::analysisPerson);
        log.info("一次性任务...结束");
    }

    /**
     * 清除无效的标签
     */
    private void analysisPerson(Person person) {
        Personas personas = new Personas()
                .setPersonnelId(person.getPersonnelId())
                .setLabelName("灞呬綇");
        personasService.removeFuzzyPersonasByKey(personas);
        Personas personas2 = new Personas()
                .setPersonnelId(person.getPersonnelId())
                .setLabelName("杩愯惀鍟�");
        personasService.removeFuzzyPersonasByKey(personas2);
        Personas personas3 = new Personas()
                .setPersonnelId(person.getPersonnelId())
                .setLabelName("鍑昏触浜�");
        personasService.removeFuzzyPersonasByKey(personas3);

        log.info("用户一次性 {}{} 删除完成，", person.getId(), person.getPersonnelName());
    }

}
