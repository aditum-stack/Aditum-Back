package com.ten.aditum.back.service;

import com.ten.aditum.back.controller.BaseController;
import com.ten.aditum.back.entity.*;
import com.ten.aditum.back.util.TimeGenerator;
import com.ten.aditum.back.vo.BasicCountData;
import com.ten.aditum.back.vo.BasicDeviceCountData;
import com.ten.aditum.back.vo.BasicDeviceWeekendData;
import com.ten.aditum.back.vo.BasicLabelData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 基础图表数据展示，功能函数
 */
@Slf4j
@Service
public class BasicDataService {

    private static final int WEEKEND = 7;

    private final CommunityService communityService;
    private final DeviceService deviceService;
    private final PersonService personService;
    private final RecordService recordService;
    private final PersonasPortraitService personasPortraitService;

    @Autowired
    public BasicDataService(CommunityService communityService,
                            DeviceService deviceService,
                            PersonService personService,
                            RecordService recordService, PersonasPortraitService personasPortraitService) {
        this.communityService = communityService;
        this.deviceService = deviceService;
        this.personService = personService;
        this.recordService = recordService;
        this.personasPortraitService = personasPortraitService;
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

    /**
     * 分析数量最多的用户画像标签
     */
    public BasicLabelData analysisBasicLabelCount(String communityId, int maxCount) {
        // TODO 根据communityId获取person
        Person person = new Person()
                .setIsDeleted(BaseController.NO_DELETED);
        List<Person> personList = personService.select(person);

        // 标签数量集合
        Map<String, Integer> labelCountMap = new ConcurrentHashMap<>(personList.size());

        // 统计各个标签出现的次数
        personList.parallelStream().forEach(person1 -> {
            PersonasPortrait personasPortrait = new PersonasPortrait()
                    .setPersonnelId(person1.getPersonnelId())
                    .setIsDeleted(BaseController.NO_DELETED);
            List<PersonasPortrait> personasPortraits = personasPortraitService.select(personasPortrait);
            if (personasPortraits.size() > 0) {
                PersonasPortrait portrait = personasPortraits.get(0);
                String personasExt = portrait.getPersonasExt();
                String[] labels = personasExt.split(",");
                for (String label : labels) {
                    if (labelCountMap.containsKey(label)) {
                        Integer value = labelCountMap.get(label);
                        labelCountMap.put(label, value + 1);
                    } else {
                        labelCountMap.put(label, 0);
                    }
                }
            }
        });

        // 降序排列
        List<Map.Entry<String, Integer>> list = new ArrayList<>(labelCountMap.entrySet());
        list.sort((o1, o2) -> o2.getValue().compareTo(o1.getValue()));

        // 获取数量最多的maxCount个标签
        BasicLabelData basicLabelData = new BasicLabelData();
        List<BasicLabelData.LabelCount> labelCountList = new ArrayList<>();
        for (int i = 0; i < maxCount; i++) {
            BasicLabelData.LabelCount labelCount = new BasicLabelData.LabelCount();
            labelCount.setLabelName(list.get(i).getKey());
            labelCount.setLabelCount(list.get(i).getValue());
            labelCountList.add(labelCount);
        }
        basicLabelData.setLabelCountList(labelCountList);
        return basicLabelData;
    }

    /**
     * 分析访问量最大的设备以及它们的访问量
     */
    public BasicDeviceCountData analysisBasicDeviceData(String communityId, int maxCount) {
        Device device = new Device()
                .setCommunityId(communityId)
                .setIsDeleted(BaseController.NO_DELETED);
        List<Device> deviceList = deviceService.select(device);

        // 数量集合
        PriorityQueue<BasicDeviceCountData.DeviceCount> queue = new PriorityQueue<>(deviceList.size(), new Comparator<BasicDeviceCountData.DeviceCount>() {
            @Override
            public int compare(BasicDeviceCountData.DeviceCount o1, BasicDeviceCountData.DeviceCount o2) {
                return o2.getDeviceCount() - o1.getDeviceCount();
            }
        });

        // 设备访问量
        deviceList.parallelStream().forEach(device1 -> {
            Record record = new Record()
                    .setImei(device1.getImei())
                    .setIsDeleted(BaseController.NO_DELETED);
            int count = recordService.selectCount(record);
            if (count > 0) {
                BasicDeviceCountData.DeviceCount deviceCount = new BasicDeviceCountData.DeviceCount();
                deviceCount.setImei(device1.getImei());
                deviceCount.setDeviceName(device1.getAlias());
                deviceCount.setDeviceCount(count);
                queue.add(deviceCount);
            }
        });

        // 获取数量最多的maxCount个标签
        BasicDeviceCountData basicDeviceCountData = new BasicDeviceCountData();
        List<BasicDeviceCountData.DeviceCount> deviceCounts = new ArrayList<>();
        for (int i = 0; i < maxCount; i++) {
            deviceCounts.add(queue.poll());
        }
        basicDeviceCountData.setDeviceCountList(deviceCounts);
        return basicDeviceCountData;
    }

    /**
     * 展示首页的最近七天预览，最近七天每天访问量前MOST_WEEKEND_COUNT的设备（访问量前MOST_WEEKEND_COUNT的设备最近一周的访问量）
     */
    public BasicDeviceWeekendData analysisBasicWeekendData(String communityId, int mostWeekendCount) {
        // 设备一周访问时间
        String[] weekendDays = TimeGenerator.weekendZeroDateTimes();
        // 访问量最大的mostWeekendCount个设备
        BasicDeviceCountData basicDeviceCountData = analysisBasicDeviceData(communityId, mostWeekendCount);
        List<BasicDeviceCountData.DeviceCount> deviceCountList = basicDeviceCountData.getDeviceCountList();

        // 获取数量最多的maxCount个标签
        BasicDeviceWeekendData basicDeviceWeekendData = new BasicDeviceWeekendData();
        List<BasicDeviceWeekendData.WeekendDeviceCount> weekendDeviceCounts = new ArrayList<>();

        // 获取每台设备的最近一周访问量
        deviceCountList.parallelStream().forEach(deviceCount -> {
            List<Integer> weekendAccessCount = new ArrayList<>(7);
            String imei = deviceCount.getImei();
            Record record = new Record()
                    .setImei(imei)
                    .setIsDeleted(BaseController.NO_DELETED);
            // 今天的访问量
            int todayCount = recordService.selectCountAfterDateTime(record, weekendDays[0]);
            weekendAccessCount.add(todayCount);
            // 其余天数的访问量
            for (int i = 0; i < weekendDays.length - 1; i++) {
                // 第一次为：昨天一整天的访问量
                int countBetweenDateTime = recordService.selectCountBetweenDateTime(record, weekendDays[i + 1], weekendDays[i]);
                weekendAccessCount.add(countBetweenDateTime);
            }

            BasicDeviceWeekendData.WeekendDeviceCount weekendDeviceCount = new BasicDeviceWeekendData.WeekendDeviceCount();
            weekendDeviceCount.setDeviceName(deviceCount.getDeviceName());
            weekendDeviceCount.setWeekendAccessCount(weekendAccessCount);
            weekendDeviceCounts.add(weekendDeviceCount);
        });

        basicDeviceWeekendData.setWeekendDeviceCountList(weekendDeviceCounts);
        return basicDeviceWeekendData;
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
            return basicCountDataResult;
        }
        int count = communityList.size();
        basicCountDataResult.setCommunityCount(count);
        List<String> timeList = communityList.stream()
                .map(Community::getCreateTime)
                .collect(Collectors.toList());
        List<BasicCountData.TotalAndIncrement> totalAndIncrements = analysisWeekendData(timeList);
        basicCountDataResult.setCommunityCountList(totalAndIncrements);
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
            return basicCountDataResult;
        }
        int count = deviceList.size();
        basicCountDataResult.setDeviceCount(count);
        List<String> timeList = deviceList.stream()
                .map(Device::getCreateTime)
                .collect(Collectors.toList());
        List<BasicCountData.TotalAndIncrement> totalAndIncrements = analysisWeekendData(timeList);
        basicCountDataResult.setDeviceCountList(totalAndIncrements);
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
            return basicCountDataResult;
        }
        int count = personList.size();
        basicCountDataResult.setPersonCount(count);
        List<String> timeList = personList.stream()
                .map(Person::getCreateTime)
                .collect(Collectors.toList());
        List<BasicCountData.TotalAndIncrement> totalAndIncrements = analysisWeekendData(timeList);
        basicCountDataResult.setPersonCountList(totalAndIncrements);
        return basicCountDataResult;
    }

    /**
     * 获取record的总数和最近一周的总量增量数据
     */
    private BasicCountData analysisRecord(String communityId, BasicCountData basicCountDataResult) {
        if (basicCountDataResult == null) {
            return null;
        }
        // TODO 根据社区communityId查询record
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
