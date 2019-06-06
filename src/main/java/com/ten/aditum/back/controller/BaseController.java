package com.ten.aditum.back.controller;


import com.ten.aditum.back.model.AditumCode;
import com.ten.aditum.back.model.ResultModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;

import java.rmi.server.RMIClassLoader;

@Slf4j
@RestController
public abstract class BaseController<Entity> {

    public static final int NO_DELETED = 0;
    public static final int IS_DELETED = 1;

    /**
     * 缓存有效时间 12小时
     */
    protected static final int VALID_TIME = 60 * 60 * 12;

    @Autowired
    protected Jedis jedis;

    /* Restful Interface */

}
