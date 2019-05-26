package com.ten.aditum.back.statistic.device;


import com.ten.aditum.back.BaseAnalysor;
import com.ten.aditum.back.entity.Device;
import com.ten.aditum.back.entity.DeviceAccessCount;
import com.ten.aditum.back.entity.Record;
import com.ten.aditum.back.service.DeviceAccessCountService;
import com.ten.aditum.back.service.DeviceService;
import com.ten.aditum.back.service.RecordService;
import com.ten.aditum.back.util.TimeGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ten.aditum.back.util.TimeGenerator.formatDate;

@Slf4j
@Component
@EnableScheduling
@EnableAutoConfiguration
public class DeviceCountAnalyzer  extends BaseAnalysor {

    /**
     * 每天3点20分更新设备按天访问日志
     */
//    @Scheduled(cron = TEST_TIME)
    @Scheduled(cron = "0 20 3 1/1 * ?")
    public void analysis() {
        log.info("开始更新设备访问日志...");

        Device deviceEntity = new Device()
                .setIsDeleted(NO_DELETED);
        List<Device> deviceList = deviceService.select(deviceEntity);

        deviceList.forEach(this::analysisDevice);

        log.info("设备按天访问日志更新完成...");
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

        // 按天访问的record集合
        Map<String, Integer> recordDayMap = new HashMap<>();

        recordList.forEach(record -> {
            log.info("开始分析此record : {}", record);

            String visiteTime = record.getVisiteTime().substring(0, 19);
            record.setVisiteTime(visiteTime);
            String formatDate = formatDate(visiteTime);

            // 若已经包含此日期，访问次数+1
            if (recordDayMap.containsKey(formatDate)) {
                Integer integer = recordDayMap.get(formatDate);
                recordDayMap.put(formatDate, integer + 1);
            }
            // 若未包含此日期，初始化访问次数
            else {
                recordDayMap.put(formatDate, 1);
            }
        });

        for (Map.Entry<String, Integer> accessCount : recordDayMap.entrySet()) {
            DeviceAccessCount accessCountEntity = new DeviceAccessCount()
                    .setImei(device.getImei())
                    .setLogDate(accessCount.getKey())
                    .setAccessCount(accessCount.getValue())
                    .setCreateTime(TimeGenerator.currentTime())
                    .setUpdateTime(TimeGenerator.currentTime())
                    .setIsDeleted(NO_DELETED);

            DeviceAccessCount accessCountSelectEntity = new DeviceAccessCount()
                    .setImei(device.getImei())
                    .setLogDate(accessCount.getKey())
                    .setIsDeleted(NO_DELETED);

            List<DeviceAccessCount> select = deviceAccessCountService.select(accessCountSelectEntity);
            // 此日子已经存在，更新
            if (select.size() > 0) {
                accessCountEntity.setId(select.get(0).getId());
                deviceAccessCountService.update(accessCountEntity);
            } else {
                deviceAccessCountService.insert(accessCountEntity);
            }

            log.info("此device完成更新 date:{}, count:{}", accessCount.getKey(), accessCount.getValue());
        }
    }

}
