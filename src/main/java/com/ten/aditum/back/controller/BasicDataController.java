package com.ten.aditum.back.controller;

import com.ten.aditum.back.model.AditumCode;
import com.ten.aditum.back.model.ResultModel;
import com.ten.aditum.back.service.*;
import com.ten.aditum.back.vo.BasicCountData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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
        log.debug("BasicCountData [GET] : {}", communityId);

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
            log.debug("BasicCountData [GET] SUCCESS : {} -> {}", communityId, basicCountDataResult);
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
