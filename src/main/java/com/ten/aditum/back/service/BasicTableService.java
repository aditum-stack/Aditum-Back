package com.ten.aditum.back.service;

import com.ten.aditum.back.controller.BaseController;
import com.ten.aditum.back.entity.Device;
import com.ten.aditum.back.entity.Person;
import com.ten.aditum.back.entity.Record;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.stream.Collectors;

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

    /**
     * 获取访问量最多的mostPersonCount个用户
     */
    public List<Person> mostAccessPersonList(String communityId, int mostPersonCount) {
        Person person = new Person()
                .setCommunityId(communityId)
                .setIsDeleted(BaseController.NO_DELETED);
        List<Person> personList = personService.select(person);

        // 使用id属性保存访问量Int
        PriorityQueue<Person> countList = new PriorityQueue<>(
                personList.size(), (o1, o2) -> o2.getId() - o1.getId());

        // 统计访问数量
        personList.parallelStream().forEach(person1 -> {
            Record record = new Record()
                    .setPersonnelId(person1.getPersonnelId())
                    .setIsDeleted(BaseController.NO_DELETED);
            int personCount = recordService.selectCount(record);
            person1.setId(personCount);
            countList.add(person1);
        });

        List<Person> mostAccessPersonList = new ArrayList<>(mostPersonCount);
        for (int i = 0; i < mostPersonCount; i++) {
            mostAccessPersonList.add(countList.poll());
        }
        return mostAccessPersonList;
    }

    /**
     * 获取使用天数最长的mostDeviceRepair个设备列表
     */
    public List<Device> mostRepairDeviceList(String communityId, int mostDeviceRepair) {
        Device device = new Device()
                .setCommunityId(communityId)
                .setIsDeleted(BaseController.NO_DELETED);
        List<Device> deviceList = deviceService.select(device);
        List<Device> list = deviceList.parallelStream()
                .sorted(Comparator.comparing(Device::getCreateTime))
                .collect(Collectors.toList());
        return list.subList(0, mostDeviceRepair);
    }
}
