package com.ten.aditum.back.util;

import org.junit.Assert;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPoolConfig;

/**
 * RedisConnection Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>六月 6, 2019</pre>
 */
public class RedisConnectionTest {

    private RedisConnection redisConnection;

    @Before
    public void before() throws Exception {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        // 设置 redis 连接池最大连接数量
        jedisPoolConfig.setMaxTotal(50);
        // 设置 redis 连接池最大空闲连接数量
        jedisPoolConfig.setMaxIdle(10);
        // 设置 redis 连接池最小空闲连接数量
        jedisPoolConfig.setMinIdle(1);
        redisConnection = new RedisConnection();
        redisConnection.setIp("47.106.11.84");
        redisConnection.setPort(6379);
        redisConnection.setPwd("wangshihao");
        redisConnection.setClientName(Thread.currentThread().getName());
        redisConnection.setTimeOut(60000);
        redisConnection.setJedisPoolConfig(jedisPoolConfig);
    }

    @After
    public void after() throws Exception {
    }

    /**
     * Method: getJedis()
     */
    @Test
    public void testGetJedis() throws Exception {
        Jedis jedis = redisConnection.getJedis();
        try {
            jedis.select(1);
//            jedis.set("name", "grace");
            System.out.println(jedis.get("name1"));
            Assert.assertEquals("grace", jedis.get("name"));
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

} 
