package com.ten.aditum.back.util;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisConnection {
    /**
     * redis 连接池配置信息
     */
    private JedisPoolConfig jedisPoolConfig;

    private String ip;
    private Integer port;
    private String pwd;
    private Integer timeOut;

    /**
     * redis 连接客户端名称
     */
    private String clientName = null;

    private JedisPool jedisPool;

    public void setJedisPoolConfig(JedisPoolConfig jedisPoolConfig) {
        this.jedisPoolConfig = jedisPoolConfig;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public void setTimeOut(Integer timeOut) {
        this.timeOut = timeOut;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    private void buildConnection() {
        if (jedisPool == null) {
            if (jedisPoolConfig == null) {
                jedisPool = new JedisPool(new JedisPoolConfig(), ip, port, timeOut);
            } else {
                jedisPool = new JedisPool(jedisPoolConfig, ip, port, timeOut);
            }
        }
    }

    public Jedis getJedis() {
        buildConnection();
        if (jedisPool != null) {
            return jedisPool.getResource();
        }
        return null;
    }
}
