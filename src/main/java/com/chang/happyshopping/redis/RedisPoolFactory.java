package com.chang.happyshopping.redis;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Service
public class RedisPoolFactory {

  @Autowired
  RedisConfig redisConfig;

  @Bean
  public JedisPool jedisFactory(){
    JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
    jedisPoolConfig.setMaxIdle(redisConfig.getPoolMaxIdle());
    jedisPoolConfig.setMaxTotal(redisConfig.getPoolMaxTotal());
    jedisPoolConfig.setMaxWaitMillis(redisConfig.getPoolMaxWait()*1000);
    JedisPool jp = new JedisPool(jedisPoolConfig,redisConfig.getHost(),redisConfig.getPort(),
            redisConfig.getTimeout()*1000,redisConfig.getPassword(),0);
    System.out.println(redisConfig.getHost()+" "+redisConfig.getPort());
    return jp;
  }
}
