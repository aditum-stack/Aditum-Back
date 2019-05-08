package com.ten.aditum.back.entity;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 访问地点信息
 */
@Data
@Accessors(chain = true)
public class AccessAddress {
    /**
     * 主键ID Auto
     */
    private Integer id;
    /**
     * 用户ID Unique
     */
    private String personnelId;
    /**
     * 最常访问地址
     */
    private String firstAddress;
    /**
     * 次数
     */
    private Integer firstAddressCount;
    /**
     * 第二访问地址
     */
    private String secondAddress;
    /**
     * 次数
     */
    private Integer secondAddressCount;
    /**
     * 累计访问地点
     */
    private String totalAddress;
    /**
     * 累计访问地点数量
     */
    private Integer totalAddressCount;
    /**
     * 累计访问次数
     */
    private Integer totalCount;
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
