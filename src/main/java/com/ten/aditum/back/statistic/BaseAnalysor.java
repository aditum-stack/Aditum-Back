package com.ten.aditum.back.statistic;

import com.ten.aditum.back.entity.AccessTime;
import com.ten.aditum.back.entity.Community;
import com.ten.aditum.back.entity.Device;
import com.ten.aditum.back.entity.Person;
import com.ten.aditum.back.service.*;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
public abstract class BaseAnalysor {

    @Getter
    public enum LabelType {
        /**
         * 0 随机标签
         * 1 基于早晚访问时间的二维分析
         * 2 基于早或晚时间的一维分析
         * 3 基于访问频次的一维分析
         * 4 基于用户信息的数据分析
         * 5 基于用户访问地理信息的分析
         * 6 基于访问时长的分析
         * 7 基于排名的数据分析
         */
        RANDOM(0),
        TIME_TWO(1),
        TIME_ONE(2),
        FREQUENCY(3),
        BASIC_DATA(4),
        ADDRESS(5),
        INTERVAL(6),
        RANK(7);

        int type;

        LabelType(int type) {
            this.type = type;
        }
    }

    /**
     * 测试时间，每分钟执行一次
     */
    protected static final String TEST_TIME = "0 0/1 * * * ?";

    protected static final int NO_DELETED = 0;

    /**
     * Record.select操作每次取出1000条数据
     */
    protected static final int SELECT_SIZE = 1000;

    @Resource
    protected CommunityService communityService;
    @Resource
    protected DeviceService deviceService;
    @Resource
    protected PersonService personService;
    @Resource
    protected RecordService recordService;

    @Resource
    protected AccessAddressService accessAddressService;
    @Resource
    protected AccessIntervalService accessIntervalService;
    @Resource
    protected AccessTimeService accessTimeService;

    @Resource
    protected DeviceAccessCountService deviceAccessCountService;
    @Resource
    protected DeviceAccessHeatService deviceAccessHeatService;
    @Resource
    protected DeviceAccessLogService deviceAccessLogService;
    @Resource
    protected DeviceAccessTotalService deviceAccessTotalService;

    @Resource
    protected PersonasService personasService;
    @Resource
    protected PersonasLabelService personasLabelService;
    @Resource
    protected PersonasPortraitService personasPortraitService;

    /**
     * 在子类中展示本类要分析的标签，需要和数据库表中的标签信息相对应
     */
    public void showModelLabel() {
    }

    // -------------------------------------------------------------- Basic data

    /**
     * 获取所有的社区
     */
    protected List<Community> selectAllCommunity() {
        Community community = new Community()
                .setIsDeleted(NO_DELETED);
        List<Community> communityList = communityService.select(community);
        log.info("Community集合 : {}", communityList);
        return communityList;
    }

    /**
     * 获取所有的设备
     */
    protected List<Device> selectAllDevice() {
        Device device = new Device()
                .setIsDeleted(NO_DELETED);
        List<Device> deviceList = deviceService.select(device);
        log.info("Device集合 : {}", deviceList);
        return deviceList;
    }

    /**
     * 通过社区ID获取所有的设备
     */
    protected List<Device> selectAllDevice(String communityId) {
        Device device = new Device()
                .setCommunityId(communityId)
                .setIsDeleted(NO_DELETED);
        List<Device> deviceList = deviceService.select(device);
        log.info("Device集合 : {}", deviceList);
        return deviceList;
    }

    /**
     * 获取所有的用户
     */
    protected List<Person> selectAllPerson() {
        Person personEntity = new Person()
                .setIsDeleted(NO_DELETED);
        List<Person> personList = personService.select(personEntity);
        log.info("Person集合 : {}", personList);
        return personList;
    }

    /**
     * 通过社区ID获取所有的用户
     */
    protected List<Person> selectAllPerson(String communityId) {
        Person personEntity = new Person()
                .setCommunityId(communityId)
                .setIsDeleted(NO_DELETED);
        List<Person> personList = personService.select(personEntity);
        log.info("Person集合 : {}", personList);
        return personList;
    }

    // -------------------------------------------------------------- UBT data

    /**
     * 根据用户ID获取访问时间记录
     */
    protected List<AccessTime> selectPersonAccessTime(String personnelId) {
        AccessTime accessTimeEntity = new AccessTime()
                .setPersonnelId(personnelId)
                .setIsDeleted(NO_DELETED);
        List<AccessTime> select = accessTimeService.select(accessTimeEntity);
        log.info("AccessTime集合 : {}", select);
        return select;
    }

}
