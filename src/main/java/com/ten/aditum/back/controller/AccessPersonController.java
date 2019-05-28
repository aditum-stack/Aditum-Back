package com.ten.aditum.back.controller;

import com.ten.aditum.back.entity.AccessAddress;
import com.ten.aditum.back.entity.AccessTime;
import com.ten.aditum.back.entity.Person;
import com.ten.aditum.back.model.AditumCode;
import com.ten.aditum.back.model.ResultModel;
import com.ten.aditum.back.service.AccessAddressService;
import com.ten.aditum.back.service.AccessIntervalService;
import com.ten.aditum.back.service.AccessTimeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/access/person")
public class AccessPersonController {

    private static final int NO_DELETED = 0;
    private static final int IS_DELETED = 1;

    private final AccessAddressService accessAddressService;
    private final AccessIntervalService accessIntervalService;
    private final AccessTimeService accessTimeService;

    @Autowired
    public AccessPersonController(AccessAddressService accessAddressService,
                                  AccessIntervalService accessIntervalService,
                                  AccessTimeService accessTimeService) {
        this.accessAddressService = accessAddressService;
        this.accessIntervalService = accessIntervalService;
        this.accessTimeService = accessTimeService;
    }

    /**
     * 根据personId获取person的访问时间数据
     */
    @RequestMapping(value = "/time", method = RequestMethod.GET)
    public ResultModel getTime(Person person) {
        log.debug("AccessPersonTime [GET] : {}", person);

        if (person.getPersonnelId() == null) {
            return new ResultModel(AditumCode.ERROR);
        }

        AccessTime timeEntity = new AccessTime()
                .setPersonnelId(person.getPersonnelId())
                .setIsDeleted(NO_DELETED);

        List<AccessTime> accessTimeList = accessTimeService.select(timeEntity);
        if (accessTimeList.size() < 1) {
            log.warn("AccessPersonTime [GET] FAILURE : {}", person);
            return new ResultModel(AditumCode.ERROR);
        }

        log.debug("AccessPersonTime [GET] SUCCESS : {} -> {}", person, accessTimeList.get(0));
        return new ResultModel(AditumCode.OK, accessTimeList.get(0));
    }

    /**
     * 根据personId获取person的访问地点数据
     */
    @RequestMapping(value = "/address", method = RequestMethod.GET)
    public ResultModel getAddress(Person person) {
        log.debug("AccessPersonAddress [GET] : {}", person);

        if (person.getPersonnelId() == null) {
            return new ResultModel(AditumCode.ERROR);
        }

        AccessAddress timeEntity = new AccessAddress()
                .setPersonnelId(person.getPersonnelId())
                .setIsDeleted(NO_DELETED);

        List<AccessAddress> accessAddressList = accessAddressService.select(timeEntity);
        if (accessAddressList.size() < 1) {
            log.warn("AccessPersonAddress [GET] FAILURE : {}", person);
            return new ResultModel(AditumCode.ERROR);
        }

        log.debug("AccessPersonAddress [GET] SUCCESS : {} -> {}", person, accessAddressList.get(0));
        return new ResultModel(AditumCode.OK, accessAddressList.get(0));
    }

}
