package com.ten.aditum.back.mapper;

import com.ten.aditum.back.entity.DeviceAccessLog;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DeviceAccessLogDao {

    int insert(@Param("pojo") DeviceAccessLog pojo);

    int insertList(@Param("pojos") List<DeviceAccessLog> pojo);

    List<DeviceAccessLog> select(@Param("pojo") DeviceAccessLog pojo);

    int update(@Param("pojo") DeviceAccessLog pojo);

}
