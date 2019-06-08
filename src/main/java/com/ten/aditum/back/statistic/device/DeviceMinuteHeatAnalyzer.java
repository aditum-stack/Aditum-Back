package com.ten.aditum.back.statistic.device;


import com.ten.aditum.back.entity.Device;
import com.ten.aditum.back.entity.DeviceAccessMinuteHeat;
import com.ten.aditum.back.entity.Record;
import com.ten.aditum.back.statistic.BaseAnalysor;
import com.ten.aditum.back.util.TimeGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;


@Slf4j
@Component
@EnableScheduling
@EnableAutoConfiguration
public class DeviceMinuteHeatAnalyzer extends BaseAnalysor {

    /**
     * 每分钟更新设备实时访问日志(热度)
     */
    @Scheduled(cron = "0 0/1 * * * ?")
    public void analysis() {
        log.info("开始更新设备实时热度...");
        List<Device> deviceList = selectAllDevice();
        deviceList.forEach(this::analysisDevice);
        log.info("设备实时热度更新完成...");
    }

    /**
     * 分析设备前一分钟的访问热度
     */
    private void analysisDevice(Device device) {
        // 前一分钟的00秒
        String minuteBeforeDateTime = TimeGenerator.minuteBeforeDateTime();
        String minuteBeforeDate = minuteBeforeDateTime.substring(0, 17);
        String minuteBeforeZeroDateTime = minuteBeforeDate + "00";

        // 前一分钟是否已更新
        DeviceAccessMinuteHeat selectEntity = new DeviceAccessMinuteHeat()
                .setImei(device.getImei())
                .setCurrentMinuteTime(minuteBeforeZeroDateTime)
                .setIsDeleted(NO_DELETED);
        List<DeviceAccessMinuteHeat> select = deviceAccessMinuteHeatService.select(selectEntity);
        if (select.size() > 0) {
            return;
        }

        AtomicInteger currentMinuteCount = new AtomicInteger(0);
        AtomicInteger currentMinuteInCount = new AtomicInteger(0);
        AtomicInteger currentMinuteOutCount = new AtomicInteger(0);

        // 前一分钟的访问记录
        Record recordEntity = new Record()
                .setImei(device.getImei())
                .setVisiteTime(minuteBeforeZeroDateTime)
                .setIsDeleted(NO_DELETED);
        List<Record> recordList = recordService.selectAfterTheDateTime(recordEntity);
        if (recordList.size() == 0) {
            log.debug("device {} 本分钟 {} 没有任何访问记录!", device.getAlias(), minuteBeforeZeroDateTime);
        } else {
            recordList.forEach(record -> {
                String visiteTime = record.getVisiteTime().substring(0, 19);
                record.setVisiteTime(visiteTime);
                // 若record时间大于前一分钟且小于当前分钟，访问次数+1
                if (visiteTime.compareTo(minuteBeforeZeroDateTime) > 0
                        && TimeGenerator.currentDateTime().compareTo(visiteTime) > 0) {
                    currentMinuteCount.getAndIncrement();
                    // 出入类型
                    if (record.getVisiteStatus() == 0) {
                        currentMinuteInCount.getAndIncrement();
                    } else if (record.getVisiteStatus() == 1) {
                        currentMinuteOutCount.getAndIncrement();
                    } else {
                        log.info("记录访问失败！{}", record);
                    }
                }
            });
        }

        DeviceAccessMinuteHeat accessMinuteHeat = new DeviceAccessMinuteHeat()
                .setImei(device.getImei())
                .setCurrentMinuteTime(minuteBeforeZeroDateTime)
                .setCurrentMinuteCount(currentMinuteCount.get())
                .setCurrentMinuteInCount(currentMinuteInCount.get())
                .setCurrentMinuteOutCount(currentMinuteOutCount.get())
                .setCreateTime(TimeGenerator.currentDateTime())
                .setUpdateTime(TimeGenerator.currentDateTime())
                .setIsDeleted(NO_DELETED);
        deviceAccessMinuteHeatService.insert(accessMinuteHeat);
    }

}
