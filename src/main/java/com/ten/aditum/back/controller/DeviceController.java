package com.ten.aditum.back.controller;

import com.ten.aditum.back.entity.Device;
import com.ten.aditum.back.model.AditumCode;
import com.ten.aditum.back.model.ResultModel;
import com.ten.aditum.back.service.CommunityService;
import com.ten.aditum.back.service.DeviceService;
import com.ten.aditum.back.util.TimeGenerator;
import com.ten.aditum.back.util.UidGenerator;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/device")
public class DeviceController extends BaseController<Device> {

    private final DeviceService deviceService;

    @Autowired
    public DeviceController(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    @Override
    @RequestMapping(method = RequestMethod.GET)
    public ResultModel get(Device device) {
        log.info("Device [GET] : {}", device);
        List<Device> deviceList = deviceService.select(device);
        if (deviceList == null) {
            log.warn("Device [GET] FAILURE : {}", device);
            return new ResultModel(AditumCode.ERROR);
        }
        log.info("Device [GET] SUCCESS : {} -> {}", device, deviceList);
        return new ResultModel(AditumCode.OK, deviceList);
    }

    @Override
    @RequestMapping(method = RequestMethod.POST)
    public ResultModel post(@RequestBody Device device) {
        log.info("Device [POST] : {}", device);
        Device entity = new Device()
                .setImei(UidGenerator.generateUid())
                .setAlias(device.getAlias())
                .setCommunityId(device.getCommunityId())
                .setDeviceStatus(0)
                .setCreateTime(TimeGenerator.currentTime())
                .setUpdateTime(TimeGenerator.currentTime())
                .setIsDeleted(NO_DELETED);

        int result = deviceService.insert(entity);
        if (result < 1) {
            log.warn("Device [POST] FAILURE : {}", device);
            return new ResultModel(AditumCode.ERROR);
        }
        log.info("Device [POST] SUCCESS : {}", device);
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
