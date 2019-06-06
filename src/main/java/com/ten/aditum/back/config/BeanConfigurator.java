package com.ten.aditum.back.config;

import com.ten.aditum.back.util.RedisConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
public class BeanConfigurator {

    /**
     * redis
     */
    @Bean
    public Jedis getJedis() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        // 设置 redis 连接池最大连接数量
        jedisPoolConfig.setMaxTotal(50);
        // 设置 redis 连接池最大空闲连接数量
        jedisPoolConfig.setMaxIdle(10);
        // 设置 redis 连接池最小空闲连接数量
        jedisPoolConfig.setMinIdle(1);
        RedisConnection redisConnection = new RedisConnection();
        redisConnection.setIp("47.106.11.84");
        redisConnection.setPort(6379);
//        redisConnection.setPwd("wangshihao");
        redisConnection.setClientName(Thread.currentThread().getName());
        redisConnection.setTimeOut(1000);
        redisConnection.setJedisPoolConfig(jedisPoolConfig);
        return redisConnection.getJedis();
    }

}
