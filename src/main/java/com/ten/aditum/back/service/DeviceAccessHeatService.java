package com.ten.aditum.back.service;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

import com.ten.aditum.back.entity.DeviceAccessHeat;
import com.ten.aditum.back.mapper.DeviceAccessHeatDao;

@Service
public class DeviceAccessHeatService {

    @Resource
    private DeviceAccessHeatDao deviceAccessHeatDao;

    public int insert(DeviceAccessHeat pojo) {
        return deviceAccessHeatDao.insert(pojo);
    }

    public int insertList(List<DeviceAccessHeat> pojos) {
        return deviceAccessHeatDao.insertList(pojos);
    }

    public List<DeviceAccessHeat> select(DeviceAccessHeat pojo) {
        return deviceAccessHeatDao.select(pojo);
    }

    public int update(DeviceAccessHeat pojo) {
        return deviceAccessHeatDao.update(pojo);
    }

}
