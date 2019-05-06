package com.ten.aditum.back.entity;

/**
 * 访问记录表
 */
public class Record {
    /**
     * 主键ID Auto
     */
    private Integer id;
    /**
     * 设备ID
     */
    private String imei;
    /**
     * 访客ID
     */
    private String personnelId;
    /**
     * 访问时间
     */
    private String visiteTime;
    /**
     * 访问状态 0进入社区 1离开社区 2识别失败 3响应超时
     */
    private Integer visiteStatus;
    /**
     * 删除标记
     */
    private Integer isDeleted;
}
