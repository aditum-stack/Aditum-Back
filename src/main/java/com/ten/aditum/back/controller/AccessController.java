package com.ten.aditum.back.controller;

import com.ten.aditum.back.entity.AccessInterval;
import com.ten.aditum.back.model.ResultModel;
import com.ten.aditum.back.service.AccessAddressService;
import com.ten.aditum.back.service.AccessIntervalService;
import com.ten.aditum.back.service.AccessTimeService;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(value = "/access")
public class AccessController {

    private final AccessAddressService accessAddressService;
    private final AccessIntervalService accessIntervalService;
    private final AccessTimeService accessTimeService;

    @Autowired
    public AccessController(AccessAddressService accessAddressService, AccessIntervalService accessIntervalService, AccessTimeService accessTimeService) {
        this.accessAddressService = accessAddressService;
        this.accessIntervalService = accessIntervalService;
        this.accessTimeService = accessTimeService;
    }

}
