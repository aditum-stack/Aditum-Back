package com.ten.aditum.back.statistic.device;


import com.ten.aditum.back.BaseAnalysor;
import com.ten.aditum.back.entity.DeviceAccessLog;
import com.ten.aditum.back.entity.Record;
import com.ten.aditum.back.util.TimeGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@EnableScheduling
@EnableAutoConfiguration
public class DeviceLogAnalyzer extends BaseAnalysor {

//    @Scheduled(cron = TEST_TIME)

    /**
     * 每天3点10分同步昨天设备访问日志
     */
    @Scheduled(cron = "0 10 3 1/1 * ? ")
    public void analysis() {
        log.info("开始同步设备访问日志...");
        // 昨天00:00:00时刻
        String yesterdayDateTime = TimeGenerator.yesterdayDateTime();
        String yesterdayDate = yesterdayDateTime.substring(0, 11);
        String yesterdayZeroDateTime = yesterdayDate + "00:00:00";
        Record recordEntity = new Record()
                .setVisiteTime(yesterdayZeroDateTime)
                .setIsDeleted(NO_DELETED);
        List<Record> recordList = recordService.selectAfterTheDateTime(recordEntity);
        if (recordList.size() == 0) {
            log.warn("昨天 {} 没有任何访问记录!", yesterdayDateTime);
            return;
        }
        recordList.forEach(this::analysisRecord);
        log.info("设备访问日志同步完成...");
    }

    /**
     * 同步更新昨天的访问记录
     */
    private void analysisRecord(Record record) {
        // 判断是不是昨天的记录
        String visiteTime = record.getVisiteTime();
        if (!TimeGenerator.isYesterday(visiteTime)) {
            return;
        }

        DeviceAccessLog accessLogEntity = new DeviceAccessLog()
                .setRecordId(String.valueOf(record.getId()))
                .setIsDeleted(NO_DELETED);
        List<DeviceAccessLog> deviceAccessLogs = deviceAccessLogService.select(accessLogEntity);
        // 此记录还未更新
        if (deviceAccessLogs.size() < 1) {
            DeviceAccessLog accessLog = new DeviceAccessLog()
                    .setImei(record.getImei())
                    .setRecordId(String.valueOf(record.getId()))
                    .setAccessTime(record.getVisiteTime())
                    .setAccessType(String.valueOf(record.getVisiteStatus()))
                    .setCreateTime(TimeGenerator.currentTime())
                    .setUpdateTime(TimeGenerator.currentTime())
                    .setIsDeleted(NO_DELETED);
            deviceAccessLogService.insert(accessLog);
            log.info("此记录完成更新 id:{}", record.getId());
        } else {
            log.debug("此记录已更新过 id:{}", record.getId());
        }
    }
}
