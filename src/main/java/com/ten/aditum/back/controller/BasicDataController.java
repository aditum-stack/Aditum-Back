package com.ten.aditum.back.controller;

import com.ten.aditum.back.model.AditumCode;
import com.ten.aditum.back.model.ResultModel;
import com.ten.aditum.back.service.*;
import com.ten.aditum.back.vo.BasicCountData;
import com.ten.aditum.back.vo.BasicLabelData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 首页的图表数据接口
 */
@Slf4j
@RestController
@RequestMapping(value = "/basic")
public class BasicDataController extends BaseController<BasicCountData> {

    private final BasicDataService basicDataService;

    @Autowired
    public BasicDataController(BasicDataService basicDataService) {
        this.basicDataService = basicDataService;
    }

    /**
     * 缓存
     */
    private ResultModel cache;
    private long cacheTime = System.currentTimeMillis();
    /**
     * 缓存有效时间 12小时
     */
    private static final long VALID_TIME = 1000 * 60 * 60 * 12;

    /**
     * 展示四个基本属性的总数，以及最近七天的每天的总量和增量
     */
    @RequestMapping(value = "count", method = RequestMethod.GET)
    public ResultModel showBasicCountData(BasicCountData basicCountData) {
        String communityId = basicCountData.getCommunityId();
        if (communityId == null) {
            return new ResultModel(AditumCode.ERROR, "communityId不能为空");
        }
        log.info("BasicCountData [GET] : {}", communityId);

        // 初始化缓存
        if (cache == null) {
            basicCountData = basicDataService.analysisBasicData(communityId);
            if (basicCountData == null) {
                log.warn("BasicCountData [GET] [INIT] FAILURE : {} -> {}", communityId);
                return new ResultModel(AditumCode.ERROR);
            }
            basicCountData.setCommunityId(communityId);
            cache = new ResultModel(AditumCode.OK, basicCountData);
            log.info("BasicCountData [GET] [INIT] SUCCESS {}", basicCountData);
            return cache;
        }

        // 缓存过期，更新
        long current = System.currentTimeMillis();
        if (current - cacheTime > VALID_TIME) {
            cacheTime = current;
            BasicCountData basicCountDataResult = basicDataService.analysisBasicData(communityId);
            if (basicCountDataResult == null) {
                log.warn("BasicCountData [GET] FAILURE : {} -> {}", communityId);
                return new ResultModel(AditumCode.ERROR);
            }
            basicCountDataResult.setCommunityId(communityId);
            log.info("BasicCountData [GET] SUCCESS : {} -> {}", communityId, basicCountDataResult);
            return new ResultModel(AditumCode.OK, basicCountDataResult);
        }

        log.info("BasicCountData [GET] [CACHE] SUCCESS {}", cache.getData());
        return cache;
    }

    /**
     * 展示首页的标签预览，数量最多的五个标签和相对应的数量
     */
    @RequestMapping(value = "/label/count", method = RequestMethod.GET)
    public ResultModel showBasicLabelData(BasicCountData basicCountData) {
        String communityId = basicCountData.getCommunityId();
        if (communityId == null) {
            return new ResultModel(AditumCode.ERROR, "communityId不能为空");
        }
        log.info("showBasicLabelData [GET] : {}", communityId);

        // 初始化缓存
        if (cache == null) {
            BasicLabelData basicLabelData = basicDataService.analysisBasicLabelCount(communityId, 5);
            if (basicLabelData == null) {
                log.warn("showBasicLabelData [GET] [INIT] FAILURE : {} -> {}", communityId);
                return new ResultModel(AditumCode.ERROR);
            }
            cache = new ResultModel(AditumCode.OK, basicLabelData);
            log.info("showBasicLabelData [GET] [INIT] SUCCESS {}", basicLabelData);
            return cache;
        }

        // 缓存过期，更新
        long current = System.currentTimeMillis();
        if (current - cacheTime > VALID_TIME) {
            cacheTime = current;
            BasicLabelData basicLabelData = basicDataService.analysisBasicLabelCount(communityId, 5);
            if (basicLabelData == null) {
                log.warn("showBasicLabelData [GET] FAILURE : {} -> {}", communityId);
                return new ResultModel(AditumCode.ERROR);
            }
            log.info("showBasicLabelData [GET] SUCCESS : {} -> {}", communityId, basicLabelData);
            return new ResultModel(AditumCode.OK, basicLabelData);
        }

        log.info("showBasicLabelData [GET] [CACHE] SUCCESS {}", cache.getData());
        return cache;
    }

    /**
     * 展示首页的门禁预览，访问量最多的五个门禁设备以及数量
     */
    @RequestMapping(value = "/device/count", method = RequestMethod.GET)
    public ResultModel showBasicDeviceCountData(BasicCountData basicCountData) {
        String communityId = basicCountData.getCommunityId();
        if (communityId == null) {
            return new ResultModel(AditumCode.ERROR, "communityId不能为空");
        }
        log.info("BasicCountData [GET] : {}", communityId);

        // 初始化缓存
        if (cache == null) {
            basicCountData = basicDataService.analysisBasicData(communityId);
            if (basicCountData == null) {
                log.warn("BasicCountData [GET] [INIT] FAILURE : {} -> {}", communityId);
                return new ResultModel(AditumCode.ERROR);
            }
            basicCountData.setCommunityId(communityId);
            cache = new ResultModel(AditumCode.OK, basicCountData);
            log.info("BasicCountData [GET] [INIT] SUCCESS {}", basicCountData);
            return cache;
        }

        // 缓存过期，更新
        long current = System.currentTimeMillis();
        if (current - cacheTime > VALID_TIME) {
            cacheTime = current;
            BasicCountData basicCountDataResult = basicDataService.analysisBasicData(communityId);
            if (basicCountDataResult == null) {
                log.warn("BasicCountData [GET] FAILURE : {} -> {}", communityId);
                return new ResultModel(AditumCode.ERROR);
            }
            basicCountDataResult.setCommunityId(communityId);
            log.info("BasicCountData [GET] SUCCESS : {} -> {}", communityId, basicCountDataResult);
            return new ResultModel(AditumCode.OK, basicCountDataResult);
        }

        log.info("BasicCountData [GET] [CACHE] SUCCESS {}", cache.getData());
        return cache;
    }

    /**
     * 展示首页的最近七天预览，最近七天每天访问量前三的设备（访问量前三的设备最近一周的访问量）
     */
    @RequestMapping(value = "/device/weekend", method = RequestMethod.GET)
    public ResultModel showBasicDeviceWeekendData(BasicCountData basicCountData) {
        String communityId = basicCountData.getCommunityId();
        if (communityId == null) {
            return new ResultModel(AditumCode.ERROR, "communityId不能为空");
        }
        log.info("BasicCountData [GET] : {}", communityId);

        // 初始化缓存
        if (cache == null) {
            basicCountData = basicDataService.analysisBasicData(communityId);
            if (basicCountData == null) {
                log.warn("BasicCountData [GET] [INIT] FAILURE : {} -> {}", communityId);
                return new ResultModel(AditumCode.ERROR);
            }
            basicCountData.setCommunityId(communityId);
            cache = new ResultModel(AditumCode.OK, basicCountData);
            log.info("BasicCountData [GET] [INIT] SUCCESS {}", basicCountData);
            return cache;
        }

        // 缓存过期，更新
        long current = System.currentTimeMillis();
        if (current - cacheTime > VALID_TIME) {
            cacheTime = current;
            BasicCountData basicCountDataResult = basicDataService.analysisBasicData(communityId);
            if (basicCountDataResult == null) {
                log.warn("BasicCountData [GET] FAILURE : {} -> {}", communityId);
                return new ResultModel(AditumCode.ERROR);
            }
            basicCountDataResult.setCommunityId(communityId);
            log.info("BasicCountData [GET] SUCCESS : {} -> {}", communityId, basicCountDataResult);
            return new ResultModel(AditumCode.OK, basicCountDataResult);
        }

        log.info("BasicCountData [GET] [CACHE] SUCCESS {}", cache.getData());
        return cache;
    }

    @Override
    public ResultModel get(BasicCountData basicCountData) {
        return null;
    }

    @Override
    public ResultModel post(BasicCountData basicCountData) {
        return null;
    }

    @Override
    public ResultModel update(BasicCountData basicCountData) {
        return null;
    }

    @Override
    public ResultModel delete(BasicCountData basicCountData) {
        return null;
    }
}
