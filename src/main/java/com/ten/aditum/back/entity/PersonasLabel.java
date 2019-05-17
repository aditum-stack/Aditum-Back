package com.ten.aditum.back.entity;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 用户画像标签
 */
@Data
@Accessors(chain = true)
public class PersonasLabel {
    /**
     * 主键ID Auto
     */
    private Integer id;
    /**
     * 标签ID Unique
     */
    private String labelId;
    /**
     * 标签名称
     */
    private String labelName;
    /**
     * 标签类别
     */
    private Integer labelType;
    /**
     * 标签描述
     */
    private String labelDesc;
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
