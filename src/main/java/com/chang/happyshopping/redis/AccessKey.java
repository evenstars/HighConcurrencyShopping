package com.chang.happyshopping.redis;

public class AccessKey extends BasePrefix{
  private AccessKey(int expireSeconds, String prefix) {
    super(expireSeconds, prefix);
  }

  public static AccessKey access = new AccessKey(5,"access");

  public static AccessKey withExpire(int expire){
    return new AccessKey(expire,"access");
  }
}
