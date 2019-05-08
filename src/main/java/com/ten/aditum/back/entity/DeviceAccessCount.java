package com.ten.aditum.back.entity;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 设备访问按天的记录
 */
@Data
@Accessors(chain = true)
public class DeviceAccessCount {
    /**
     * 主键ID Auto
     */
    private Integer id;
    /**
     * 设备ID Unique
     */
    private String imei;
    /**
     * 日期
     */
    private String logDate;
    /**
     * 访问次数
     */
    private Integer accessCount;
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
