package com.ten.aditum.back.statistic.device;


import com.ten.aditum.back.BaseAnalysor;
import com.ten.aditum.back.entity.Device;
import com.ten.aditum.back.entity.DeviceAccessTotal;
import com.ten.aditum.back.entity.Record;
import com.ten.aditum.back.util.TimeGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static com.ten.aditum.back.util.TimeGenerator.formatDate;


@Slf4j
@Component
@EnableScheduling
@EnableAutoConfiguration
public class DeviceTotalAnalyzer extends BaseAnalysor {

//    @Scheduled(cron = TEST_TIME)

    /**
     * 每天3点30分更新设备总访问日志
     */
    @Scheduled(cron = "0 30 3 1/1 * ?")
    public void analysis() {
        log.info("开始更新设备总访问日志...");
        List<Device> deviceList = selectAllDevice();
        deviceList.forEach(this::analysisDevice);
        log.info("设备总访问日志更新完成...");
    }

    /**
     * 分析设备总访问记录
     */
    private void analysisDevice(Device device) {
        DeviceAccessTotal total = new DeviceAccessTotal();
        total.setTotalAccessCount(0);
        total.setImei(device.getImei());
        List<String> dayList = new ArrayList<>();

        // 每次取出1000条，遍历循环直到全部取出
        for (int i = 0; ; ) {
            Record recordEntity = new Record()
                    .setId(i)
                    .setImei(device.getImei())
                    .setIsDeleted(NO_DELETED);
            List<Record> recordList = recordService.selectAfterTheId(recordEntity);
            if (recordList.size() == 0) {
                break;
            }
            analysisDeviceRecord(total, recordList, dayList);
            // 若数量为SELECT_SIZE，说明后面可能还有未取出的数据
            if (recordList.size() >= SELECT_SIZE) {
                i = recordList.get(SELECT_SIZE - 1).getId();
            }
            // 如数量不足SELECT_SIZE，说明后面已经没有数据了
            else {
                break;
            }
        }

        Integer totalDayCount = dayList.size();
        Integer totalAccessCount = total.getTotalAccessCount();
        DeviceAccessTotal accessTotal = new DeviceAccessTotal()
                .setImei(device.getImei())
                .setTotalAccessCount(totalAccessCount)
                .setTotalDayCount(totalDayCount)
                .setIsDeleted(NO_DELETED);

        DeviceAccessTotal selectEntity = new DeviceAccessTotal()
                .setImei(device.getImei())
                .setIsDeleted(NO_DELETED);
        List<DeviceAccessTotal> select = deviceAccessTotalService.select(selectEntity);
        // 已有记录
        if (select.size() > 0) {
            accessTotal
                    .setId(select.get(0).getId())
                    .setUpdateTime(TimeGenerator.currentTime());
            deviceAccessTotalService.update(accessTotal);
        } else {
            accessTotal.setCreateTime(TimeGenerator.currentTime());
            deviceAccessTotalService.insert(accessTotal);
        }
        log.info("Device {} 完成更新 总访问次数:{}, 使用天数:{}",
                device.getAlias(), totalAccessCount, totalDayCount);
    }

    /**
     * 分析Record记录中的访问量
     */
    private void analysisDeviceRecord(final DeviceAccessTotal device, List<Record> recordList, List<String> dayList) {
        AtomicInteger totalDayCount = new AtomicInteger(device.getTotalAccessCount());
        recordList.forEach(record -> {
            String visiteTime = record.getVisiteTime().substring(0, 19);
            record.setVisiteTime(visiteTime);
            String formatDate = formatDate(visiteTime);
            // 若未包含此日期，总天数+1
            if (!dayList.contains(formatDate)) {
                dayList.add(formatDate);
            }
            totalDayCount.getAndIncrement();
        });
        device.setTotalAccessCount(totalDayCount.get());
    }

}
