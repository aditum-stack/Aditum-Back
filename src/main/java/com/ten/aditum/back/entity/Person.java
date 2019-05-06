package com.ten.aditum.back.entity;

/**
 * 用户信息表
 */
public class Person {
    /**
     * 主键ID Auto
     */
    private Integer id;
    /**
     * 用户ID Unique
     */
    private String personnelId;
    /**
     * 用户姓名
     */
    private String personnelName;
    /**
     * 社区ID
     */
    private String communityId;
    /**
     * 用户社区住址
     */
    private String personnelAddress;
    /**
     * 用户手机号
     */
    private String personnelPhone;
    /**
     * 用户信息创建时间
     */
    private String createTime;
    /**
     * 用户信息修改时间
     */
    private String updateTime;
    /**
     * 删除标记
     */
    private Integer isDeleted;
}
