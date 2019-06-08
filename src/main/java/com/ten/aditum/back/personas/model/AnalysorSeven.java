package com.ten.aditum.back.personas.model;

import com.ten.aditum.back.statistic.BaseAnalysor;
import com.ten.aditum.back.entity.AccessTime;
import com.ten.aditum.back.entity.Person;
import com.ten.aditum.back.entity.PersonasLabel;
import com.ten.aditum.back.service.AccessTimeService;
import com.ten.aditum.back.vo.Personas;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 基于排名的数据分析
 */
@Slf4j
@Component
@EnableScheduling
@EnableAutoConfiguration
public class AnalysorSeven extends BaseAnalysor {

    private final AccessTimeService accessTimeService;

    @Autowired
    public AnalysorSeven(AccessTimeService accessTimeService) {
        this.accessTimeService = accessTimeService;
    }

    @Override
    public void showModelLabel() {
        PersonasLabel label1 = new PersonasLabel()
                .setLabelId("19")
                .setLabelName("黄金会员")
                .setLabelDesc("访问量前10%");
        PersonasLabel label2 = new PersonasLabel()
                .setLabelId("20")
                .setLabelName("白银会员")
                .setLabelDesc("访问量10-50%");
        PersonasLabel label3 = new PersonasLabel()
                .setLabelId("24")
                .setLabelName("青铜会员")
                .setLabelDesc("访问量50-100%");
    }

    /**
     * 用户访问量排序辅助类
     */
    @Data
    @AllArgsConstructor
    private static class PersonAccessCount {
        String personnelId;
        String personnelName;
        Integer totalCount;
    }

//    @Scheduled(cron = TEST_TIME)

    /**
     * 每天5点00分更新
     */
    @Scheduled(cron = "0 0 5 1/1 * ?")
    public void analysis() {
        log.info("基于排名的数据分析...开始");
        List<Person> personList = selectAllPerson();
        // 用户访问量队列
        List<PersonAccessCount> personAccessCountList = new ArrayList<>(personList.size());
        for (Person person : personList) {
            PersonAccessCount obj = analysisPerson(person);
            if (obj == null) {
                obj = new PersonAccessCount(person.getPersonnelId(), person.getPersonnelName(), 0);
            }
            personAccessCountList.add(obj);
        }
        // 基于访问量进行排序
        List<PersonAccessCount> sorted = personAccessCountList.stream()
                .sorted((o1, o2) -> o2.totalCount.compareTo(o1.totalCount))
                .collect(Collectors.toList());

        int size = sorted.size();
        // 前10%
        int high = size / 10;
        // 前50%
        int common = size / 2;

        // 遍历
        for (int i = 0; i < size; i++) {
            // 前10%
            if (i <= high) {
                analysisSort(sorted.get(i), "19");
            }
            // 10-50
            else if (i <= common) {
                analysisSort(sorted.get(i), "20");
            }
            // 其他
            else {
                analysisSort(sorted.get(i), "24");
            }
        }

        log.info("基于排名的数据分析...结束");
    }

    private PersonAccessCount analysisPerson(Person person) {
        // 获取AccessTime
        AccessTime accessTimeEntity = new AccessTime()
                .setPersonnelId(person.getPersonnelId())
                .setIsDeleted(NO_DELETED);
        List<AccessTime> select = accessTimeService.select(accessTimeEntity);
        if (select.size() < 1) {
            log.debug("此用户还没有AccessTime记录, {}", person.getPersonnelName());
            return null;
        }
        // 获取AccessTime
        AccessTime theAccessTime = select.get(0);
        int totalDay = theAccessTime.getAverageDailyFrequencyCount();
        int everyDayAccess = theAccessTime.getAverageDailyFrequency();
        // 访问总量
        int totalCount = totalDay * everyDayAccess;
        return new PersonAccessCount(person.getPersonnelId(), person.getPersonnelName(), totalCount);
    }

    private void analysisSort(PersonAccessCount person, String labelId) {
        // 标签集合
        List<String> labelSet = new ArrayList<>();
        // 标签删除集合
        List<String> removeSet = new ArrayList<>();

        // 黄金会员
        if ("19".equals(labelId)) {
            labelSet.add("黄金会员");
            Personas personas = new Personas()
                    .setPersonnelId(person.getPersonnelId())
                    .setLabelId(labelId);
            personasService.updatePersonas(personas);

            removeSet.add("白银会员");
            Personas remove = new Personas()
                    .setPersonnelId(person.getPersonnelId())
                    .setLabelId("20");
            personasService.removePersonas(remove);
            removeSet.add("青铜会员");
            Personas remove2 = new Personas()
                    .setPersonnelId(person.getPersonnelId())
                    .setLabelId("24");
            personasService.removePersonas(remove2);
        }
        // 白银会员
        if ("20".equals(labelId)) {
            labelSet.add("白银会员");
            Personas personas = new Personas()
                    .setPersonnelId(person.getPersonnelId())
                    .setLabelId(labelId);
            personasService.updatePersonas(personas);

            removeSet.add("黄金会员");
            Personas remove = new Personas()
                    .setPersonnelId(person.getPersonnelId())
                    .setLabelId("19");
            personasService.removePersonas(remove);
            removeSet.add("青铜会员");
            Personas remove2 = new Personas()
                    .setPersonnelId(person.getPersonnelId())
                    .setLabelId("24");
            personasService.removePersonas(remove2);
        }
        // 青铜会员
        if ("24".equals(labelId)) {
            labelSet.add("青铜会员");
            Personas personas = new Personas()
                    .setPersonnelId(person.getPersonnelId())
                    .setLabelId(labelId);
            personasService.updatePersonas(personas);

            removeSet.add("黄金会员");
            Personas remove = new Personas()
                    .setPersonnelId(person.getPersonnelId())
                    .setLabelId("19");
            personasService.removePersonas(remove);
            removeSet.add("白银会员");
            Personas remove2 = new Personas()
                    .setPersonnelId(person.getPersonnelId())
                    .setLabelId("20");
            personasService.removePersonas(remove2);
        }

        log.debug("用户 {} 计算完成。添加 : {} , 删除 : {}",
                person.getPersonnelName(), String.join(",", labelSet), String.join(",", removeSet));
    }
}
