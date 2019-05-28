package com.ten.aditum.back.service;

import com.sun.org.apache.regexp.internal.RE;
import com.ten.aditum.back.controller.BaseController;
import com.ten.aditum.back.entity.Community;
import com.ten.aditum.back.entity.Device;
import com.ten.aditum.back.entity.Person;
import com.ten.aditum.back.entity.Record;
import com.ten.aditum.back.util.TimeGenerator;
import com.ten.aditum.back.vo.BasicCountData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 基础数据展示，功能函数
 */
@Slf4j
@Service
public class BasicDataService {

    private static final int WEEKEND = 7;

    private final CommunityService communityService;
    private final DeviceService deviceService;
    private final PersonService personService;
    private final RecordService recordService;

    @Autowired
    public BasicDataService(CommunityService communityService,
                            DeviceService deviceService,
                            PersonService personService,
                            RecordService recordService) {
        this.communityService = communityService;
        this.deviceService = deviceService;
        this.personService = personService;
        this.recordService = recordService;
    }

    /**
     * 获取基本数据的总数和最近一周的总量增量数据
     */
    public BasicCountData analysisBasicData(String communityId) {
        BasicCountData basicCountDataResult = new BasicCountData();
        // community
        basicCountDataResult = analysisCommunity(communityId, basicCountDataResult);
        // device
        basicCountDataResult = analysisDevice(communityId, basicCountDataResult);
        // person
        basicCountDataResult = analysisPerson(communityId, basicCountDataResult);
        // record
        basicCountDataResult = analysisRecord(communityId, basicCountDataResult);
        return basicCountDataResult;
    }

    // -------------------------------------------------------------- private

    /**
     * 返回一个指标为0的最近一周总量和增量的队列
     */
    private List<BasicCountData.TotalAndIncrement> newVoidListOfWeekend() {
        List<BasicCountData.TotalAndIncrement> voidList = new ArrayList<>();
        for (int i = 0; i < WEEKEND; i++) {
            BasicCountData.TotalAndIncrement entity = new BasicCountData.TotalAndIncrement();
            entity.setTotalCount(0);
            entity.setIncreaseCount(0);
            voidList.add(entity);
        }
        return voidList;
    }

    /**
     * 获取community的总数和最近一周的总量增量数据
     */
    private BasicCountData analysisCommunity(String communityId, BasicCountData basicCountDataResult) {
        if (basicCountDataResult == null) {
            return null;
        }
        Community community = new Community()
                .setCommunityId(communityId)
                .setIsDeleted(BaseController.NO_DELETED);
        List<Community> communityList = communityService.select(community);
        if (communityList == null) {
            basicCountDataResult.setCommunityCount(0);
            List<BasicCountData.TotalAndIncrement> voidTotalAndIncrements = newVoidListOfWeekend();
            basicCountDataResult.setCommunityCountList(voidTotalAndIncrements);
        } else {
            int count = communityList.size();
            basicCountDataResult.setCommunityCount(count);
            List<String> timeList = communityList.stream()
                    .map(Community::getCreateTime)
                    .collect(Collectors.toList());
            List<BasicCountData.TotalAndIncrement> totalAndIncrements = analysisWeekendData(timeList);
            basicCountDataResult.setCommunityCountList(totalAndIncrements);
        }
        return basicCountDataResult;
    }

    /**
     * 获取device的总数和最近一周的总量增量数据
     */
    private BasicCountData analysisDevice(String communityId, BasicCountData basicCountDataResult) {
        if (basicCountDataResult == null) {
            return null;
        }
        Device device = new Device()
                .setCommunityId(communityId)
                .setIsDeleted(BaseController.NO_DELETED);
        List<Device> deviceList = deviceService.select(device);
        if (deviceList == null) {
            basicCountDataResult.setDeviceCount(0);
            List<BasicCountData.TotalAndIncrement> voidTotalAndIncrements = newVoidListOfWeekend();
            basicCountDataResult.setDeviceCountList(voidTotalAndIncrements);
        } else {
            int count = deviceList.size();
            basicCountDataResult.setDeviceCount(count);
            List<String> timeList = deviceList.stream()
                    .map(Device::getCreateTime)
                    .collect(Collectors.toList());
            List<BasicCountData.TotalAndIncrement> totalAndIncrements = analysisWeekendData(timeList);
            basicCountDataResult.setDeviceCountList(totalAndIncrements);
        }
        return basicCountDataResult;
    }

    /**
     * 获取person的总数和最近一周的总量增量数据
     */
    private BasicCountData analysisPerson(String communityId, BasicCountData basicCountDataResult) {
        if (basicCountDataResult == null) {
            return null;
        }
        Person person = new Person()
                .setCommunityId(communityId)
                .setIsDeleted(BaseController.NO_DELETED);
        List<Person> personList = personService.select(person);
        if (personList == null) {
            basicCountDataResult.setPersonCount(0);
            List<BasicCountData.TotalAndIncrement> voidTotalAndIncrements = newVoidListOfWeekend();
            basicCountDataResult.setPersonCountList(voidTotalAndIncrements);
        } else {
            int count = personList.size();
            basicCountDataResult.setPersonCount(count);
            List<String> timeList = personList.stream()
                    .map(Person::getCreateTime)
                    .collect(Collectors.toList());
            List<BasicCountData.TotalAndIncrement> totalAndIncrements = analysisWeekendData(timeList);
            basicCountDataResult.setPersonCountList(totalAndIncrements);
        }
        return basicCountDataResult;
    }

    /**
     * 获取record的总数和最近一周的总量增量数据
     */
    private BasicCountData analysisRecord(String communityId, BasicCountData basicCountDataResult) {
        if (basicCountDataResult == null) {
            return null;
        }
        Record record = new Record()
                .setIsDeleted(BaseController.NO_DELETED);
        int count = recordService.selectCount(record);
        if (count == 0) {
            basicCountDataResult.setRecordCount(0);
            List<BasicCountData.TotalAndIncrement> voidTotalAndIncrements = newVoidListOfWeekend();
            basicCountDataResult.setRecordCountList(voidTotalAndIncrements);
            return basicCountDataResult;
        }

        basicCountDataResult.setRecordCount(count);
        // 最近一周每天的0点时刻
        String[] everyDayZeroTimes = TimeGenerator.weekendZeroDateTimes();
        List<BasicCountData.TotalAndIncrement> list = new ArrayList<>(8);
        // 添加第一条数据：今天的
        int i1 = recordService.selectCountAfterDateTime(record, everyDayZeroTimes[0]);
        BasicCountData.TotalAndIncrement todayCount = new BasicCountData.TotalAndIncrement();
        todayCount.setTotalCount(count);
        todayCount.setIncreaseCount(i1);
        count -= i1;
        list.add(todayCount);
        // 计算从昨天开始每天的总量和增量
        for (int i = 0; i < WEEKEND - 1; i++) {
            BasicCountData.TotalAndIncrement dailyCount = new BasicCountData.TotalAndIncrement();
            // 增量，第一次：昨天一整天的数量
            int dayIncrease = recordService.selectCountBetweenDateTime(record, everyDayZeroTimes[i + 1], everyDayZeroTimes[i]);
            dailyCount.setIncreaseCount(dayIncrease);
            // 总量
            dailyCount.setTotalCount(count);
            count -= dayIncrease;
            list.add(dailyCount);
        }
        basicCountDataResult.setRecordCountList(list);
        return basicCountDataResult;
    }

    /**
     * 根据每条记录的创建时间，返回最近一周的每天的总量和增量集合
     */
    private List<BasicCountData.TotalAndIncrement> analysisWeekendData(List<String> createTimeList) {
        // 最近一周每天的0点时刻
        String[] everyDayZeroTimes = TimeGenerator.weekendZeroDateTimes();

        // 最近七天每天新增的量
        Map<Integer, Integer> everyDayIncreate = new HashMap<>(16);
        for (int i = 0; i < WEEKEND; i++) {
            everyDayIncreate.put(i, 0);
        }

        createTimeList.forEach(time -> {
            // 今天0点之后创建的值
            if (time.compareTo(everyDayZeroTimes[0]) > 0) {
                Integer origin0 = everyDayIncreate.get(0);
                everyDayIncreate.put(0, origin0 + 1);
            }
            // 最近一周每天创建的数量
            for (int i = 0; i < everyDayZeroTimes.length - 1; i++) {
                if (everyDayZeroTimes[i].compareTo(time) > 0
                        && time.compareTo(everyDayZeroTimes[i + 1]) > 0) {
                    Integer origini = everyDayIncreate.get(i + 1);
                    everyDayIncreate.put(i + 1, origini + 1);
                }
            }
        });

        List<BasicCountData.TotalAndIncrement> list = new ArrayList<>(8);

        // 计算每天的总量和增量
        int total = createTimeList.size();
        for (int i = 0; i < WEEKEND; i++) {
            BasicCountData.TotalAndIncrement dailyCount = new BasicCountData.TotalAndIncrement();
            // 增量
            int dayIncrease = everyDayIncreate.get(i);
            dailyCount.setIncreaseCount(dayIncrease);
            // 总量
            dailyCount.setTotalCount(total);
            total -= dayIncrease;
            list.add(dailyCount);
        }
        return list;
    }
}
