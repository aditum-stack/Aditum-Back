package com.ten.aditum.back.entity;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 设备访问总记录
 */
@Data
@Accessors(chain = true)
public class DeviceAccessTotal {
    /**
     * 主键ID Auto
     */
    private Integer id;
    /**
     * 设备ID Unique
     */
    private String imei;
    /**
     * 总访问次数
     */
    private Integer totalAccessCount;
    /**
     * 使用天数
     */
    private Integer totalDayCount;
    /**
     * 信息创建时间
     */
    private String createTime;
    /**
     * 信息修改时间
     */
    private String updateTime;
    /**
     * 删除标记
     */
    private Integer isDeleted;
}
