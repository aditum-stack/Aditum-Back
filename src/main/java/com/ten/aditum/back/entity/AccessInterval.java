package com.ten.aditum.back.entity;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 访问间隔信息
 */
@Data
@Accessors(chain = true)
public class AccessInterval {
    /**
     * 主键ID Auto
     */
    private Integer id;
    /**
     * 用户ID Unique
     */
    private String personnelId;
    /**
     * 平均滞留时间(从入到出)
     */
    private String meanTimeRetention;
    /**
     * 次数
     */
    private Integer firstAddressCount;
    /**
     * 平均外出时间(从出到入)
     */
    private String meanTimeOut;
    /**
     * 次数
     */
    private Integer secondAddressCount;
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
