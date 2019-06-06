package com.ten.aditum.back.controller;

import com.ten.aditum.back.model.AditumCode;
import com.ten.aditum.back.model.ResultModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(value = "/common")
public class CommonController extends BaseController {

    /**
     * 清除redis所有缓存
     */
    @RequestMapping(value = "/redis/clear", method = RequestMethod.GET)
    public ResultModel redisClear() {
        log.warn("清除redis所有缓存");
        jedis.flushAll();
        return new ResultModel(AditumCode.OK);
    }

}
