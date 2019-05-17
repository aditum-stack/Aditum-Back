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
     * <p>
     * xx 混合分析，标签结合
     * <p>
     * 1 基于早晚访问时间的二维分析
     * 2 基于早或晚时间的一维分析
     * 3 基于访问频次的一维分析
     * 4 基于用户信息的数据分析
     * 5 基于用户访问地理信息的分析
     * 6 基于访问时长的分析
     * 7 基于排名的数据分析
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
