package com.ten.aditum.back.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.List;

import com.ten.aditum.back.entity.DeviceAccessCount;

public interface DeviceAccessCountDao {

    int insert(@Param("pojo") DeviceAccessCount pojo);

    int insertList(@Param("pojos") List<DeviceAccessCount> pojo);

    List<DeviceAccessCount> select(@Param("pojo") DeviceAccessCount pojo);

    int update(@Param("pojo") DeviceAccessCount pojo);

}
