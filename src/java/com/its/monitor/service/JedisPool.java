package com.its.monitor.service;

import redis.clients.jedis.JedisPoolConfig;

public class JedisPool  {

    public synchronized static JedisPool jedisPool(JedisPool jedisPool) {
        if (null != jedisPool)
            return jedisPool;
        synchronized (JedisPool.class) {
            JedisPoolConfig jpconfig = new JedisPoolConfig();
            jpconfig.setMaxTotal(200);
            jpconfig.setMaxIdle(50);
            jpconfig.setMinIdle(8);// 设置最小空闲数
            jpconfig.setMaxWaitMillis(10000);
            jpconfig.setTestOnBorrow(true);
            jpconfig.setTestOnReturn(true);
            jpconfig.setTestWhileIdle(true);
            // 表示idle object evitor两次扫描之间要sleep的毫秒数
            jpconfig.setTimeBetweenEvictionRunsMillis(30000);
            // 表示idle object evitor每次扫描的最多的对象数
            jpconfig.setNumTestsPerEvictionRun(10);
            // 表示一个对象至少停留在idle状态的最短时间，然后才能被idle object
            // evitor扫描并驱逐；这一项只有在timeBetweenEvictionRunsMillis大于0时才有意义
            jpconfig.setMinEvictableIdleTimeMillis(60000);
//            jedisPool = new JedisPool(new JedisPoolConfig(), host, port, timeout, auth, systemConfig.getRedisIndexDB());
        }
        return jedisPool;

    }
}