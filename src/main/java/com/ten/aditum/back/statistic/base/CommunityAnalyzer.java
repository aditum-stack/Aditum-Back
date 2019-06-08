package com.ten.aditum.back.statistic.base;

import com.ten.aditum.back.statistic.BaseAnalysor;
import com.ten.aditum.back.entity.Community;
import com.ten.aditum.back.entity.Device;
import com.ten.aditum.back.entity.Person;
import com.ten.aditum.back.util.TimeGenerator;
import lombok.extern.slf4j.Slf4j;
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

    @Scheduled(cron = TEST_TIME)

    /**
     * 每天2点更新社区信息
     */
    @Scheduled(cron = "0 0 2 1/1 * ?")
    public void analysis() {
        log.info("开始更新社区信息...");
        List<Community> communityList = selectAllCommunity();
        communityList.forEach(this::analysisCommunityDevice);
        communityList.forEach(this::analysisCommunityPerson);
        log.info("社区信息更新完成...");
    }

    /**
     * 分析社区的设备信息
     */
    private void analysisCommunityDevice(Community community) {
        String communityId = community.getCommunityId();
        Integer originDeviceCount = community.getDeviceCount();
        String communityName = community.getCommunityName();

        List<Device> deviceList = selectAllDevice(communityId);
        if (deviceList.size() < 1) {
            log.warn("社区 {} 没有任何设备", communityName);
            return;
        }

        int deviceCount = deviceList.size();
        if (deviceCount == originDeviceCount) {
            log.info("社区 {} 没有新增设备", communityName);
            return;
        }
        if (deviceCount < originDeviceCount) {
            log.warn("社区 {} 设备数量减少 {}", communityName, originDeviceCount - deviceCount);
        }

        int id = community.getId();
        Community communityEntity = new Community()
                .setId(id)
                .setDeviceCount(deviceCount)
                .setDeviceOnlineCount(deviceCount)
                .setUpdateTime(TimeGenerator.currentDateTime());
        communityService.update(communityEntity);
        log.info("社区 {} 设备总数更新完成 {}", communityName, deviceCount);
    }

    /**
     * 分析社区的用户信息
     */
    private void analysisCommunityPerson(Community community) {
        String communityId = community.getCommunityId();
        Integer originPersonCount = community.getPersonCount();
        String communityName = community.getCommunityName();

        List<Person> personList = selectAllPerson(communityId);
        if (personList.size() < 1) {
            log.warn("社区 {} 没有任何人员", community.getCommunityName());
            return;
        }

        int personCount = personList.size();
        if (personCount == originPersonCount) {
            log.info("社区 {} 没有新注册用户", communityName);
            return;
        }
        if (personCount < originPersonCount) {
            log.warn("社区 {} 用户数量减少 {}", communityName, originPersonCount - personCount);
        }

        int id = community.getId();
        Community communityEntity = new Community()
                .setId(id)
                .setPersonCount(personCount)
                .setUpdateTime(TimeGenerator.currentDateTime());
        communityService.update(communityEntity);
        log.info("社区 {} 人员总数更新完成 {}", community.getCommunityName(), personCount);
    }

}
