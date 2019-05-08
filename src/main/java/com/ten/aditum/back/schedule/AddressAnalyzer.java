package com.ten.aditum.back.schedule;


import com.ten.aditum.back.entity.*;
import com.ten.aditum.back.service.*;
import com.ten.aditum.back.util.TimeGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;

@Slf4j
@Component
@EnableScheduling
@EnableAutoConfiguration
public class AddressAnalyzer implements Analyzer {

    private final CommunityService communityService;
    private final DeviceService deviceService;
    private final PersonService personService;
    private final RecordService recordService;

    private final AccessAddressService accessAddressService;

    @Autowired
    public AddressAnalyzer(AccessAddressService accessAddressService,
                           RecordService recordService,
                           PersonService personService,
                           DeviceService deviceService,
                           CommunityService communityService) {
        this.accessAddressService = accessAddressService;
        this.recordService = recordService;
        this.personService = personService;
        this.deviceService = deviceService;
        this.communityService = communityService;
    }

    /**
     * 每天0点分析用户访问地址
     */
    @Scheduled(cron = "0 0 0 1/1 * ?")
    public void analysis() {
        log.info("开始分析用户访问地址...");

        // 查询所有person
        Person personEntity = new Person()
                .setIsDeleted(NO_DELETED);
        List<Person> personList = personService.select(personEntity);

        log.info("查询所有person集合 : {}", personList);

        personList.forEach(this::analysisPerson);

        log.info("用户访问地址分析完成...");
    }

    /**
     * 分析单个person的行为
     */
    private void analysisPerson(Person person) {

        // person的访问地址集合
        Map<String, Integer> personAddressMap = new HashMap<>();

        log.info("开始分析此person : {}", person);

        // 查询person的所有record
        Record recordEntity = new Record()
                .setPersonnelId(person.getPersonnelId())
                .setIsDeleted(NO_DELETED);
        List<Record> recordList = recordService.select(recordEntity);

        log.info("查询此person下的所有record集合 : {}", recordList);

        recordList.forEach(record -> {
            log.info("开始分析此record : {}", record);

            // 查询record对应的device
            String imei = record.getImei();
            Device deviceEntity = new Device()
                    .setImei(imei)
                    .setIsDeleted(NO_DELETED);
            List<Device> deviceList = deviceService.select(deviceEntity);
            if (deviceList.size() < 1) {
                throw new RuntimeException("Record对应Device为空!");
            }

            Device theDevice = deviceList.get(0);

            log.info("查询此record下的device : {}", theDevice);

            // 查询device对应的community
            String communityId = theDevice.getCommunityId();
            Community communityEntity = new Community()
                    .setCommunityId(communityId)
                    .setIsDeleted(NO_DELETED);
            List<Community> communityList = communityService.select(communityEntity);
            if (communityList.size() < 1) {
                throw new RuntimeException("Device对应Community为空!");
            }

            Community theCommunity = communityList.get(0);

            log.info("查询此device下的community : {}", theCommunity);

            // 获取community地址
            String communityCity = theCommunity.getCommunityCity();
            String communityAddress = theCommunity.getCommunityAddress();

            String address = communityCity + communityAddress;

            // 若已经包含此地址，次数+1
            if (personAddressMap.containsKey(address)) {
                Integer count = personAddressMap.get(address);
                personAddressMap.put(address, count + 1);

                log.info("已包含此地址，次数+1 : {} by {}", address, person.getPersonnelName());
            }
            // 若未包含此地址，次数=1
            else {
                personAddressMap.put(address, 1);

                log.info("未包含此地址，次数=1 : {} by {}", address, person.getPersonnelName());
            }
        });

        // ------------------------------------------ 一个person的所有record遍历结束

        // 获取访问过的所有地址
        Set<String> addressSet = personAddressMap.keySet();

        // 拼接所有地址
        List<String> addressList = new ArrayList(addressSet);
        String collection = String.join(",", addressList);

        log.info("获取到此person {} 下的所有地址 {}", person.getPersonnelName(), collection);

        // 获取访问次数最多的地址
        String maxCountAddress = getMaxCount(personAddressMap);
        // 获取访问次数最多的次数
        Integer maxCount = personAddressMap.get(maxCountAddress);

        log.info("获取到此person {} 下的最常访问地址 {} {}次", person.getPersonnelName(), maxCountAddress, maxCount);

        Integer collectionCount = addressList.size();

        // 生成数据对象
        AccessAddress accessAddress = new AccessAddress()
                .setPersonnelId(person.getPersonnelId())
                .setFirstAddress(maxCountAddress)
                .setFirstAddressCount(maxCount)
                .setTotalAddress(collection)
                .setTotalAddressCount(collectionCount)
                .setTotalCount(recordList.size())
                .setIsDeleted(NO_DELETED);

        // 查询此person的原纪录
        AccessAddress accessAddressEntity = new AccessAddress()
                .setPersonnelId(person.getPersonnelId())
                .setIsDeleted(NO_DELETED);
        List<AccessAddress> accessAddressList = accessAddressService.select(accessAddressEntity);
        // 当前用户不存在记录，创建
        if (accessAddressList.size() < 1) {
            accessAddress
                    .setCreateTime(TimeGenerator.currentTime());
            accessAddressService.insert(accessAddress);

            log.info("此person {} 还没有地址记录，插入 {}", person.getPersonnelName(), accessAddress);
        }
        // 当前用户已有记录，更新
        else {
            AccessAddress origin = accessAddressList.get(0);
            Integer id = origin.getId();
            accessAddress
                    .setId(id)
                    .setUpdateTime(TimeGenerator.currentTime());
            accessAddressService.update(accessAddress);

            log.info("此person {} 已经有地址记录，更新 {}", person.getPersonnelName(), accessAddress);
        }
    }

    private String getMaxCount(Map<String, Integer> map) {
        List<Map.Entry<String, Integer>> list = new ArrayList<>(map.entrySet());
        list.sort(Comparator.comparingInt(Map.Entry::getValue));
        if (list.size() > 0) {
            return list.get(0).getKey();
        } else {
            return "";
        }
    }

}
