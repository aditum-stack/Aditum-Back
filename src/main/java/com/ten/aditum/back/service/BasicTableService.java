package com.ten.aditum.back.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 基础图表数据展示，功能函数
 */
@Slf4j
@Service
public class BasicTableService {

    private final CommunityService communityService;
    private final DeviceService deviceService;
    private final PersonService personService;
    private final RecordService recordService;

    @Autowired
    public BasicTableService(CommunityService communityService,
                            DeviceService deviceService,
                            PersonService personService,
                            RecordService recordService) {
        this.communityService = communityService;
        this.deviceService = deviceService;
        this.personService = personService;
        this.recordService = recordService;
    }

}
