package com.ten.aditum.back.controller;

import com.ten.aditum.back.config.ApolloConfigBean;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Apollo 配置检查控制器
 */
@Slf4j
@RestController(value = "/config")
public class ConfigController {

    private final ApolloConfigBean apolloConfigBean;

    @Autowired
    public ConfigController(ApolloConfigBean apolloConfigBean) {
        this.apolloConfigBean = apolloConfigBean;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String config() {
        log.info(String.valueOf(apolloConfigBean));
        return String.valueOf(apolloConfigBean);
    }

}
