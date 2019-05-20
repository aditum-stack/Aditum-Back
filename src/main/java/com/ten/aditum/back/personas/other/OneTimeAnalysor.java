package com.ten.aditum.back.personas.other;


import com.ten.aditum.back.BaseAnalysor;
import com.ten.aditum.back.entity.Person;
import com.ten.aditum.back.vo.Personas;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;
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
                .setLabelId("job1");
        personasService.removePersonas(personas);
        Personas personas2 = new Personas()
                .setPersonnelId(person.getPersonnelId())
                .setLabelId("job2");
        personasService.removePersonas(personas2);

        log.info("用户 {} 计算完成，{}", person.getPersonnelName());
    }

}
