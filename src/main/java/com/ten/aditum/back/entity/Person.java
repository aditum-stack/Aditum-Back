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

    public Person() {
    }

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", personnelId='" + personnelId + '\'' +
                ", personnelName='" + personnelName + '\'' +
                ", communityId='" + communityId + '\'' +
                ", personnelAddress='" + personnelAddress + '\'' +
                ", personnelPhone='" + personnelPhone + '\'' +
                ", createTime='" + createTime + '\'' +
                ", updateTime='" + updateTime + '\'' +
                ", isDeleted=" + isDeleted +
                '}';
    }

    public Person id(Integer id) {
        this.id = id;
        return this;
    }

    public Person personnelId(String personnelId) {
        this.personnelId = personnelId;
        return this;
    }

    public Person personnelName(String personnelName) {
        this.personnelName = personnelName;
        return this;
    }

    public Person communityId(String communityId) {
        this.communityId = communityId;
        return this;
    }

    public Person personnelAddress(String personnelAddress) {
        this.personnelAddress = personnelAddress;
        return this;
    }

    public Person personnelPhone(String personnelPhone) {
        this.personnelPhone = personnelPhone;
        return this;
    }

    public Person createTime(String createTime) {
        this.createTime = createTime;
        return this;
    }

    public Person updateTime(String updateTime) {
        this.updateTime = updateTime;
        return this;
    }

    public Person isDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
        return this;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPersonnelId() {
        return personnelId;
    }

    public void setPersonnelId(String personnelId) {
        this.personnelId = personnelId;
    }

    public String getPersonnelName() {
        return personnelName;
    }

    public void setPersonnelName(String personnelName) {
        this.personnelName = personnelName;
    }

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }

    public String getPersonnelAddress() {
        return personnelAddress;
    }

    public void setPersonnelAddress(String personnelAddress) {
        this.personnelAddress = personnelAddress;
    }

    public String getPersonnelPhone() {
        return personnelPhone;
    }

    public void setPersonnelPhone(String personnelPhone) {
        this.personnelPhone = personnelPhone;
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
