package com.ten.aditum.back.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.List;

import com.ten.aditum.back.entity.Device;

public interface DeviceDao {

    int insert(@Param("pojo") Device pojo);

    int insertList(@Param("pojos") List<Device> pojo);

    List<Device> select(@Param("pojo") Device pojo);

    int update(@Param("pojo") Device pojo);

}
