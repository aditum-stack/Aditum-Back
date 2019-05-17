package com.ten.aditum.back.statistic.person;


import com.ten.aditum.back.BaseAnalysor;
import com.ten.aditum.back.entity.AccessTime;
import com.ten.aditum.back.entity.Person;
import com.ten.aditum.back.entity.Record;
import com.ten.aditum.back.service.AccessTimeService;
import com.ten.aditum.back.service.PersonService;
import com.ten.aditum.back.service.RecordService;
import com.ten.aditum.back.util.TimeGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

    private final RecordService recordService;

    private final AccessTimeService accessTimeService;

    @Autowired
    public TimeAnalyzer(AccessTimeService accessTimeService,
                        RecordService recordService) {
        this.accessTimeService = accessTimeService;
        this.recordService = recordService;
    }

    /**
     * 每天1点分析用户访问时间
     */
//    @Scheduled(cron = TEST_TIME)
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

        // person的访问地址集合
        Map<String, List<String>> recordDayMap = new HashMap<>();

        log.info("开始分析此person : {}", person);

        // 查询person的所有record
        Record recordEntity = new Record()
                .setPersonnelId(person.getPersonnelId())
                .setIsDeleted(NO_DELETED);
        List<Record> recordList = recordService.select(recordEntity);

        if (recordList.size() == 0) {
            log.warn("此person {}没有任何访问记录!", person.getPersonnelName());
            return;
        }

        recordList.forEach(record -> {
            String visiteTime = record.getVisiteTime().substring(0, 19);
            record.setVisiteTime(visiteTime);
            String formatDate = formatDate(visiteTime);

            // 若已经包含此日期，添加访问时间到集合
            if (recordDayMap.containsKey(formatDate)) {
                List<String> originRecordDayList = recordDayMap.get(formatDate);
                originRecordDayList.add(record.getVisiteTime());
            }
            // 若未包含此日期，初始化
            else {
                List<String> newRecordDayList = new ArrayList<>();
                newRecordDayList.add(record.getVisiteTime());
                recordDayMap.put(formatDate, newRecordDayList);
            }
        });

        // ------------------------------------------ 一个person的所有record遍历结束

        // 每天最早访问时间集合
        List<String> earliestAccessTimeList = new ArrayList<>();

        // 每天最晚访问时间集合
        List<String> latestAccessTimeList = new ArrayList<>();

        // 每天访问次数
        List<Integer> dailyFrequencyList = new ArrayList<>();

        // 按天遍历集合
        for (Map.Entry<String, List<String>> recordDay : recordDayMap.entrySet()) {
            List<String> timeList = recordDay.getValue();

            if (timeList.size() < 1) {
                log.error("TimeList is null!");
                continue;
            }
            // 每天最早时间
            String earlies = timeList.stream().min(String::compareTo).get();
            // 每天最晚时间
            String latest = timeList.stream().max(String::compareTo).get();
            // 每天访问次数
            Integer count = timeList.size();

            earliestAccessTimeList.add(formatTime(earlies));
            latestAccessTimeList.add(formatTime(latest));
            dailyFrequencyList.add(count);
        }

        int dayCount = recordDayMap.entrySet().size();

        if (dayCount == 0) {
            log.warn("此person {}没有任何访问记录!", person.getPersonnelName());
            return;
        }

        log.info("最早时间集合{}", earliestAccessTimeList);
        log.info("最晚时间集合{}", latestAccessTimeList);
        log.info("每天访问次数{}", dailyFrequencyList);

        String min = averageTime(earliestAccessTimeList);
        String max = averageTime(latestAccessTimeList);
        Integer countMean = dailyFrequencyList
                .stream()
                .reduce(0, ((integer, integer2) -> integer + integer2))
                / dayCount;

        log.info("获取到此person {} 下的所有时间 {}min {}max {}count", person.getPersonnelName(), min, max, countMean);

        // 生成数据对象
        AccessTime accessTime = new AccessTime()
                .setPersonnelId(person.getPersonnelId())
                .setAverageEarliestAccessTime(min)
                .setAverageEarliestAccessCount(dayCount)
                .setAverageLatestAccessTime(max)
                .setAverageLatestAccessCount(dayCount)
                .setAverageDailyFrequency(countMean)
                .setAverageDailyFrequencyCount(dayCount)
                .setIsDeleted(NO_DELETED);

        // 查询此person的原纪录
        AccessTime accessTimeEntity = new AccessTime()
                .setPersonnelId(person.getPersonnelId())
                .setIsDeleted(NO_DELETED);
        List<AccessTime> accessTimeList = accessTimeService.select(accessTimeEntity);
        // 当前用户不存在记录，创建
        if (accessTimeList.size() < 1) {
            accessTime
                    .setCreateTime(TimeGenerator.currentTime());
            accessTimeService.insert(accessTime);

            log.info("此person {} 还没有时间记录，插入 {}", person.getPersonnelName(), accessTime);
        }
        // 当前用户已有记录，更新
        else {
            AccessTime origin = accessTimeList.get(0);
            Integer id = origin.getId();
            accessTime
                    .setId(id)
                    .setUpdateTime(TimeGenerator.currentTime());
            accessTimeService.update(accessTime);

            log.info("此person {} 已经有时间记录，更新 {}", person.getPersonnelName(), accessTime);
        }
    }

}
