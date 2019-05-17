package com.ten.aditum.back.statistic.base;

import com.ten.aditum.back.BaseAnalysor;
import com.ten.aditum.back.entity.Community;
import com.ten.aditum.back.entity.Device;
import com.ten.aditum.back.entity.Person;
import com.ten.aditum.back.service.CommunityService;
import com.ten.aditum.back.service.DeviceService;
import com.ten.aditum.back.service.PersonService;
import com.ten.aditum.back.service.RecordService;
import com.ten.aditum.back.util.TimeGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@EnableScheduling
@EnableAutoConfiguration
public class CommunityAnalyzer extends BaseAnalysor {

    private final CommunityService communityService;
    private final DeviceService deviceService;
    private final PersonService personService;
    private final RecordService recordService;

    @Autowired
    public CommunityAnalyzer(RecordService recordService,
                             DeviceService deviceService,
                             CommunityService communityService,
                             PersonService personService) {
        this.recordService = recordService;
        this.deviceService = deviceService;
        this.communityService = communityService;
        this.personService = personService;
    }

    /**
     * 每天0点更新社区信息
     */
//    @Scheduled(cron = TEST_TIME)
    @Scheduled(cron = "0 0 0 1/1 * ?")
    public void analysis() {
        log.info("开始更新社区信息...");

        Community communityEntity = new Community()
                .setIsDeleted(NO_DELETED);
        List<Community> communityList = communityService.select(communityEntity);

        log.info("查询所有community集合 : {}", communityList);

        communityList.forEach(this::analysisCommunityDevice);
        communityList.forEach(this::analysisCommunityPerson);

        log.info("社区信息更新完成...");
    }

    private void analysisCommunityDevice(Community community) {
        String communityId = community.getCommunityId();

        Device deviceEntity = new Device()
                .setCommunityId(communityId)
                .setIsDeleted(NO_DELETED);

        List<Device> deviceList = deviceService.select(deviceEntity);

        if (deviceList.size() < 1) {
            log.warn("此社区 {} 没有任何设备", community.getCommunityName());
            return;
        }

        int deviceCount = deviceList.size();

        int id = community.getId();

        Community communityEntity = new Community()
                .setId(id)
                .setDeviceCount(deviceCount)
                .setDeviceOnlineCount(deviceCount)
                .setUpdateTime(TimeGenerator.currentTime());

        communityService.update(communityEntity);

        log.info("此社区 {} 设备总数更新完成 {}", community.getCommunityName(), deviceCount);
    }

    private void analysisCommunityPerson(Community community) {
        String communityId = community.getCommunityId();

        Person personEntity = new Person()
                .setCommunityId(communityId)
                .setIsDeleted(NO_DELETED);

        List<Person> personList = personService.select(personEntity);

        if (personList.size() < 1) {
            log.warn("此社区 {} 没有任何人员", community.getCommunityName());
            return;
        }

        int personCount = personList.size();

        int id = community.getId();

        Community communityEntity = new Community()
                .setId(id)
                .setPersonCount(personCount)
                .setUpdateTime(TimeGenerator.currentTime());

        communityService.update(communityEntity);

        log.info("此社区 {} 人员总数更新完成 {}", community.getCommunityName(), personCount);
    }

}
