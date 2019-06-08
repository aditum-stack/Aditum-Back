package com.ten.aditum.back.controller;

import com.ten.aditum.back.entity.*;
import com.ten.aditum.back.model.AditumCode;
import com.ten.aditum.back.model.ResultModel;
import com.ten.aditum.back.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 设备访问热度分析数据
 */
@Slf4j
@RestController
@RequestMapping(value = "/access/device")
public class AccessDeviceController extends BaseController {

    private final DeviceAccessCountService deviceAccessCountService;
    private final DeviceAccessHeatService deviceAccessHeatService;
    private final DeviceAccessLogService deviceAccessLogService;
    private final DeviceAccessTotalService deviceAccessTotalService;
    private final DeviceAccessMinuteHeatService deviceAccessMinuteHeatService;

    @Autowired
    public AccessDeviceController(DeviceAccessCountService deviceAccessCountService,
                                  DeviceAccessHeatService deviceAccessHeatService,
                                  DeviceAccessLogService deviceAccessLogService,
                                  DeviceAccessTotalService deviceAccessTotalService,
                                  DeviceAccessMinuteHeatService deviceAccessMinuteHeatService) {
        this.deviceAccessCountService = deviceAccessCountService;
        this.deviceAccessHeatService = deviceAccessHeatService;
        this.deviceAccessLogService = deviceAccessLogService;
        this.deviceAccessTotalService = deviceAccessTotalService;
        this.deviceAccessMinuteHeatService = deviceAccessMinuteHeatService;
    }

    /**
     * 根据IMEI获取该设备的所有访问记录
     */
    @RequestMapping(value = "/log", method = RequestMethod.GET)
    public ResultModel getLog(Device device) {
        log.info("AccessDeviceLog [GET] : {}", device);

        if (device.getImei() == null) {
            return new ResultModel(AditumCode.ERROR, "缺少imei");
        }

        DeviceAccessLog logEntity = new DeviceAccessLog()
                .setImei(device.getImei())
                .setIsDeleted(NO_DELETED);

        List<DeviceAccessLog> deviceAccessLogs = deviceAccessLogService.select(logEntity);
        if (deviceAccessLogs.size() < 1) {
            log.warn("AccessDeviceLog [GET] FAILURE : {}", device);
            return new ResultModel(AditumCode.ERROR, "数据库中无数据");
        }

        log.info("AccessDeviceLog [GET] SUCCESS : {} -> {}", device, deviceAccessLogs);
        return new ResultModel(AditumCode.OK, deviceAccessLogs);
    }

    /**
     * 根据IMEI获取设备最近一小时六十分钟的分钟访问热度
     */
    @RequestMapping(value = "/heat/minute", method = RequestMethod.GET)
    public ResultModel getMinuteHeat(Device device) {
        log.info("AccessDeviceMinuteHeat [GET] : {}", device);

        if (device.getImei() == null) {
            return new ResultModel(AditumCode.ERROR);
        }

        DeviceAccessMinuteHeat heatEntity = new DeviceAccessMinuteHeat()
                .setImei(device.getImei())
                .setIsDeleted(NO_DELETED);

        List<DeviceAccessMinuteHeat> deviceAccessMinuteHeats =
                deviceAccessMinuteHeatService.selectOneHourHeat(heatEntity);
        if (deviceAccessMinuteHeats.size() < 1) {
            log.warn("AccessDeviceMinuteHeat [GET] FAILURE : {}", device);
            return new ResultModel(AditumCode.ERROR);
        }

        // 获取最近六十条
        List<DeviceAccessMinuteHeat> deviceAccessHeatList = deviceAccessMinuteHeats.stream()
                .sorted(((o1, o2) -> o2.getCurrentMinuteTime().compareTo(o1.getCurrentMinuteTime())))
                .collect(Collectors.toList());

        if (deviceAccessHeatList.size() > 60) {
            deviceAccessHeatList = deviceAccessHeatList.subList(0, 60);
        }

        log.info("AccessDeviceMinuteHeat [GET] SUCCESS : {} -> {}", device, deviceAccessMinuteHeats);
        return new ResultModel(AditumCode.OK, deviceAccessHeatList);
    }

    /**
     * 根据IMEI获取设备最近二十四小时的访问热度
     */
    @RequestMapping(value = "/heat", method = RequestMethod.GET)
    public ResultModel getHeat(Device device) {
        log.info("AccessDeviceHeat [GET] : {}", device);

        if (device.getImei() == null) {
            return new ResultModel(AditumCode.ERROR);
        }

        DeviceAccessHeat heatEntity = new DeviceAccessHeat()
                .setImei(device.getImei())
                .setIsDeleted(NO_DELETED);

        List<DeviceAccessHeat> deviceAccessHeats = deviceAccessHeatService.selectOneDayHeat(heatEntity);
        if (deviceAccessHeats.size() < 1) {
            log.warn("AccessDeviceHeat [GET] FAILURE : {}", device);
            return new ResultModel(AditumCode.ERROR);
        }

        // 获取最近二十四条
        List<DeviceAccessHeat> deviceAccessHeatList = deviceAccessHeats.stream()
                .sorted(((o1, o2) -> o2.getCurrentHourTime().compareTo(o1.getCurrentHourTime())))
                .collect(Collectors.toList());

        if (deviceAccessHeatList.size() > 24) {
            deviceAccessHeatList = deviceAccessHeatList.subList(0, 24);
        }

        log.info("AccessDeviceHeat [GET] SUCCESS : {} -> {}", device, deviceAccessHeats);
        return new ResultModel(AditumCode.OK, deviceAccessHeatList);
    }

    /**
     * 根据IMEI获取设备按天计算的访问热度(最近三十天)
     */
    @RequestMapping(value = "/count", method = RequestMethod.GET)
    public ResultModel getCountHeat(DeviceAccessCount device) {
        log.info("AccessDeviceCount [GET] : {}", device);

        if (device.getImei() == null) {
            return new ResultModel(AditumCode.ERROR);
        }

        DeviceAccessCount countEntity = new DeviceAccessCount()
                .setImei(device.getImei())
                .setIsDeleted(NO_DELETED);

        List<DeviceAccessCount> deviceAccessCounts = deviceAccessCountService.selectOneMonth(countEntity);
        if (deviceAccessCounts.size() < 1) {
            log.warn("AccessDeviceCount [GET] FAILURE : {}", device);
            return new ResultModel(AditumCode.ERROR);
        }

        // 获取最近三十天
        List<DeviceAccessCount> deviceAccessCountList = deviceAccessCounts.stream()
                .sorted(((o1, o2) -> o2.getLogDate().compareTo(o1.getLogDate())))
                .collect(Collectors.toList());

        if (deviceAccessCountList.size() > 30) {
            deviceAccessCountList = deviceAccessCountList.subList(0, 30);
        }

        log.info("AccessDeviceCount [GET] SUCCESS : {} -> {}", device, deviceAccessCounts);
        return new ResultModel(AditumCode.OK, deviceAccessCountList);
    }

    /**
     * 根据日期logDate获取此日期所有的count热度信息
     */
    @RequestMapping(value = "/countByDay", method = RequestMethod.GET)
    public ResultModel getCountByDay(DeviceAccessCount device) {
        log.info("AccessDeviceCount ByDay [GET] : {}", device);

        if (device.getLogDate() == null) {
            return new ResultModel(AditumCode.ERROR);
        }

        device.setIsDeleted(NO_DELETED);

        List<DeviceAccessCount> deviceAccessCounts = deviceAccessCountService.select(device);
        if (deviceAccessCounts.size() < 1) {
            log.warn("AccessDeviceCount ByDay [GET] FAILURE : {}", device);
            return new ResultModel(AditumCode.ERROR);
        }

        log.info("AccessDeviceCount ByDay [GET] SUCCESS : {} -> {}", device, deviceAccessCounts);
        return new ResultModel(AditumCode.OK, deviceAccessCounts);
    }

    /**
     * 根据IMEI获取设备总访问次数和总使用天数
     */
    @RequestMapping(value = "/total", method = RequestMethod.GET)
    public ResultModel getTotal(Device device) {
        log.info("AccessDeviceTotal [GET] : {}", device);

        if (device.getImei() == null) {
            return new ResultModel(AditumCode.ERROR);
        }

        DeviceAccessTotal totalEntity = new DeviceAccessTotal()
                .setImei(device.getImei())
                .setIsDeleted(NO_DELETED);

        List<DeviceAccessTotal> deviceAccessTotals = deviceAccessTotalService.select(totalEntity);
        if (deviceAccessTotals.size() < 1) {
            log.warn("AccessDeviceTotal [GET] FAILURE : {}", device);
            return new ResultModel(AditumCode.ERROR);
        }

        log.info("AccessDeviceTotal [GET] SUCCESS : {} -> {}", device, deviceAccessTotals.get(0));
        return new ResultModel(AditumCode.OK, deviceAccessTotals.get(0));
    }

}
