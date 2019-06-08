package com.ten.aditum.back.statistic.person;


import com.alibaba.fastjson.JSON;
import com.ten.aditum.back.statistic.BaseAnalysor;
import com.ten.aditum.back.entity.*;
import com.ten.aditum.back.util.TimeGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Component
@EnableScheduling
@EnableAutoConfiguration
public class AccessAddressAnalyzer extends BaseAnalysor {

//    @Scheduled(cron = TEST_TIME)

    /**
     * 每天1点10分分析用户访问地址
     */
    @Scheduled(cron = "0 10 1 1/1 * ?")
    public void analysis() {
        log.info("开始分析用户访问地址...");
        List<Person> personList = selectAllPerson();
        personList.forEach(this::analysisPerson);
        log.info("用户访问地址分析完成...");
    }

    /**
     * 分析单个person的行为
     */
    private void analysisPerson(Person person) {
        AccessAddress access = new AccessAddress()
                .setPersonnelId(person.getPersonnelId())
                .setTotalCount(0);
        // person的访问地址集合
        Map<String, Integer> personAddressMap = new HashMap<>();
        // person的访问设备集合
        Map<String, Integer> personImeiMap = new HashMap<>();
        for (int i = 0; ; ) {
            Record recordEntity = new Record()
                    .setId(i)
                    .setPersonnelId(person.getPersonnelId())
                    .setIsDeleted(NO_DELETED);
            List<Record> recordList = recordService.selectAfterTheId(recordEntity);
            if (recordList.size() == 0) {
                break;
            }
            analysisPersonRecord(access, recordList, personAddressMap, personImeiMap);
            // 若数量为SELECT_SIZE，说明后面可能还有未取出的数据
            if (recordList.size() >= SELECT_SIZE) {
                i = recordList.get(SELECT_SIZE - 1).getId();
            }
            // 如数量不足SELECT_SIZE，说明后面已经没有数据了
            else {
                break;
            }
        }

        // 社区分析
        Set<String> addressSet = personAddressMap.keySet();
        List<String> addressList = new ArrayList<>(addressSet);
        String collection = String.join(",", addressList);
        String maxCountAddress = getMaxCount(personAddressMap);
        Integer maxCount = personAddressMap.get(maxCountAddress);
        log.debug("Person {} 最常访问社区 {} {}次",
                person.getPersonnelName(), maxCountAddress, maxCount);

        // 设备分析
        Set<String> imeiSet = personImeiMap.keySet();
        List<String> imeiList = new ArrayList<>(imeiSet);
        String imeiCollection = String.join(",", imeiList);
        String maxCountImei = getMaxCount(personImeiMap);
        Integer maxCount2 = personImeiMap.get(maxCountImei);
        log.debug("Person {} 最常访问设备 {} {}次，累计 {}次",
                person.getPersonnelName(), maxCountImei, maxCount2, access.getTotalCount());

        if (imeiCollection.length() > 500) {
            imeiCollection = imeiCollection.substring(0, 499);
            log.error("AccessAddress.totalAddress需要扩容，切割前长度{}", imeiCollection.length());
        }

        // 访问过的设备
        Integer collectionCount = imeiList.size();
        // 生成数据对象
        AccessAddress accessAddress = new AccessAddress()
                .setPersonnelId(person.getPersonnelId())
                .setFirstAddress(maxCountAddress)
                .setFirstAddressCount(maxCount)
                .setSecondAddress(maxCountImei)
                .setSecondAddressCount(maxCount2)
                .setTotalAddress(imeiCollection)
                .setTotalAddressCount(collectionCount)
                .setTotalCount(access.getTotalCount())
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
            log.debug("Person {} 还没有地址记录，插入 {}", person.getPersonnelName(), accessAddress);
        }
        // 当前用户已有记录，更新
        else {
            AccessAddress origin = accessAddressList.get(0);
            Integer id = origin.getId();
            accessAddress
                    .setId(id)
                    .setUpdateTime(TimeGenerator.currentTime());
            accessAddressService.update(accessAddress);
            log.debug("Person {} 已经有地址记录，更新 {}", person.getPersonnelName(), accessAddress);
        }
    }

    /**
     * 分析用户地址信息
     */
    private void analysisPersonRecord(AccessAddress accessAddress,
                                      List<Record> recordList,
                                      Map<String, Integer> personAddressMap,
                                      Map<String, Integer> personImeiMap) {
        AtomicInteger total = new AtomicInteger(accessAddress.getTotalCount());
        recordList.forEach(record -> {
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
            // 获取community地址
            String communityCity = theCommunity.getCommunityCity();
            String communityAddress = theCommunity.getCommunityAddress();
            String address = communityCity + communityAddress;
            if (personAddressMap.containsKey(address)) {
                Integer count = personAddressMap.get(address);
                personAddressMap.put(address, count + 1);
            } else {
                personAddressMap.put(address, 1);
            }

            // 获取device地址
            String alia = theDevice.getAlias();
            if (personImeiMap.containsKey(alia)) {
                Integer count = personImeiMap.get(alia);
                personImeiMap.put(alia, count + 1);
            } else {
                personImeiMap.put(alia, 1);
            }

            total.getAndIncrement();
        });
        accessAddress.setTotalCount(total.get());
    }

    /**
     * 获取访问次数最多的量
     */
    private String getMaxCount(Map<String, Integer> map) {
        List<Map.Entry<String, Integer>> list = new ArrayList<>(map.entrySet());
        list.sort((o1, o2) -> o2.getValue().compareTo(o1.getValue()));
        if (list.size() > 0) {
            return list.get(0).getKey();
        } else {
            return "";
        }
    }

}
