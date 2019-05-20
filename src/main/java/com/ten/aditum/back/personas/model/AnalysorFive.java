package com.ten.aditum.back.personas.model;

import com.ten.aditum.back.BaseAnalysor;
import com.ten.aditum.back.entity.AccessAddress;
import com.ten.aditum.back.entity.Person;
import com.ten.aditum.back.entity.PersonasLabel;
import com.ten.aditum.back.service.AccessAddressService;
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
 * 基于用户访问地理信息的分析
 */
@Slf4j
@Component
@EnableScheduling
@EnableAutoConfiguration
public class AnalysorFive extends BaseAnalysor {

    private final AccessAddressService accessAddressService;

    @Autowired
    public AnalysorFive(AccessAddressService accessAddressService) {
        this.accessAddressService = accessAddressService;
    }

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
        log.info("基于用户访问地理信息的分析...开始");

        List<Person> personList = selectAllPerson();

        personList.forEach(this::analysisPerson);

        log.info("基于用户访问地理信息的分析...结束");
    }

    private void analysisPerson(Person person) {
        // 获取AccessAddress
        AccessAddress accessAddressEntity = new AccessAddress()
                .setPersonnelId(person.getPersonnelId())
                .setIsDeleted(NO_DELETED);
        List<AccessAddress> select = accessAddressService.select(accessAddressEntity);
        if (select.size() < 1) {
            log.info("此用户还没有AccessAddress记录, {}", person.getPersonnelName());
            return;
        }

        AccessAddress theAccessAddress = select.get(0);
        // 访问的次数
        Integer firstAddressCount = theAccessAddress.getFirstAddressCount();
        Integer totalCount = theAccessAddress.getTotalCount();
        // 去过多少地方
        Integer totalAddressCount = theAccessAddress.getTotalAddressCount();

        // 标签集合
        List<String> labelSet = new ArrayList<>();
        // 标签删除集合
        List<String> removeSet = new ArrayList<>();

        // 去过5个以上的地方
        if (totalAddressCount > 5) {
            labelSet.add("出差狂魔");
            Personas personas = new Personas()
                    .setPersonnelId(person.getPersonnelId())
                    .setLabelId("13");
            personasService.updatePersonas(personas);

            removeSet.add("宅");
            Personas remove = new Personas()
                    .setPersonnelId(person.getPersonnelId())
                    .setLabelId("14");
            personasService.removePersonas(remove);
        }
        // 只去过一个地方
        if (totalAddressCount < 2) {
            labelSet.add("宅");
            Personas personas = new Personas()
                    .setPersonnelId(person.getPersonnelId())
                    .setLabelId("14");
            personasService.updatePersonas(personas);

            removeSet.add("出差狂魔");
            Personas remove = new Personas()
                    .setPersonnelId(person.getPersonnelId())
                    .setLabelId("13");
            personasService.removePersonas(remove);
        }
        // 最常访问的社区次数大于100次
        if (firstAddressCount > 100) {
            labelSet.add("朋友圈");
            Personas personas = new Personas()
                    .setPersonnelId(person.getPersonnelId())
                    .setLabelId("18");
            personasService.updatePersonas(personas);
        }

        log.info("用户 {} 计算完成。添加 : {} , 删除 : {}",
                person.getPersonnelName(), String.join(",", labelSet), String.join(",", removeSet));
    }

}
