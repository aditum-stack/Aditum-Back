package com.ten.aditum.back.statistic.device;


import com.ten.aditum.back.BaseAnalysor;
import com.ten.aditum.back.entity.Device;
import com.ten.aditum.back.entity.DeviceAccessHeat;
import com.ten.aditum.back.entity.Record;
import com.ten.aditum.back.service.DeviceAccessHeatService;
import com.ten.aditum.back.service.DeviceService;
import com.ten.aditum.back.service.RecordService;
import com.ten.aditum.back.util.TimeGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.sql.Time;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static com.ten.aditum.back.util.TimeGenerator.hourBeforeDateTime;


@Slf4j
@Component
@EnableScheduling
@EnableAutoConfiguration
public class DeviceHeatAnalyzer extends BaseAnalysor {

//    @Scheduled(cron = TEST_TIME)

    /**
     * 每小时更新设备实时访问日志(热度)
     */
    @Scheduled(cron = "0 0 0/1 * * ?")
    public void analysis() {
        log.info("开始更新设备热度...");
        List<Device> deviceList = selectAllDevice();
        deviceList.forEach(this::analysisDevice);
        log.info("设备热度更新完成...");
    }

    /**
     * 分析设备上一个小时的访问热度
     */
    private void analysisDevice(Device device) {
        // 上个小时00:00时刻
        String hourBeforeDateTime = TimeGenerator.hourBeforeDateTime();
        String hourBeforeDate = hourBeforeDateTime.substring(0, 14);
        String hourBeforeZeroDateTime = hourBeforeDate + "00:00";

        // 上个小时是否已更新
        DeviceAccessHeat selectEntity = new DeviceAccessHeat()
                .setImei(device.getImei())
                .setCurrentHourTime(hourBeforeZeroDateTime)
                .setIsDeleted(NO_DELETED);
        List<DeviceAccessHeat> select = deviceAccessHeatService.select(selectEntity);
        if (select.size() > 0) {
            log.warn("当前时间已更新 {}", hourBeforeZeroDateTime);
            return;
        }

        // 上个小时的访问记录
        Record recordEntity = new Record()
                .setImei(device.getImei())
                .setVisiteTime(hourBeforeZeroDateTime)
                .setIsDeleted(NO_DELETED);
        List<Record> recordList = recordService.selectAfterTheDateTime(recordEntity);
        if (recordList.size() == 0) {
            log.warn("device {} 本小时 {} 没有任何访问记录!", device.getAlias(), hourBeforeZeroDateTime);
            return;
        }

        AtomicInteger currentHourCount = new AtomicInteger(0);
        AtomicInteger currentHourInCount = new AtomicInteger(0);
        AtomicInteger currentHourOutCount = new AtomicInteger(0);
        recordList.forEach(record -> {
            String visiteTime = record.getVisiteTime().substring(0, 19);
            record.setVisiteTime(visiteTime);
            // 若record时间大于前一小时，访问次数+1
            if (visiteTime.compareTo(hourBeforeZeroDateTime) > 0) {
                currentHourCount.getAndIncrement();
                // 出入类型
                if (record.getVisiteStatus() == 0) {
                    currentHourInCount.getAndIncrement();
                } else if (record.getVisiteStatus() == 1) {
                    currentHourOutCount.getAndIncrement();
                } else {
                    log.warn("记录访问失败！{}", record);
                }
            }
        });

        DeviceAccessHeat accessHeat = new DeviceAccessHeat()
                .setImei(device.getImei())
                .setCurrentHourTime(hourBeforeZeroDateTime)
                .setCurrentHourCount(currentHourCount.get())
                .setCurrentHourInCount(currentHourInCount.get())
                .setCurrentHourOutCount(currentHourOutCount.get())
                .setCreateTime(TimeGenerator.currentDateTime())
                .setUpdateTime(TimeGenerator.currentDateTime())
                .setIsDeleted(NO_DELETED);
        deviceAccessHeatService.insert(accessHeat);
    }

}
