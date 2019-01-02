package com.ten.aditum.back.config;

import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * Apollo 配置获取客户端
 */
@Configuration
@EnableApolloConfig
public class ApolloConfigBean {

    @Value("${timeout:20}")
    private int timeout;

    public int getTimeout() {
        return timeout;
    }

    @Override
    public String toString() {
        return "ApolloConfigBean{" +
                "timeout=" + timeout +
                '}';
    }
}
