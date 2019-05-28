package com.ten.aditum.back.controller;

import com.ten.aditum.back.model.AditumCode;
import com.ten.aditum.back.model.ResultModel;
import com.ten.aditum.back.service.BasicDataService;
import com.ten.aditum.back.vo.BasicCountData;
import com.ten.aditum.back.vo.BasicDeviceCountData;
import com.ten.aditum.back.vo.BasicDeviceWeekendData;
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
     * 缓存有效时间 12小时
     */
    private static final long VALID_TIME = 1000 * 60 * 60 * 12;

    /**
     * showBasicCountData缓存
     */
    private ResultModel showBasicCountDataCache;
    private long showBasicCountDataCacheTime = System.currentTimeMillis();

    /**
     * 展示四个基本属性的总数，以及最近七天的每天的总量和增量
     */
    @RequestMapping(value = "count", method = RequestMethod.GET)
    public ResultModel showBasicCountData(BasicCountData basicCountData) {
        String communityId = basicCountData.getCommunityId();
        if (communityId == null) {
            return new ResultModel(AditumCode.ERROR, "communityId不能为空");
        }
        log.info("showBasicCountData [GET] : {}", communityId);

        // 初始化缓存
        if (showBasicCountDataCache == null) {
            basicCountData = basicDataService.analysisBasicData(communityId);
            if (basicCountData == null) {
                log.warn("showBasicCountData [GET] [INIT] FAILURE : {} -> {}", communityId);
                return new ResultModel(AditumCode.ERROR);
            }
            basicCountData.setCommunityId(communityId);
            showBasicCountDataCache = new ResultModel(AditumCode.OK, basicCountData);
            log.info("showBasicCountData [GET] [INIT] SUCCESS {}", basicCountData);
            return showBasicCountDataCache;
        }

        // 缓存过期，更新
        long current = System.currentTimeMillis();
        if (current - showBasicCountDataCacheTime > VALID_TIME) {
            showBasicCountDataCacheTime = current;
            BasicCountData basicCountDataResult = basicDataService.analysisBasicData(communityId);
            if (basicCountDataResult == null) {
                log.warn("showBasicCountData [GET] FAILURE : {} -> {}", communityId);
                return new ResultModel(AditumCode.ERROR);
            }
            basicCountDataResult.setCommunityId(communityId);
            log.info("showBasicCountData [GET] SUCCESS : {} -> {}", communityId, basicCountDataResult);
            showBasicCountDataCache = new ResultModel(AditumCode.OK, basicCountDataResult);
            return showBasicCountDataCache;
        }

        log.info("showBasicCountData [GET] [CACHE] SUCCESS {}", showBasicCountDataCache.getData());
        return showBasicCountDataCache;
    }

    /**
     * showBasicLabelData缓存
     */
    private ResultModel showBasicLabelDataCache;
    private long showBasicLabelDataCacheTime = System.currentTimeMillis();
    private static final int MOST_LABEL_COUNT = 5;

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
        if (showBasicLabelDataCache == null) {
            BasicLabelData basicLabelData =
                    basicDataService.analysisBasicLabelCount(communityId, MOST_LABEL_COUNT);
            if (basicLabelData == null) {
                log.warn("showBasicLabelData [GET] [INIT] FAILURE : {} -> {}", communityId);
                return new ResultModel(AditumCode.ERROR);
            }
            showBasicLabelDataCache = new ResultModel(AditumCode.OK, basicLabelData);
            log.info("showBasicLabelData [GET] [INIT] SUCCESS {}", basicLabelData);
            return showBasicLabelDataCache;
        }

        // 缓存过期，更新
        long current = System.currentTimeMillis();
        if (current - showBasicLabelDataCacheTime > VALID_TIME) {
            showBasicLabelDataCacheTime = current;
            BasicLabelData basicLabelData =
                    basicDataService.analysisBasicLabelCount(communityId, MOST_LABEL_COUNT);
            if (basicLabelData == null) {
                log.warn("showBasicLabelData [GET] FAILURE : {} -> {}", communityId);
                return new ResultModel(AditumCode.ERROR);
            }
            log.info("showBasicLabelData [GET] SUCCESS : {} -> {}", communityId, basicLabelData);
            showBasicLabelDataCache = new ResultModel(AditumCode.OK, basicLabelData);
            return showBasicLabelDataCache;
        }

        log.info("showBasicLabelData [GET] [CACHE] SUCCESS {}", showBasicLabelDataCache.getData());
        return showBasicLabelDataCache;
    }

    /**
     * showBasicDeviceCountData缓存
     */
    private ResultModel showBasicDeviceCountDataCache;
    private long showBasicDeviceCountDataCacheTime = System.currentTimeMillis();
    private static final int MOST_DEVICE_COUNT = 7;

    /**
     * 展示首页的门禁预览，访问量最多的MOST_DEVICE_COUNT个门禁设备以及数量
     */
    @RequestMapping(value = "/device/count", method = RequestMethod.GET)
    public ResultModel showBasicDeviceCountData(BasicCountData basicCountData) {
        String communityId = basicCountData.getCommunityId();
        if (communityId == null) {
            return new ResultModel(AditumCode.ERROR, "communityId不能为空");
        }
        log.info("showBasicDeviceCountData [GET] : {}", communityId);

        // 初始化缓存
        if (showBasicDeviceCountDataCache == null) {
            BasicDeviceCountData basicDeviceCountData =
                    basicDataService.analysisBasicDeviceData(communityId, MOST_DEVICE_COUNT);
            if (basicDeviceCountData == null) {
                log.warn("showBasicDeviceCountData [GET] [INIT] FAILURE : {} -> {}", communityId);
                return new ResultModel(AditumCode.ERROR);
            }
            showBasicDeviceCountDataCache = new ResultModel(AditumCode.OK, basicDeviceCountData);
            log.info("showBasicDeviceCountData [GET] [INIT] SUCCESS {}", basicDeviceCountData);
            return showBasicDeviceCountDataCache;
        }

        // 缓存过期，更新
        long current = System.currentTimeMillis();
        if (current - showBasicDeviceCountDataCacheTime > VALID_TIME) {
            showBasicDeviceCountDataCacheTime = current;
            BasicDeviceCountData basicDeviceCountData =
                    basicDataService.analysisBasicDeviceData(communityId, MOST_DEVICE_COUNT);
            if (basicDeviceCountData == null) {
                log.warn("showBasicDeviceCountData [GET] FAILURE : {} -> {}", communityId);
                return new ResultModel(AditumCode.ERROR);
            }
            log.info("showBasicDeviceCountData [GET] SUCCESS : {} -> {}", communityId, basicDeviceCountData);
            showBasicDeviceCountDataCache = new ResultModel(AditumCode.OK, basicDeviceCountData);
            return showBasicDeviceCountDataCache;
        }

        log.info("showBasicDeviceCountData [GET] [CACHE] SUCCESS {}", showBasicDeviceCountDataCache.getData());
        return showBasicDeviceCountDataCache;
    }

    /**
     * showBasicDeviceWeekendData缓存
     */
    private ResultModel showBasicDeviceWeekendDataCache;
    private long showBasicDeviceWeekendDataCacheTime = System.currentTimeMillis();
    private static final int MOST_WEEKEND_COUNT = 3;

    /**
     * 展示首页的最近七天预览，最近七天每天访问量前MOST_WEEKEND_COUNT的设备（访问量前MOST_WEEKEND_COUNT的设备最近一周的访问量）
     */
    @RequestMapping(value = "/device/weekend", method = RequestMethod.GET)
    public ResultModel showBasicDeviceWeekendData(BasicCountData basicCountData) {
        String communityId = basicCountData.getCommunityId();
        if (communityId == null) {
            return new ResultModel(AditumCode.ERROR, "communityId不能为空");
        }
        log.info("showBasicDeviceWeekendData [GET] : {}", communityId);

        // 初始化缓存
        if (showBasicDeviceWeekendDataCache == null) {
            BasicDeviceWeekendData basicDeviceWeekendData =
                    basicDataService.analysisBasicWeekendData(communityId, MOST_WEEKEND_COUNT);
            if (basicDeviceWeekendData == null) {
                log.warn("showBasicDeviceWeekendData [GET] [INIT] FAILURE : {} -> {}", communityId);
                return new ResultModel(AditumCode.ERROR);
            }
            showBasicDeviceWeekendDataCache = new ResultModel(AditumCode.OK, basicDeviceWeekendData);
            log.info("showBasicDeviceWeekendData [GET] [INIT] SUCCESS {}", basicDeviceWeekendData);
            return showBasicDeviceWeekendDataCache;
        }

        // 缓存过期，更新
        long current = System.currentTimeMillis();
        if (current - showBasicDeviceWeekendDataCacheTime > VALID_TIME) {
            showBasicDeviceWeekendDataCacheTime = current;
            BasicDeviceWeekendData basicDeviceWeekendData =
                    basicDataService.analysisBasicWeekendData(communityId, MOST_WEEKEND_COUNT);
            if (basicDeviceWeekendData == null) {
                log.warn("showBasicDeviceWeekendData [GET] FAILURE : {} -> {}", communityId);
                return new ResultModel(AditumCode.ERROR);
            }
            log.info("showBasicDeviceWeekendData [GET] SUCCESS : {} -> {}", communityId, basicDeviceWeekendData);
            showBasicDeviceWeekendDataCache = new ResultModel(AditumCode.OK, basicDeviceWeekendData);
            return showBasicDeviceWeekendDataCache;
        }

        log.info("showBasicDeviceWeekendData [GET] [CACHE] SUCCESS {}", showBasicDeviceWeekendDataCache.getData());
        return showBasicDeviceWeekendDataCache;
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
