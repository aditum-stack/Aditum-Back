package com.ten.aditum.back.schedule;


import com.ten.aditum.back.entity.Device;
import com.ten.aditum.back.entity.DeviceAccessHeat;
import com.ten.aditum.back.entity.Record;
import com.ten.aditum.back.service.DeviceAccessHeatService;
import com.ten.aditum.back.service.DeviceService;
import com.ten.aditum.back.service.RecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Component
@EnableScheduling
@EnableAutoConfiguration
public class DeviceHeatAnalyzer implements Analyzer {

    private final DeviceService deviceService;
    private final RecordService recordService;
    private final DeviceAccessHeatService deviceAccessHeatService;

    @Autowired
    public DeviceHeatAnalyzer(RecordService recordService,
                              DeviceAccessHeatService deviceAccessHeatService,
                              DeviceService deviceService) {
        this.recordService = recordService;
        this.deviceAccessHeatService = deviceAccessHeatService;
        this.deviceService = deviceService;
    }

    /**
     * 每小时更新设备实时访问日志(热度)
     */
//    @Scheduled(cron = TEST_TIME)
    @Scheduled(cron = "0 0 0/1 * * ?")
    public void analysis() {
        log.info("开始更新设备热度...");

        Device deviceEntity = new Device()
                .setIsDeleted(NO_DELETED);
        List<Device> deviceList = deviceService.select(deviceEntity);

        log.info("查询所有device集合 : {}", deviceList);

        deviceList.forEach(this::analysisDevice);

        log.info("设备热度更新完成...");
    }

    private void analysisDevice(Device device) {
        Record recordEntity = new Record()
                .setImei(device.getImei())
                .setIsDeleted(NO_DELETED);
        List<Record> recordList = recordService.select(recordEntity);

        if (recordList.size() == 0) {
            log.warn("此device {} 本小时没有任何访问记录!", device.getAlias());
            return;
        }

        String hourBefore = hourBeforeDateTime();
        String beforeCurrentHour = hourBefore.substring(0, 14) + "00:00";

        log.info("前一小时时间 {} , 格式化后 {} ", hourBefore, beforeCurrentHour);

        AtomicInteger currentHourCount = new AtomicInteger(0);
        AtomicInteger currentHourInCount = new AtomicInteger(0);
        AtomicInteger currentHourOutCount = new AtomicInteger(0);

        recordList.forEach(record -> {
            log.info("开始分析此record : {}", record);

            String visiteTime = record.getVisiteTime().substring(0, 19);
            record.setVisiteTime(visiteTime);

            // 若record时间大于前一小时，访问次数+1
            if (visiteTime.compareTo(beforeCurrentHour) > 0) {
                currentHourCount.getAndIncrement();

                // 出入类型
                if (record.getVisiteStatus() == 0) {
                    currentHourInCount.getAndIncrement();
                } else if (record.getVisiteStatus() == 1) {
                    currentHourOutCount.getAndIncrement();
                } else {
                    log.warn("此记录访问失败！{}", record);
                }
            }
        });

        DeviceAccessHeat selectEntity = new DeviceAccessHeat()
                .setImei(device.getImei())
                .setCurrentHourTime(beforeCurrentHour)
                .setIsDeleted(NO_DELETED);

        List<DeviceAccessHeat> select = deviceAccessHeatService.select(selectEntity);
        if (select.size() > 0) {
            log.warn("当前时间已更新 {}", beforeCurrentHour);
            return;
        }

        DeviceAccessHeat accessHeat = new DeviceAccessHeat()
                .setImei(device.getImei())
                .setCurrentHourTime(beforeCurrentHour)
                .setCurrentHourCount(currentHourCount.get())
                .setCurrentHourInCount(currentHourInCount.get())
                .setCurrentHourOutCount(currentHourOutCount.get())
                .setCreateTime(beforeCurrentHour)
                .setUpdateTime(hourBefore)
                .setIsDeleted(NO_DELETED);

        deviceAccessHeatService.insert(accessHeat);
    }

}
