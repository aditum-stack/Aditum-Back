package com.ten.aditum.back.statistic.device;


import com.ten.aditum.back.BaseAnalysor;
import com.ten.aditum.back.entity.DeviceAccessLog;
import com.ten.aditum.back.entity.Record;
import com.ten.aditum.back.service.DeviceAccessLogService;
import com.ten.aditum.back.service.RecordService;
import com.ten.aditum.back.util.TimeGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@EnableScheduling
@EnableAutoConfiguration
public class DeviceLogAnalyzer  extends BaseAnalysor {

    private final RecordService recordService;
    private final DeviceAccessLogService deviceAccessLogService;

    @Autowired
    public DeviceLogAnalyzer(RecordService recordService,
                             DeviceAccessLogService deviceAccessLogService) {
        this.recordService = recordService;
        this.deviceAccessLogService = deviceAccessLogService;
    }

    /**
     * 每天3点10分更新设备访问日志
     */
//    @Scheduled(cron = TEST_TIME)
    @Scheduled(cron = "0 10 3 1/1 * ? ")
    public void analysis() {
        log.info("开始更新设备访问日志...");

        // 查询所有record
        Record recordEntity = new Record()
                .setIsDeleted(NO_DELETED);
        List<Record> recordList = recordService.select(recordEntity);

        recordList.forEach(this::analysisRecord);

        log.info("设备访问日志更新完成...");
    }

    private void analysisRecord(Record record) {
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
