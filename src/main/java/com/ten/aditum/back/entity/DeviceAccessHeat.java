package com.ten.aditum.back.entity;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 设备访问按小时的实时记录(热度信息)
 */
@Data
@Accessors(chain = true)
public class DeviceAccessHeat {
    /**
     * 主键ID Auto
     */
    private Integer id;
    /**
     * 设备ID Unique
     */
    private String imei;
    /**
     * 按小时计算
     */
    private String currentHourTime;
    /**
     * 访问次数
     */
    private Integer currentHourCount;
    /**
     * 进门访问次数
     */
    private Integer currentHourInCount;
    /**
     * 出门访问次数
     */
    private Integer currentHourOutCount;
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
