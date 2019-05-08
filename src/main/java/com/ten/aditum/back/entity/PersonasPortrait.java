package com.ten.aditum.back.entity;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 用户画像存储
 */
@Data
@Accessors(chain = true)
public class PersonasPortrait {
    /**
     * 主键ID Auto
     */
    private Integer id;
    /**
     * 用户ID Unique
     */
    private String personnelId;
    /**
     * 用户标签集合
     */
    private String personasExt;
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
