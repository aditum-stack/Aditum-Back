package com.ten.aditum.back.controller;


import com.ten.aditum.back.util.RedisConnection;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public abstract class BaseController<Entity> {

    public static final int NO_DELETED = 0;
    public static final int IS_DELETED = 1;

    /**
     * 缓存有效时间 24小时
     */
    protected static final int VALID_TIME = 60 * 60 * 24;

    @Autowired
    protected RedisConnection jedis;

    /* Restful Interface */

}
