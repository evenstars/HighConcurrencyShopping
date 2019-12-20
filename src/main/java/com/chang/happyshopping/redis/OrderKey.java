package com.chang.happyshopping.redis;

public class OrderKey extends BasePrefix {
  public OrderKey(int expireSeconds, String prefix) {
    super(expireSeconds, prefix);
  }

  public static OrderKey getSeckillOrderByUidGid = new OrderKey(0,"soug");
}
