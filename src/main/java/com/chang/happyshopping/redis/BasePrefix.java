package com.chang.happyshopping.redis;

public abstract class BasePrefix implements KeyPrefix {

  private int expireSeconds;

  private String prefix;

  public BasePrefix(int expireSeconds,String prefix){
    this.expireSeconds = expireSeconds;
    this.prefix = prefix;
  }

  public BasePrefix(String prefix){
    this(0,prefix);
  }

  @Override
  public int expireSeconds() {
    // 0 - never expire
    return expireSeconds;
  }

  @Override
  public String getPrefix() {
    String className = getClass().getSimpleName();
    return className +":"+ prefix;
  }
}
