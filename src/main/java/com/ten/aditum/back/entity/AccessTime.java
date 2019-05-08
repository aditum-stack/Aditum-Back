package com.ten.aditum.back.entity;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 访问时间信息
 */
@Data
@Accessors(chain = true)
public class AccessTime {
    /**
     * 主键ID Auto
     */
    private Integer id;
    /**
     * 用户ID Unique
     */
    private String personnelId;
    /**
     * 平均最早访问时间
     */
    private String averageEarliestAccessTime;
    /**
     * 次数
     */
    private Integer averageEarliestAccessCount;
    /**
     * 平均最晚访问时间
     */
    private String averageLatestAccessTime;
    /**
     * 次数
     */
    private Integer averageLatestAccessCount;
    /**
     * 平均每天访问频率
     */
    private Integer averageDailyFrequency;
    /**
     * 次数
     */
    private Integer averageDailyFrequencyCount;
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
