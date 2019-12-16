package com.chang.happyshopping.redis;

import com.alibaba.fastjson.JSON;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Service
public class RedisService {

  @Autowired
  JedisPool jedisPool;

  public <T> T get(KeyPrefix prefix,String key,Class<T> clazz){
    Jedis jedis = null;
    try {
      jedis = jedisPool.getResource();
      String realKey = prefix.getPrefix()+key;
      String str = jedis.get(realKey);
      T t = stringToBean(str,clazz);
      return t;
    }
    finally {
      returnToPool(jedis);
    }
  }

  public <T> boolean exists(KeyPrefix prefix,String key){
    Jedis jedis = null;
    try {
      jedis = jedisPool.getResource();
      String realKey = prefix.getPrefix()+key;
      return jedis.exists(realKey);
    }
    finally {
      returnToPool(jedis);
    }
  }

  public <T> boolean set(KeyPrefix prefix,String key,T value){
    Jedis jedis = null;
    try {
      jedis = jedisPool.getResource();
      String str = beanToString(value);
      if (str == null || str.isEmpty())
        return false;
      String realKey = prefix.getPrefix()+key;
      System.out.println("key in redis: "+realKey);
      int expireTime = prefix.expireSeconds();
      if (expireTime==0)
        jedis.set(realKey,str);
      else
        jedis.setex(realKey,expireTime,str);
      return true;
    }
    finally {
      returnToPool(jedis);
    }
  }

  public Long increase(KeyPrefix prefix,String key){
    Jedis jedis = null;
    try {
      jedis = jedisPool.getResource();
      String realKey = prefix.getPrefix()+key;
      return jedis.incr(realKey);
    }
    finally {
      returnToPool(jedis);
    }
  }

  public Long decrease(KeyPrefix prefix,String key){
    Jedis jedis = null;
    try {
      jedis = jedisPool.getResource();
      String realKey = prefix.getPrefix()+key;
      return jedis.decr(realKey);
    }
    finally {
      returnToPool(jedis);
    }
  }

  private <T> String beanToString(T value) {
    if (value == null)
      return null;
    Class<?> clazz = value.getClass();
    if (clazz == int.class || clazz==Integer.class){
      return ""+value;
    }
    else if (clazz == String.class){
      return (String)value;
    }
    else if (clazz == long.class || clazz == Long.class){
      return ""+value;
    }
    else{
      return JSON.toJSONString(value);
    }
  }

  private <T> T stringToBean(String str,Class<T> clazz) {
    if (str==null || str.isEmpty()||clazz==null)
      return null;
    if (clazz == int.class || clazz==Integer.class){
      return (T)Integer.valueOf(str);
    }
    else if (clazz == String.class){
      return (T)str;
    }
    else if (clazz == long.class || clazz == Long.class){
      return (T)Long.valueOf(str);
    }
    else{
      return JSON.toJavaObject(JSON.parseObject(str),clazz);
    }
  }

  private void returnToPool(Jedis jedis){
    if (jedis==null)
      return;
    jedis.close();
  }

}
