package com.chang.happyshopping.redis;

public class SeckillKey extends BasePrefix {
  public SeckillKey(int expire,String prefix) {
    super(expire,prefix);
  }

  public static SeckillKey isGoodOver = new SeckillKey(0,"go");

  public static SeckillKey getSeckillPath = new SeckillKey(60,"gp");

  public static SeckillKey getSeckillVerifyCode = new SeckillKey(300,"vc");

}
