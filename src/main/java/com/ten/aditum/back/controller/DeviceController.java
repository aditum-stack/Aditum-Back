package com.ten.aditum.back.controller;

import com.ten.aditum.back.entity.Community;
import com.ten.aditum.back.entity.Device;
import com.ten.aditum.back.model.AditumCode;
import com.ten.aditum.back.model.ResultModel;
import com.ten.aditum.back.service.CommunityService;
import com.ten.aditum.back.service.DeviceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/device")
public class DeviceController extends BaseController<Device> {
    private static final Logger logger = LoggerFactory.getLogger(DeviceController.class);

    private final DeviceService deviceService;
    private final CommunityService communityService;

    @Autowired
    public DeviceController(DeviceService deviceService, CommunityService communityService) {
        this.deviceService = deviceService;
        this.communityService = communityService;
    }

    @Override
    @RequestMapping(method = RequestMethod.GET)
    public ResultModel get(Device device) {
        List<Device> deviceList = deviceService.select(device);
        if (deviceList == null) {
            return new ResultModel(AditumCode.ERROR);
        }
        return new ResultModel(AditumCode.OK, deviceList);
    }

    @Override
    @RequestMapping(method = RequestMethod.POST)
    public ResultModel post(Device device) {
        Device entity = new Device()
                .imei(super.uidGenerator.generateUid())
                .alias(device.getAlias())
                .communityId(device.getCommunityId())
                .deviceStatus(0)
                .createTime(super.timeGenerator.currentTime())
                .updateTime(super.timeGenerator.currentTime())
                .isDeleted(NO_DELETED);

        Integer result = deviceService.insert(entity);
        if (result < 1) {
            return new ResultModel(AditumCode.ERROR);
        }
        return new ResultModel(AditumCode.OK);
    }

    @Override
    public ResultModel update(Device device) {
        return null;
    }

    @Override
    public ResultModel delete(Device device) {
        return null;
    }
}
