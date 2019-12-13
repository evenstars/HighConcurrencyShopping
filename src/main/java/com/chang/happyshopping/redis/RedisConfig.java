package com.chang.happyshopping.redis;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "redis")
public class RedisConfig {
  private String host;
  private int port;
  private int timeout; // second
  private String password;
  private int poolMaxTotal;
  private int poolMaxIdle;
  private int poolMaxWait;//second


  public int getPoolMaxWait() {
    return poolMaxWait;
  }

  public void setPoolMaxWait(int poolMaxWait) {
    this.poolMaxWait = poolMaxWait;
  }

  public int getPoolMaxIdle() {

    return poolMaxIdle;
  }

  public void setPoolMaxIdle(int poolMaxIdle) {
    this.poolMaxIdle = poolMaxIdle;
  }

  public int getPoolMaxTotal() {

    return poolMaxTotal;
  }

  public void setPoolMaxTotal(int poolMaxTotal) {
    this.poolMaxTotal = poolMaxTotal;
  }

  public String getPassword() {

    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public int getPort() {

    return port;
  }

  public void setPort(int port) {
    this.port = port;
  }

  public int getTimeout() {
    return timeout;
  }

  public void setTimeout(int timeout) {
    this.timeout = timeout;
  }

  public String getHost() {
    return host;
  }

  public void setHost(String host) {
    this.host = host;
  }
}
