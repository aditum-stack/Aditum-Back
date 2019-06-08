package com.ten.aditum.back.mapper;

import org.apache.ibatis.annotations.Param;
import java.util.List;
import com.ten.aditum.back.entity.DeviceAccessMinuteHeat;

public interface DeviceAccessMinuteHeatDao {

    int insert(@Param("pojo") DeviceAccessMinuteHeat pojo);

    int insertList(@Param("pojos") List< DeviceAccessMinuteHeat> pojo);

    List<DeviceAccessMinuteHeat> select(@Param("pojo") DeviceAccessMinuteHeat pojo);

    int update(@Param("pojo") DeviceAccessMinuteHeat pojo);

    List<DeviceAccessMinuteHeat> selectOneHourHeat(@Param("pojo") DeviceAccessMinuteHeat pojo);
}
