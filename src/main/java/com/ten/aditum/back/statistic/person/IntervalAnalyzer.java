package com.ten.aditum.back.statistic.person;


import com.ten.aditum.back.BaseAnalysor;
import com.ten.aditum.back.entity.AccessInterval;
import com.ten.aditum.back.entity.AccessTime;
import com.ten.aditum.back.entity.Person;
import com.ten.aditum.back.entity.Record;
import com.ten.aditum.back.service.AccessIntervalService;
import com.ten.aditum.back.service.AccessTimeService;
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
import static com.ten.aditum.back.util.TimeGenerator.averageTime;

@Slf4j
@Component
@EnableScheduling
@EnableAutoConfiguration
public class IntervalAnalyzer extends BaseAnalysor {

    private final AccessTimeService accessTimeService;
    private final AccessIntervalService accessIntervalService;

    @Autowired
    public IntervalAnalyzer(AccessTimeService accessTimeService, AccessIntervalService accessIntervalService) {
        this.accessTimeService = accessTimeService;
        this.accessIntervalService = accessIntervalService;
    }

    /**
     * 每天1点20分分析用户访问间隔
     */
//    @Scheduled(cron = TEST_TIME)
    @Scheduled(cron = "0 20 1 1/1 * ?")
    public void analysis() {
        log.info("开始分析用户访问间隔...");

        List<Person> personList = selectAllPerson();

        personList.forEach(this::analysisPerson);

        log.info("用户访问间隔分析完成...");
    }

    private static final long DAY = 60 * 60 * 24;

    /**
     * 分析单个person的行为
     */
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

        // 获取AccessTime
        AccessTime theAccessTime = select.get(0);
        String earliest = theAccessTime.getAverageEarliestAccessTime();
        String latest = theAccessTime.getAverageLatestAccessTime();
        // 总访问天数
        Integer dayCount = theAccessTime.getAverageDailyFrequencyCount();

        long es = TimeGenerator.getTotalSec(earliest);
        long ls = TimeGenerator.getTotalSec(latest);

        // FIXME 若只有一次访问，则为0
        long workTimeS = ls - es;
        long lifeTimeS = DAY - workTimeS;

        // 每天外出工作时间
        String workTime = getTimeFromSec(workTimeS);
        // 每天在家滞留时间
        String lifeTime = getTimeFromSec(lifeTimeS);

        // 插入数据

        // 生成数据对象
        AccessInterval accessInterval = new AccessInterval()
                .setPersonnelId(person.getPersonnelId())
                .setMeanTimeRetention(lifeTime)
                .setFirstAddressCount(dayCount)
                .setMeanTimeOut(workTime)
                .setSecondAddressCount(dayCount)
                .setIsDeleted(NO_DELETED);

        // 查询此person的原纪录
        AccessInterval accessIntervalEntity = new AccessInterval()
                .setPersonnelId(person.getPersonnelId())
                .setIsDeleted(NO_DELETED);
        List<AccessInterval> accessIntervalList = accessIntervalService.select(accessIntervalEntity);
        // 当前用户不存在记录，创建
        if (accessIntervalList.size() < 1) {
            accessInterval
                    .setCreateTime(TimeGenerator.currentTime());
            accessIntervalService.insert(accessInterval);

            log.info("此person {} 还没有时间间隔记录，插入 {}", person.getPersonnelName(), accessInterval);
        }
        // 当前用户已有记录，更新
        else {
            AccessInterval origin = accessIntervalList.get(0);
            Integer id = origin.getId();
            accessInterval
                    .setId(id)
                    .setUpdateTime(TimeGenerator.currentTime());
            accessIntervalService.update(accessInterval);

            log.info("此person {} 已经有时间间隔记录，更新 {}", person.getPersonnelName(), accessInterval);
        }
    }

}
