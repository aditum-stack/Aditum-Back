package com.ten.aditum.back.controller;

import com.alibaba.fastjson.JSON;
import com.ten.aditum.back.entity.Device;
import com.ten.aditum.back.entity.Person;
import com.ten.aditum.back.model.AditumCode;
import com.ten.aditum.back.model.ResultModel;
import com.ten.aditum.back.service.BasicTableService;
import com.ten.aditum.back.vo.BasicLabelData;
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
public class BasicTableController extends BaseController {

    private final BasicTableService basicTableService;

    @Autowired
    public BasicTableController(BasicTableService basicTableService) {
        this.basicTableService = basicTableService;
    }

    private static final int MOST_PERSON = 7;

    /**
     * 展示访问量最多的MOST_PERSON个用户，同时返回当前状态
     */
    @RequestMapping(value = "personMost", method = RequestMethod.GET)
    public ResultModel showBasicUserTable(String communityId) {
        if (communityId == null) {
            return new ResultModel(AditumCode.ERROR, "communityId不能为空");
        }
        log.info("showBasicUserTable [GET] : {}", communityId);

        String key = "showBasicUserTable" + communityId;
        String originValue = jedis.get(key);
        if (originValue == null) {
            ResultModel resultModel = getBasicUserTable(communityId);
            if (resultModel.getCode() == AditumCode.ERROR.getCode()) {
                log.warn("showBasicUserTable [GET] [INIT] FAILURE : {} -> {}", communityId);
                return resultModel;
            } else {
                String value = JSON.toJSONString(resultModel);
                jedis.setex(key, VALID_TIME, value);
                log.info("showBasicUserTable [GET] [INIT] SUCCESS {}", resultModel);
                return resultModel;
            }
        } else {
            ResultModel cache = JSON.parseObject(originValue, ResultModel.class);
            log.info("showBasicUserTable [GET] [CACHE] SUCCESS {}", cache);
            return cache;
        }
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

    private static final int MOST_DEVICE_REPAIR = 8;

    /**
     * 待维修设备，使用时间最常的MOST_DEVICE_REPAIR台设备
     */
    @RequestMapping(value = "deviceRepair", method = RequestMethod.GET)
    public ResultModel showBasicDeviceRepair(String communityId) {
        if (communityId == null) {
            return new ResultModel(AditumCode.ERROR, "communityId不能为空");
        }
        log.info("showBasicDeviceRepair [GET] : {}", communityId);

        String key = "showBasicDeviceRepair" + communityId;
        String originValue = jedis.get(key);
        if (originValue == null) {
            ResultModel resultModel = getBasicDeviceRepair(communityId);
            if (resultModel.getCode() == AditumCode.ERROR.getCode()) {
                log.warn("showBasicDeviceRepair [GET] [INIT] FAILURE : {} -> {}", communityId);
                return resultModel;
            } else {
                String value = JSON.toJSONString(resultModel);
                jedis.setex(key, VALID_TIME, value);
                log.info("showBasicDeviceRepair [GET] [INIT] SUCCESS {}", resultModel);
                return resultModel;
            }
        } else {
            ResultModel cache = JSON.parseObject(originValue, ResultModel.class);
            log.info("showBasicDeviceRepair [GET] [CACHE] SUCCESS {}", cache);
            return cache;
        }
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
