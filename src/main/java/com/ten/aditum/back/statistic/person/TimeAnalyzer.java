package com.ten.aditum.back.statistic.person;


import com.ten.aditum.back.BaseAnalysor;
import com.ten.aditum.back.entity.AccessTime;
import com.ten.aditum.back.entity.Person;
import com.ten.aditum.back.entity.Record;
import com.ten.aditum.back.util.TimeGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ten.aditum.back.util.TimeGenerator.*;

@Slf4j
@Component
@EnableScheduling
@EnableAutoConfiguration
public class TimeAnalyzer extends BaseAnalysor {

//    @Scheduled(cron = TEST_TIME)

    /**
     * 每天1点分析用户访问时间
     */
    @Scheduled(cron = "0 0 1 1/1 * ? ")
    public void analysis() {
        log.info("开始分析用户访问时间...");
        List<Person> personList = selectAllPerson();
        personList.forEach(this::analysisPerson);
        log.info("用户访问时间分析完成...");
    }

    /**
     * 分析单个person的行为
     */
    private void analysisPerson(Person person) {
        Map<String, List<String>> dayTimeMap = new HashMap<>();
        for (int i = 0; ; ) {
            Record recordEntity = new Record()
                    .setId(i)
                    .setPersonnelId(person.getPersonnelId())
                    .setIsDeleted(NO_DELETED);
            List<Record> recordList = recordService.selectAfterTheId(recordEntity);
            if (recordList.size() == 0) {
                return;
            }
            analysisPersonTime(recordList, dayTimeMap);
            // 若数量为SELECT_SIZE，说明后面可能还有未取出的数据
            if (recordList.size() >= SELECT_SIZE) {
                i = recordList.get(SELECT_SIZE - 1).getId();
            }
            // 如数量不足SELECT_SIZE，说明后面已经没有数据了
            else {
                break;
            }
        }

        // 每天最早访问时间集合
        List<String> earliestAccessTimeList = new ArrayList<>();
        // 每天最晚访问时间集合
        List<String> latestAccessTimeList = new ArrayList<>();
        // 总访问次数
        Integer accessCount = 0;
        // 按天遍历集合
        for (Map.Entry<String, List<String>> recordDay : dayTimeMap.entrySet()) {
            List<String> timeList = recordDay.getValue();
            if (timeList.size() < 1) {
                log.error("TimeList is null!");
                continue;
            }
            accessCount += timeList.size();
            // 每天最早时间
            String earlies = timeList.stream().min(String::compareTo).get();
            // 每天最晚时间
            String latest = timeList.stream().max(String::compareTo).get();
            earliestAccessTimeList.add(formatTime(earlies));
            latestAccessTimeList.add(formatTime(latest));
        }

        int dayCount = dayTimeMap.entrySet().size();
        if (dayCount == 0) {
            log.warn("Person {} 没有任何访问记录!", person.getPersonnelName());
            return;
        }

        String min = averageTime(earliestAccessTimeList);
        String max = averageTime(latestAccessTimeList);
        Integer frequency = accessCount / dayCount;

        AccessTime accessTime = new AccessTime()
                .setPersonnelId(person.getPersonnelId())
                .setAverageEarliestAccessTime(min)
                .setAverageEarliestAccessCount(dayCount)
                .setAverageLatestAccessTime(max)
                .setAverageLatestAccessCount(dayCount)
                .setAverageDailyFrequency(frequency)
                .setAverageDailyFrequencyCount(dayCount)
                .setIsDeleted(NO_DELETED);

        AccessTime accessTimeEntity = new AccessTime()
                .setPersonnelId(person.getPersonnelId())
                .setIsDeleted(NO_DELETED);
        List<AccessTime> accessTimeList = accessTimeService.select(accessTimeEntity);
        if (accessTimeList.size() < 1) {
            accessTime
                    .setCreateTime(TimeGenerator.currentTime());
            accessTimeService.insert(accessTime);
            log.info("Person {} 插入 {}min {}max {}f/d",
                    person.getPersonnelName(), min, max, frequency);
        } else {
            AccessTime origin = accessTimeList.get(0);
            Integer id = origin.getId();
            accessTime
                    .setId(id)
                    .setUpdateTime(TimeGenerator.currentTime());
            accessTimeService.update(accessTime);
            log.info("Person {} 更新 {}min {}max {}f/d",
                    person.getPersonnelName(), min, max, frequency);
        }
    }

    /**
     * 分析Record，建立 {@literal <日期，List<访问时间>>}数据结构
     */
    private void analysisPersonTime(List<Record> recordList, Map<String, List<String>> dayTimeMap) {
        recordList.forEach(record -> {
            String visiteTime = record.getVisiteTime().substring(0, 19);
            record.setVisiteTime(visiteTime);
            String formatDate = formatDate(visiteTime);
            if (dayTimeMap.containsKey(formatDate)) {
                List<String> list = dayTimeMap.get(formatDate);
                list.add(visiteTime);
            } else {
                List<String> visiteTimeList = new ArrayList<>();
                visiteTimeList.add(visiteTime);
                dayTimeMap.put(formatDate, visiteTimeList);
            }
        });
    }

}
