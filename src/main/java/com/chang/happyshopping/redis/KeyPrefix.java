package com.chang.happyshopping.redis;

public interface KeyPrefix {

  int expireSeconds();

  String getPrefix();

}
