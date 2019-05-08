package com.ten.aditum.back.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.List;

import com.ten.aditum.back.entity.DeviceAccessTotal;

public interface DeviceAccessTotalDao {

    int insert(@Param("pojo") DeviceAccessTotal pojo);

    int insertList(@Param("pojos") List<DeviceAccessTotal> pojo);

    List<DeviceAccessTotal> select(@Param("pojo") DeviceAccessTotal pojo);

    int update(@Param("pojo") DeviceAccessTotal pojo);

}
