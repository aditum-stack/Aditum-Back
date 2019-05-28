//package com.ten.aditum.back.controller;
//
//import com.ten.aditum.back.model.AditumCode;
//import com.ten.aditum.back.model.ResultModel;
//import com.ten.aditum.back.service.BasicDataService;
//import com.ten.aditum.back.service.BasicTableService;
//import com.ten.aditum.back.vo.BasicCountData;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RestController;
//
///**
// * 首页的表格数据接口
// */
//@Slf4j
//@RestController
//@RequestMapping(value = "/basic/table")
//public class BasicTableController {
//
//    private final BasicTableService basicTableService;
//
//    @Autowired
//    public BasicTableController(BasicTableService basicTableService) {
//        this.basicTableService = basicTableService;
//    }
//
//    /**
//     * 缓存
//     */
//    private ResultModel cache;
//    private long cacheTime = System.currentTimeMillis();
//    /**
//     * 缓存有效时间 12小时
//     */
//    private static final long VALID_TIME = 1000 * 60 * 60 * 12;
//
//    /**
//     * 展示访问量最多的七个用户，同时返回当前状态
//     */
//    @RequestMapping(value = "personMost", method = RequestMethod.GET)
//    public ResultModel showBasicUserTable(BasicCountData basicCountData) {
//        String communityId = basicCountData.getCommunityId();
//        if (communityId == null) {
//            return new ResultModel(AditumCode.ERROR, "communityId不能为空");
//        }
//        log.debug("BasicCountData [GET] : {}", communityId);
//
//        // 初始化缓存
//        if (cache == null) {
//            basicCountData = basicTableService.analysisBasicData(communityId);
//            if (basicCountData == null) {
//                log.warn("BasicCountData [GET] [INIT] FAILURE : {} -> {}", communityId);
//                return new ResultModel(AditumCode.ERROR);
//            }
//            basicCountData.setCommunityId(communityId);
//            cache = new ResultModel(AditumCode.OK, basicCountData);
//            log.info("BasicCountData [GET] [INIT] SUCCESS {}", basicCountData);
//            return cache;
//        }
//
//        // 缓存过期，更新
//        long current = System.currentTimeMillis();
//        if (current - cacheTime > VALID_TIME) {
//            cacheTime = current;
//            BasicCountData basicCountDataResult = basicTableService.analysisBasicData(communityId);
//            if (basicCountDataResult == null) {
//                log.warn("BasicCountData [GET] FAILURE : {} -> {}", communityId);
//                return new ResultModel(AditumCode.ERROR);
//            }
//            basicCountDataResult.setCommunityId(communityId);
//            log.debug("BasicCountData [GET] SUCCESS : {} -> {}", communityId, basicCountDataResult);
//            return new ResultModel(AditumCode.OK, basicCountDataResult);
//        }
//
//        log.info("BasicCountData [GET] [CACHE] SUCCESS {}", cache.getData());
//        return cache;
//    }
//    /**
//     * 待维修设备，使用时间最常的8台设备
//     */
//    @RequestMapping(value = "deviceRepair", method = RequestMethod.GET)
//    public ResultModel showBasicDeviceRepair(BasicCountData basicCountData) {
//        String communityId = basicCountData.getCommunityId();
//        if (communityId == null) {
//            return new ResultModel(AditumCode.ERROR, "communityId不能为空");
//        }
//        log.debug("showBasicDeviceRepair [GET] : {}", communityId);
//
//        // 初始化缓存
//        if (cache == null) {
//            basicCountData = basicTableService.analysisBasicData(communityId);
//            if (basicCountData == null) {
//                log.warn("showBasicDeviceRepair [GET] [INIT] FAILURE : {} -> {}", communityId);
//                return new ResultModel(AditumCode.ERROR);
//            }
//            basicCountData.setCommunityId(communityId);
//            cache = new ResultModel(AditumCode.OK, basicCountData);
//            log.info("showBasicDeviceRepair [GET] [INIT] SUCCESS {}", basicCountData);
//            return cache;
//        }
//
//        // 缓存过期，更新
//        long current = System.currentTimeMillis();
//        if (current - cacheTime > VALID_TIME) {
//            cacheTime = current;
//            BasicCountData basicCountDataResult = basicTableService.analysisBasicData(communityId);
//            if (basicCountDataResult == null) {
//                log.warn("showBasicDeviceRepair [GET] FAILURE : {} -> {}", communityId);
//                return new ResultModel(AditumCode.ERROR);
//            }
//            basicCountDataResult.setCommunityId(communityId);
//            log.debug("showBasicDeviceRepair [GET] SUCCESS : {} -> {}", communityId, basicCountDataResult);
//            return new ResultModel(AditumCode.OK, basicCountDataResult);
//        }
//
//        log.info("showBasicDeviceRepair [GET] [CACHE] SUCCESS {}", cache.getData());
//        return cache;
//    }
//
//}
