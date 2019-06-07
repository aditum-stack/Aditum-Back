package com.ten.aditum.back.util;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Slf4j
public class RedisConnection {
    @Setter
    private String ip;
    @Setter
    private Integer port;
    @Setter
    private String pwd;
    @Setter
    private Integer timeOut;
    /**
     * redis 连接池配置信息
     */
    @Setter
    private JedisPoolConfig jedisPoolConfig;
    /**
     * redis 连接客户端名称
     */
    @Setter
    private String clientName;

    private JedisPool jedisPool;

    private void buildConnection() {
        if (jedisPool == null) {
            if (jedisPoolConfig == null) {
                jedisPool = new JedisPool(new JedisPoolConfig(), ip, port, timeOut);
            } else {
                jedisPool = new JedisPool(jedisPoolConfig, ip, port, timeOut);
            }
        }
    }

    public void buildJedis() {
        buildConnection();
        if (jedisPool == null) {
            throw new RuntimeException("redisPool初始化失败");
        }
    }

    public String get(String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            String value = jedis.get(key);
            log.info("redis [GET] \\{key: {}, value: {}\\}", key, value);
            return value;
        } catch (Exception e) {
            log.error("redis [GET] FAILURE key: {}", key);
            return null;
        }
    }

    public String setex(String key, int timeOutS, String value) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.setex(key, timeOutS, value);
        } catch (Exception e) {
            log.error("redis [setEx] FAILURE \\{key: {}, value: {}\\}", key, value);
            return null;
        }
    }

    public void flushAll() {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.flushAll();
        } catch (Exception e) {
            log.error("redis [flushAll] FAILURE");
        }
    }
}
