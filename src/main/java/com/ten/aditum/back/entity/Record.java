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

    public Record() {
    }

    @Override
    public String toString() {
        return "Record{" +
                "id=" + id +
                ", imei='" + imei + '\'' +
                ", personnelId='" + personnelId + '\'' +
                ", visiteTime='" + visiteTime + '\'' +
                ", visiteStatus=" + visiteStatus +
                ", isDeleted=" + isDeleted +
                '}';
    }

    public Record id(Integer id) {
        this.id = id;
        return this;
    }

    public Record imei(String imei) {
        this.imei = imei;
        return this;
    }

    public Record personnelId(String personnelId) {
        this.personnelId = personnelId;
        return this;
    }

    public Record visiteTime(String visiteTime) {
        this.visiteTime = visiteTime;
        return this;
    }

    public Record visiteStatus(Integer visiteStatus) {
        this.visiteStatus = visiteStatus;
        return this;
    }

    public Record isDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
        return this;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getPersonnelId() {
        return personnelId;
    }

    public void setPersonnelId(String personnelId) {
        this.personnelId = personnelId;
    }

    public String getVisiteTime() {
        return visiteTime;
    }

    public void setVisiteTime(String visiteTime) {
        this.visiteTime = visiteTime;
    }

    public Integer getVisiteStatus() {
        return visiteStatus;
    }

    public void setVisiteStatus(Integer visiteStatus) {
        this.visiteStatus = visiteStatus;
    }

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }
}
