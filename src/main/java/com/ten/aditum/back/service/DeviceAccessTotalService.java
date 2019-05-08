package com.ten.aditum.back.service;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

import com.ten.aditum.back.entity.DeviceAccessTotal;
import com.ten.aditum.back.mapper.DeviceAccessTotalDao;

@Service
public class DeviceAccessTotalService {

    @Resource
    private DeviceAccessTotalDao deviceAccessTotalDao;

    public int insert(DeviceAccessTotal pojo) {
        return deviceAccessTotalDao.insert(pojo);
    }

    public int insertList(List<DeviceAccessTotal> pojos) {
        return deviceAccessTotalDao.insertList(pojos);
    }

    public List<DeviceAccessTotal> select(DeviceAccessTotal pojo) {
        return deviceAccessTotalDao.select(pojo);
    }

    public int update(DeviceAccessTotal pojo) {
        return deviceAccessTotalDao.update(pojo);
    }

}
