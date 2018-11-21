package com.ten.aditum.back.entity;

/**
 * 设备信息表
 */
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

    public Device() {
    }

    @Override
    public String toString() {
        return "Device{" +
                "id=" + id +
                ", imei='" + imei + '\'' +
                ", alias='" + alias + '\'' +
                ", communityId='" + communityId + '\'' +
                ", deviceStatus=" + deviceStatus +
                ", activateTime='" + activateTime + '\'' +
                ", lastOnlineTime='" + lastOnlineTime + '\'' +
                ", lastOfflineTime='" + lastOfflineTime + '\'' +
                ", createTime='" + createTime + '\'' +
                ", updateTime='" + updateTime + '\'' +
                ", isDeleted=" + isDeleted +
                '}';
    }

    public Device id(Integer id) {
        this.id = id;
        return this;
    }

    public Device imei(String imei) {
        this.imei = imei;
        return this;
    }

    public Device alias(String alias) {
        this.alias = alias;
        return this;
    }

    public Device communityId(String communityId) {
        this.communityId = communityId;
        return this;
    }

    public Device deviceStatus(Integer deviceStatus) {
        this.deviceStatus = deviceStatus;
        return this;
    }

    public Device activateTime(String activateTime) {
        this.activateTime = activateTime;
        return this;
    }

    public Device lastOnlineTime(String lastOnlineTime) {
        this.lastOnlineTime = lastOnlineTime;
        return this;
    }

    public Device lastOfflineTime(String lastOfflineTime) {
        this.lastOfflineTime = lastOfflineTime;
        return this;
    }

    public Device createTime(String createTime) {
        this.createTime = createTime;
        return this;
    }

    public Device updateTime(String updateTime) {
        this.updateTime = updateTime;
        return this;
    }

    public Device isDeleted(Integer isDeleted) {
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

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }

    public Integer getDeviceStatus() {
        return deviceStatus;
    }

    public void setDeviceStatus(Integer deviceStatus) {
        this.deviceStatus = deviceStatus;
    }

    public String getActivateTime() {
        return activateTime;
    }

    public void setActivateTime(String activateTime) {
        this.activateTime = activateTime;
    }

    public String getLastOnlineTime() {
        return lastOnlineTime;
    }

    public void setLastOnlineTime(String lastOnlineTime) {
        this.lastOnlineTime = lastOnlineTime;
    }

    public String getLastOfflineTime() {
        return lastOfflineTime;
    }

    public void setLastOfflineTime(String lastOfflineTime) {
        this.lastOfflineTime = lastOfflineTime;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }
}
