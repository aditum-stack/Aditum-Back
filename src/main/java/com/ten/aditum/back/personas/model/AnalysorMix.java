package com.ten.aditum.back.personas.model;

import com.ten.aditum.back.entity.AccessAddress;
import com.ten.aditum.back.entity.AccessTime;
import com.ten.aditum.back.entity.Person;
import com.ten.aditum.back.entity.PersonasLabel;
import com.ten.aditum.back.BaseAnalysor;
import com.ten.aditum.back.service.AccessAddressService;
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
 * 混合分析，标签结合
 */
@Slf4j
@Component
@EnableScheduling
@EnableAutoConfiguration
public class AnalysorMix extends BaseAnalysor {

    private final AccessTimeService accessTimeService;
    private final AccessAddressService accessAddressService;

    @Autowired
    public AnalysorMix(AccessTimeService accessTimeService, AccessAddressService accessAddressService) {
        this.accessTimeService = accessTimeService;
        this.accessAddressService = accessAddressService;
    }

    @Override
    public void showModelLabel() {
        PersonasLabel label1 = new PersonasLabel()
                .setLabelId("15")
                .setLabelType(35)
                .setLabelName("居家达人")
                .setLabelDesc("每天访问一个地方的频率大于十次");
    }

//    @Scheduled(cron = TEST_TIME)

    /**
     * 每天5点10分更新
     */
    @Scheduled(cron = "0 10 5 1/1 * ?")
    public void analysis() {
        log.info("混合分析，标签结合...开始");

        List<Person> personList = selectAllPerson();

        personList.forEach(this::analysisPerson);

        log.info("混合分析，标签结合...开始");
    }

    private void analysisPerson(Person person) {
        // 获取AccessTime
        AccessTime accessTimeEntity = new AccessTime()
                .setPersonnelId(person.getPersonnelId())
                .setIsDeleted(NO_DELETED);
        List<AccessTime> select = accessTimeService.select(accessTimeEntity);
        if (select.size() < 1) {
            log.info("此用户还没有AccessTime记录, {}", person);
            return;
        }

        // 获取AccessAddress
        AccessAddress accessAddressEntity = new AccessAddress()
                .setPersonnelId(person.getPersonnelId())
                .setIsDeleted(NO_DELETED);
        List<AccessAddress> select2 = accessAddressService.select(accessAddressEntity);
        if (select2.size() < 1) {
            log.info("此用户还没有AccessAddress记录, {}", person);
            return;
        }

        // 获取AccessTime
        AccessTime theAccessTime = select.get(0);
        Integer totalDay = theAccessTime.getAverageDailyFrequencyCount();

        // 获取AccessAddress
        AccessAddress theAccessAddress = select2.get(0);
        // 访问首地址的次数
        Integer firstAddressCount = theAccessAddress.getFirstAddressCount();

        // 每天访问一个地方的频率
        int frequency = firstAddressCount / totalDay;

        List<String> labelSet = new ArrayList<>();

        // 每天访问一个地方的频率大于十次
        if (frequency >= 10) {
            labelSet.add("居家达人");
            Personas personas = new Personas()
                    .setPersonnelId(person.getPersonnelId())
                    .setLabelId("15");
            personasController.updatePersonas(personas);
        }

        log.info("用户 {} 计算完成，{}", person.getPersonnelName(), String.join(",", labelSet));
    }
}
