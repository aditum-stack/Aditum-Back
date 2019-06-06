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
 * @since <pre>���� 6, 2019</pre>
 */
public class RedisConnectionTest {

    private RedisConnection redisConnection;

    @Before
    public void before() throws Exception {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        // ���� redis ���ӳ������������
        jedisPoolConfig.setMaxTotal(50);
        // ���� redis ���ӳ���������������
        jedisPoolConfig.setMaxIdle(10);
        // ���� redis ���ӳ���С������������
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
