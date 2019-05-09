package com.ten.aditum.back.schedule;


import com.ten.aditum.back.entity.Device;
import com.ten.aditum.back.entity.DeviceAccessCount;
import com.ten.aditum.back.entity.DeviceAccessTotal;
import com.ten.aditum.back.entity.Record;
import com.ten.aditum.back.service.DeviceAccessTotalService;
import com.ten.aditum.back.service.DeviceService;
import com.ten.aditum.back.service.RecordService;
import com.ten.aditum.back.util.TimeGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Component
@EnableScheduling
@EnableAutoConfiguration
public class DeviceTotalAnalyzer implements Analyzer {

    private final DeviceService deviceService;
    private final RecordService recordService;
    private final DeviceAccessTotalService deviceAccessTotalService;

    @Autowired
    public DeviceTotalAnalyzer(RecordService recordService,
                               DeviceAccessTotalService deviceAccessTotalService,
                               DeviceService deviceService) {
        this.recordService = recordService;
        this.deviceAccessTotalService = deviceAccessTotalService;
        this.deviceService = deviceService;
    }

    /**
     * 每天3点30分更新设备总访问日志
     */
//    @Scheduled(cron = TEST_TIME)
    @Scheduled(cron = "0 30 3 1/1 * ?")
    public void analysis() {
        log.info("开始更新设备总访问日志...");

        Device deviceEntity = new Device()
                .setIsDeleted(NO_DELETED);
        List<Device> deviceList = deviceService.select(deviceEntity);

        log.info("查询所有device集合 : {}", deviceList);

        deviceList.forEach(this::analysisDevice);

        log.info("设备总访问日志更新完成...");
    }

    private void analysisDevice(Device device) {
        Record recordEntity = new Record()
                .setImei(device.getImei())
                .setIsDeleted(NO_DELETED);
        List<Record> recordList = recordService.select(recordEntity);

        if (recordList.size() == 0) {
            log.warn("此device {} 没有任何访问记录!", device.getAlias());
            return;
        }

        // 访问的record集合
        List<String> dayList = new ArrayList<>();

        // 总访问天数
        AtomicInteger totalDayCount = new AtomicInteger();

        // 总访问次数
        int totalAccessCount = recordList.size();

        recordList.forEach(record -> {
            log.info("开始分析此record : {}", record);

            String visiteTime = record.getVisiteTime().substring(0, 19);
            record.setVisiteTime(visiteTime);
            String formatDate = formatDate(visiteTime);

            // 若未包含此日期，总天数+1
            if (!dayList.contains(formatDate)) {
                dayList.add(formatDate);
                totalDayCount.getAndIncrement();
            }
        });

        DeviceAccessTotal accessTotal = new DeviceAccessTotal()
                .setImei(device.getImei())
                .setTotalAccessCount(totalAccessCount)
                .setTotalDayCount(totalDayCount.get())
                .setIsDeleted(NO_DELETED);

        DeviceAccessTotal accessTotalEntity = new DeviceAccessTotal()
                .setImei(device.getImei())
                .setIsDeleted(NO_DELETED);

        List<DeviceAccessTotal> select = deviceAccessTotalService.select(accessTotalEntity);
        // 已有记录
        if (select.size() > 0) {
            accessTotal
                    .setId(select.get(0).getId())
                    .setUpdateTime(TimeGenerator.currentTime());
            deviceAccessTotalService.update(accessTotal);
        }
        // 未有记录
        else {
            accessTotal
                    .setCreateTime(TimeGenerator.currentTime());
            deviceAccessTotalService.insert(accessTotal);
        }

        log.info("此device完成更新 总访问次数:{}, 使用天数:{}", totalAccessCount, totalDayCount.get());
    }

}
