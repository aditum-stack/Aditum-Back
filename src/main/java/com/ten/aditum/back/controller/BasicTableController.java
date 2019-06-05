package com.ten.aditum.back.controller;

import com.ten.aditum.back.entity.Device;
import com.ten.aditum.back.entity.Person;
import com.ten.aditum.back.model.AditumCode;
import com.ten.aditum.back.model.ResultModel;
import com.ten.aditum.back.service.BasicTableService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 首页的表格数据接口
 */
@Slf4j
@RestController
@RequestMapping(value = "/basic/table")
public class BasicTableController {

    private final BasicTableService basicTableService;

    @Autowired
    public BasicTableController(BasicTableService basicTableService) {
        this.basicTableService = basicTableService;
    }

    /**
     * 缓存有效时间 12小时
     */
    private static final long VALID_TIME = 1000 * 60 * 60 * 12;

    /**
     * showBasicUserTable缓存
     */
    private ResultModel showBasicUserTableCache;
    private long showBasicUserTableCacheTime = System.currentTimeMillis();
    private static final int MOST_PERSON = 7;

    /**
     * 展示访问量最多的MOST_PERSON个用户，同时返回当前状态
     */
    @RequestMapping(value = "personMost", method = RequestMethod.GET)
    public ResultModel showBasicUserTable(String communityId) {
        if (communityId == null) {
            return new ResultModel(AditumCode.ERROR, "communityId不能为空");
        }
        log.debug("showBasicUserTable [GET] : {}", communityId);

        // 初始化缓存
        if (showBasicUserTableCache == null) {
            showBasicUserTableCache = getBasicUserTable(communityId);
            return showBasicUserTableCache;
        }

        // 缓存过期，更新
        long current = System.currentTimeMillis();
        if (current - showBasicUserTableCacheTime > VALID_TIME) {
            showBasicUserTableCacheTime = current;
            showBasicUserTableCache = getBasicUserTable(communityId);
            return showBasicUserTableCache;
        }

        log.info("showBasicUserTable [GET] [CACHE] SUCCESS {}", showBasicUserTableCache.getData());
        return showBasicUserTableCache;
    }

    private ResultModel getBasicUserTable(String communityId) {
        List<Person> personList = basicTableService.mostAccessPersonList(communityId, MOST_PERSON);
        if (personList.size() < 1) {
            log.warn("showBasicUserTable [GET] FAILURE : {} -> {}", communityId);
            return new ResultModel(AditumCode.ERROR);
        }
        log.info("showBasicUserTable [GET] SUCCESS : {} -> {}", communityId, personList);
        return new ResultModel(AditumCode.OK, personList);
    }

    /**
     * showBasicDeviceRepair缓存
     */
    private ResultModel showBasicDeviceRepairCache;
    private long showBasicDeviceRepairCacheTime = System.currentTimeMillis();
    private static final int MOST_DEVICE_REPAIR = 8;

    /**
     * 待维修设备，使用时间最常的MOST_DEVICE_REPAIR台设备
     */
    @RequestMapping(value = "deviceRepair", method = RequestMethod.GET)
    public ResultModel showBasicDeviceRepair(String communityId) {
        if (communityId == null) {
            return new ResultModel(AditumCode.ERROR, "communityId不能为空");
        }
        log.debug("showBasicDeviceRepair [GET] : {}", communityId);

        // 初始化缓存
        if (showBasicDeviceRepairCache == null) {
            showBasicDeviceRepairCache = getBasicDeviceRepair(communityId);
            return showBasicDeviceRepairCache;
        }

        // 缓存过期，更新
        long current = System.currentTimeMillis();
        if (current - showBasicDeviceRepairCacheTime > VALID_TIME) {
            showBasicDeviceRepairCacheTime = current;
            showBasicDeviceRepairCache = getBasicDeviceRepair(communityId);
            return showBasicDeviceRepairCache;
        }

        log.info("showBasicDeviceRepair [GET] [CACHE] SUCCESS {}", showBasicDeviceRepairCache.getData());
        return showBasicDeviceRepairCache;
    }

    private ResultModel getBasicDeviceRepair(String communityId) {
        List<Device> deviceList = basicTableService.mostRepairDeviceList(communityId, MOST_DEVICE_REPAIR);
        if (deviceList.size() < 1) {
            log.warn("showBasicDeviceRepair [GET] FAILURE : {} -> {}", communityId);
            return new ResultModel(AditumCode.ERROR);
        }
        log.info("showBasicDeviceRepair [GET] SUCCESS : {} -> {}", communityId, deviceList);
        return new ResultModel(AditumCode.OK, deviceList);
    }

}
