package com.ten.aditum.back.service;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

import com.ten.aditum.back.entity.DeviceAccessCount;
import com.ten.aditum.back.mapper.DeviceAccessCountDao;

@Service
public class DeviceAccessCountService {

    @Resource
    private DeviceAccessCountDao deviceAccessCountDao;

    public int insert(DeviceAccessCount pojo) {
        return deviceAccessCountDao.insert(pojo);
    }

    public int insertList(List<DeviceAccessCount> pojos) {
        return deviceAccessCountDao.insertList(pojos);
    }

    public List<DeviceAccessCount> select(DeviceAccessCount pojo) {
        return deviceAccessCountDao.select(pojo);
    }

    public int update(DeviceAccessCount pojo) {
        return deviceAccessCountDao.update(pojo);
    }

}
