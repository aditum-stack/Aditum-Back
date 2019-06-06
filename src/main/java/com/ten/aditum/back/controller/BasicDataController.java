package com.ten.aditum.back.controller;

import com.alibaba.fastjson.JSON;
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
     * 展示四个基本属性的总数，以及最近七天的每天的总量和增量
     */
    @RequestMapping(value = "count", method = RequestMethod.GET)
    public ResultModel showBasicCountData(BasicCountData basicCountData) {
        String communityId = basicCountData.getCommunityId();
        if (communityId == null) {
            return new ResultModel(AditumCode.ERROR, "communityId不能为空");
        }
        log.info("showBasicCountData [GET] : {}", communityId);

        String key = "showBasicCountData" + communityId;
        String originValue = jedis.get(key);
        if (originValue == null) {
            basicCountData = basicDataService.analysisBasicData(communityId);
            if (basicCountData == null) {
                log.warn("showBasicCountData [GET] [INIT] FAILURE : {} -> {}", communityId);
                return new ResultModel(AditumCode.ERROR);
            } else {
                ResultModel cache = new ResultModel(AditumCode.OK, basicCountData);
                String value = JSON.toJSONString(cache);
                jedis.setex(key, VALID_TIME, value);
                log.info("showBasicCountData [GET] [INIT] SUCCESS {}", cache);
                return cache;
            }
        } else {
            ResultModel cache = JSON.parseObject(originValue, ResultModel.class);
            log.info("showBasicCountData [GET] [CACHE] SUCCESS {}", cache);
            return cache;
        }
    }

    private static final int MOST_LABEL_COUNT = 5;

    /**
     * 展示首页的标签预览，数量最多的MOST_LABEL_COUNT个标签和相对应的数量
     */
    @RequestMapping(value = "/label/count", method = RequestMethod.GET)
    public ResultModel showBasicLabelData(BasicCountData basicCountData) {
        String communityId = basicCountData.getCommunityId();
        if (communityId == null) {
            return new ResultModel(AditumCode.ERROR, "communityId不能为空");
        }
        log.info("showBasicLabelData [GET] : {}", communityId);

        String key = "showBasicLabelData" + communityId;
        String originValue = jedis.get(key);
        if (originValue == null) {
            BasicLabelData basicLabelData =
                    basicDataService.analysisBasicLabelCount(communityId, MOST_LABEL_COUNT);
            if (basicLabelData == null) {
                log.warn("showBasicLabelData [GET] [INIT] FAILURE : {} -> {}", communityId);
                return new ResultModel(AditumCode.ERROR);
            } else {
                ResultModel cache = new ResultModel(AditumCode.OK, basicLabelData);
                String value = JSON.toJSONString(cache);
                jedis.setex(key, VALID_TIME, value);
                log.info("showBasicLabelData [GET] [INIT] SUCCESS {}", cache);
                return cache;
            }
        } else {
            ResultModel cache = JSON.parseObject(originValue, ResultModel.class);
            log.info("showBasicLabelData [GET] [CACHE] SUCCESS {}", cache);
            return cache;
        }
    }

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

        String key = "showBasicDeviceCountData" + communityId;
        String originValue = jedis.get(key);
        if (originValue == null) {
            BasicDeviceCountData basicDeviceCountData =
                    basicDataService.analysisBasicDeviceData(communityId, MOST_DEVICE_COUNT);
            if (basicDeviceCountData == null) {
                log.warn("showBasicDeviceCountData [GET] [INIT] FAILURE : {} -> {}", communityId);
                return new ResultModel(AditumCode.ERROR);
            } else {
                ResultModel cache = new ResultModel(AditumCode.OK, basicDeviceCountData);
                String value = JSON.toJSONString(cache);
                jedis.setex(key, VALID_TIME, value);
                log.info("showBasicDeviceCountData [GET] [INIT] SUCCESS {}", cache);
                return cache;
            }
        } else {
            ResultModel cache = JSON.parseObject(originValue, ResultModel.class);
            log.info("showBasicDeviceCountData [GET] [CACHE] SUCCESS {}", cache);
            return cache;
        }
    }

    private static final int MOST_WEEKEND_COUNT = 3;

    /**
     * 展示首页的最近七天预览，最近七天每天访问量前MOST_WEEKEND_COUNT的设备
     * （访问量前MOST_WEEKEND_COUNT的设备最近一周的访问量）
     */
    @RequestMapping(value = "/device/weekend", method = RequestMethod.GET)
    public ResultModel showBasicDeviceWeekendData(BasicCountData basicCountData) {
        String communityId = basicCountData.getCommunityId();
        if (communityId == null) {
            return new ResultModel(AditumCode.ERROR, "communityId不能为空");
        }
        log.info("showBasicDeviceWeekendData [GET] : {}", communityId);

        String key = "showBasicDeviceWeekendData" + communityId;
        String originValue = jedis.get(key);
        if (originValue == null) {
            BasicDeviceWeekendData basicDeviceWeekendData =
                    basicDataService.analysisBasicWeekendData(communityId, MOST_WEEKEND_COUNT);
            if (basicDeviceWeekendData == null) {
                log.warn("showBasicDeviceWeekendData [GET] [INIT] FAILURE : {} -> {}", communityId);
                return new ResultModel(AditumCode.ERROR);
            } else {
                ResultModel cache = new ResultModel(AditumCode.OK, basicDeviceWeekendData);
                String value = JSON.toJSONString(cache);
                jedis.setex(key, VALID_TIME, value);
                log.info("showBasicDeviceWeekendData [GET] [INIT] SUCCESS {}", cache);
                return cache;
            }
        } else {
            ResultModel cache = JSON.parseObject(originValue, ResultModel.class);
            log.info("showBasicDeviceWeekendData [GET] [CACHE] SUCCESS {}", cache);
            return cache;
        }
    }
}
