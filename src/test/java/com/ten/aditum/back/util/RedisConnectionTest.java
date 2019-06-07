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

    private RedisConnection jedis;

    @Before
    public void before() throws Exception {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        // ���� redis ���ӳ������������
        jedisPoolConfig.setMaxTotal(50);
        // ���� redis ���ӳ���������������
        jedisPoolConfig.setMaxIdle(10);
        // ���� redis ���ӳ���С������������
        jedisPoolConfig.setMinIdle(1);
        jedis = new RedisConnection();
        jedis.setIp("47.106.11.84");
        jedis.setPort(6379);
        jedis.setPwd("wangshihao");
        jedis.setClientName(Thread.currentThread().getName());
        jedis.setTimeOut(60000);
        jedis.setJedisPoolConfig(jedisPoolConfig);
        jedis.buildJedis();
    }

    @After
    public void after() throws Exception {
    }

    /**
     * Method: buildJedis()
     */
    @Test
    public void testGetJedis() throws Exception {
//        try {
//            jedis.select(1);
////            jedis.set("name", "grace");
//            System.out.println(jedis.get("name1"));
//            Assert.assertEquals("grace", jedis.get("name"));
//        } finally {
//            if (jedis != null) {
//                jedis.close();
//            }
//        }
    }

} 
