package com.ten.aditum.back.statistic.device;


import com.ten.aditum.back.statistic.BaseAnalysor;
import com.ten.aditum.back.entity.Device;
import com.ten.aditum.back.entity.DeviceAccessCount;
import com.ten.aditum.back.entity.Record;
import com.ten.aditum.back.util.TimeGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.ten.aditum.back.util.TimeGenerator.formatDate;
import static com.ten.aditum.back.util.TimeGenerator.isYesterday;

@Slf4j
@Component
@EnableScheduling
@EnableAutoConfiguration
public class DeviceCountAnalyzer extends BaseAnalysor {

//    @Scheduled(cron = TEST_TIME)

    /**
     * 每天3点20分更新设备按天访问日志
     */
    @Scheduled(cron = "0 20 3 1/1 * ?")
    public void analysis() {
        log.info("开始更新设备按天访问日志...");
        List<Device> deviceList = selectAllDevice();
        deviceList.forEach(this::analysisDevice);
        log.info("设备按天访问日志更新完成...");
    }

    /**
     * 更新昨天T+1的设备访问日志
     */
    private void analysisDevice(Device device) {
        // 昨天00:00:00时刻
        String yesterdayDateTime = TimeGenerator.yesterdayDateTime();
        String yesterdayDate = yesterdayDateTime.substring(0, 11);
        String yesterdayZeroDateTime = yesterdayDate + "00:00:00";
        Record recordEntity = new Record()
                .setImei(device.getImei())
                .setVisiteTime(yesterdayZeroDateTime)
                .setIsDeleted(NO_DELETED);
        List<Record> collect = recordService.selectAfterTheDateTime(recordEntity);
        if (collect.size() == 0) {
            log.warn("device {} 昨天 {} 没有任何访问记录!", device.getAlias(), yesterdayDateTime);
            return;
        }
        // 过滤，只剩下昨天的数据
        List<Record> recordList = collect.stream()
                .filter(record -> isYesterday(record.getVisiteTime()))
                .collect(Collectors.toList());

        // 按天访问的record集合，一般将会包含昨天和今天两天的信息
        Map<String, Integer> recordDayMap = new HashMap<>(4);

        recordList.forEach(record -> {
            String visiteTime = record.getVisiteTime().substring(0, 19);
            record.setVisiteTime(visiteTime);
            // 年月日
            String formatDate = formatDate(visiteTime);
            // 若已经包含日期，访问次数+1
            if (recordDayMap.containsKey(formatDate)) {
                Integer integer = recordDayMap.get(formatDate);
                recordDayMap.put(formatDate, integer + 1);
            }
            // 若未包含日期，初始化访问次数
            else {
                recordDayMap.put(formatDate, 1);
            }
        });

        for (Map.Entry<String, Integer> accessCount : recordDayMap.entrySet()) {
            // 查询原始数据
            DeviceAccessCount accessCountSelectEntity = new DeviceAccessCount()
                    .setImei(device.getImei())
                    .setLogDate(accessCount.getKey())
                    .setIsDeleted(NO_DELETED);
            List<DeviceAccessCount> select = deviceAccessCountService.select(accessCountSelectEntity);

            DeviceAccessCount accessCountEntity = new DeviceAccessCount()
                    .setImei(device.getImei())
                    .setLogDate(accessCount.getKey())
                    .setAccessCount(accessCount.getValue())
                    .setCreateTime(TimeGenerator.currentTime())
                    .setUpdateTime(TimeGenerator.currentTime())
                    .setIsDeleted(NO_DELETED);
            // 原始数据已经存在，更新
            if (select.size() > 0) {
                accessCountEntity.setId(select.get(0).getId());
                deviceAccessCountService.update(accessCountEntity);
            } else {
                deviceAccessCountService.insert(accessCountEntity);
            }
            log.info("device完成更新 date:{}, count:{}", accessCount.getKey(), accessCount.getValue());
        }
    }

}
