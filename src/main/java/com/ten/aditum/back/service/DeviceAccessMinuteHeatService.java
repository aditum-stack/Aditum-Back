package com.ten.aditum.back.service;

import com.ten.aditum.back.entity.DeviceAccessHeat;
import com.ten.aditum.back.entity.DeviceAccessMinuteHeat;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

import com.ten.aditum.back.mapper.DeviceAccessMinuteHeatDao;

@Service
public class DeviceAccessMinuteHeatService {

    @Resource
    private DeviceAccessMinuteHeatDao deviceAccessMinuteHeatDao;

    public int insert(DeviceAccessMinuteHeat pojo) {
        return deviceAccessMinuteHeatDao.insert(pojo);
    }

    public int insertList(List<DeviceAccessMinuteHeat> pojos) {
        return deviceAccessMinuteHeatDao.insertList(pojos);
    }

    public List<DeviceAccessMinuteHeat> select(DeviceAccessMinuteHeat pojo) {
        return deviceAccessMinuteHeatDao.select(pojo);
    }

    public int update(DeviceAccessMinuteHeat pojo) {
        return deviceAccessMinuteHeatDao.update(pojo);
    }

    /**
     * 获取最近六十条(一个小时)数据
     */
    public List<DeviceAccessMinuteHeat> selectOneHourHeat(DeviceAccessMinuteHeat pojo) {
        return deviceAccessMinuteHeatDao.selectOneHourHeat(pojo);
    }
}
