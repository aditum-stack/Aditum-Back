package com.ten.aditum.back.entity;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 设备访问按分钟的实时记录(热度信息)
 */
@Data
@Accessors(chain = true)
public class DeviceAccessMinuteHeat {
    /**
     * 主键ID Auto
     */
    private Integer id;
    /**
     * 设备ID Unique
     */
    private String imei;
    /**
     * 按分钟计算
     */
    private String currentMinuteTime;
    /**
     * 访问次数
     */
    private Integer currentMinuteCount;
    /**
     * 进门访问次数
     */
    private Integer currentMinuteInCount;
    /**
     * 出门访问次数
     */
    private Integer currentMinuteOutCount;
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
