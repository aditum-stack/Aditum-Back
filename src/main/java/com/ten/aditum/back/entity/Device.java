package com.ten.aditum.back.entity;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 设备信息表
 */
@Data
@Accessors(chain = true)
public class Device {
    /**
     * 主键ID Auto
     */
    private Integer id;
    /**
     * 设备ID Unique
     */
    private String imei;
    /**
     * 设备别名
     */
    private String alias;
    /**
     * 社区ID
     */
    private String communityId;
    /**
     * 设备状态 0未激活 1已激活(但未使用) 2在线(可使用状态) 3离线
     */
    private Integer deviceStatus;
    /**
     * 设备激活时间
     */
    private String activateTime;
    /**
     * 设备最后在线时间
     */
    private String lastOnlineTime;
    /**
     * 设备最后离线时间
     */
    private String lastOfflineTime;
    /**
     * 创建时间
     */
    private String createTime;
    /**
     * 更新时间
     */
    private String updateTime;
    /**
     * 删除标记
     */
    private Integer isDeleted;
}
