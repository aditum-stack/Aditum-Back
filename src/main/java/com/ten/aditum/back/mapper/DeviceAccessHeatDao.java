package com.ten.aditum.back.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.List;

import com.ten.aditum.back.entity.DeviceAccessHeat;

public interface DeviceAccessHeatDao {

    int insert(@Param("pojo") DeviceAccessHeat pojo);

    int insertList(@Param("pojos") List<DeviceAccessHeat> pojo);

    List<DeviceAccessHeat> select(@Param("pojo") DeviceAccessHeat pojo);

    int update(@Param("pojo") DeviceAccessHeat pojo);

}
