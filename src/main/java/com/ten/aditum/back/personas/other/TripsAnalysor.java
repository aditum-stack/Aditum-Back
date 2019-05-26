package com.ten.aditum.back.personas.other;

import com.ten.aditum.back.BaseAnalysor;
import com.ten.aditum.back.entity.AccessTime;
import com.ten.aditum.back.entity.Person;
import com.ten.aditum.back.service.AccessTimeService;
import com.ten.aditum.back.service.CommunityService;
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
 * 出行量排名动态标签
 */
@Slf4j
@Component
@EnableScheduling
@EnableAutoConfiguration
public class TripsAnalysor extends BaseAnalysor {

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
     * 每天0点30分执行
     */
    @Scheduled(cron = "0 30 0 1/1 * ?")
    public void analysis() {
        log.info("出行量排名动态标签...开始");

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

        for (int i = 0; i < size; i++) {
            // 击败了多少的用户
            int index = i + 1;
            double rank = (1.0 - ((double) index / size)) * 100;
            // 取前四位 99.3%
            String rankS;
            if (String.valueOf(rank).length() > 4) {
                rankS = String.valueOf(rank).substring(0, 4) + "%";
            } else {
                rankS = rank + "%";
            }
            this.analysisRank(personAccessCountList.get(i), rankS);
        }

        log.info("出行量排名动态标签...结束");
    }

    private PersonAccessCount analysisPerson(Person person) {
        // 获取AccessTime
        AccessTime accessTimeEntity = new AccessTime()
                .setPersonnelId(person.getPersonnelId())
                .setIsDeleted(NO_DELETED);
        List<AccessTime> select = accessTimeService.select(accessTimeEntity);
        if (select.size() < 1) {
            log.info("此用户还没有AccessTime记录, {}", person.getPersonnelName());
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

    private void analysisRank(PersonAccessCount person, String rank) {
        Personas old = new Personas()
                .setPersonnelId(person.getPersonnelId())
                .setLabelName("击败了");
        personasService.removeFuzzyPersonasByKey(old);

        String labelName = "击败了" + rank + "的用户";
        Personas label = new Personas()
                .setPersonnelId(person.getPersonnelId())
                .setLabelName(labelName);
        personasService.updatePersonasByLabelName(label);
        log.info("用户 {} 访问排名计算完成，{}", person.getPersonnelName(), labelName);
    }

}
