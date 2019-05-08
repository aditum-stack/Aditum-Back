package com.ten.aditum.back.entity;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 设备访问实时记录
 */
@Data
@Accessors(chain = true)
public class DeviceAccessLog {
    /**
     * 主键ID Auto
     */
    private Integer id;
    /**
     * 设备ID Unique
     */
    private String imei;
    /**
     * 日志ID Unique
     */
    private String recordId;
    /**
     * 访问时间
     */
    private String accessTime;
    /**
     * 访问类型
     */
    private String accessType;
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
