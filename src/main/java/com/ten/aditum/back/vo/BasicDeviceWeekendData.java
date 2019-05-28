package com.ten.aditum.back.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 展示最近一周，设备的访问量（访问量最高的X个设备）
 */
@Data
@Accessors(chain = true)
public class BasicDeviceWeekendData {

    /**
     * X台设备的一周访问量
     */
    private List<WeekendDeviceCount> weekendDeviceCountList;

    @Data
    public static class WeekendDeviceCount {
        private String deviceName;
        /**
         * 一周访问量
         */
        private List<Integer> weekendAccessCount;
    }

}
