package com.chang.happyshopping.redis;

public class SeckillUserKey extends BasePrefix{

  public static final int TOKEN_EXPIRE = 3600*24*2;
  public SeckillUserKey(int expire,String prefix) {
    super(expire,prefix);
  }

  public static SeckillUserKey token = new SeckillUserKey(TOKEN_EXPIRE,"tk");

  public static SeckillUserKey getById = new SeckillUserKey(0,"id");
}
