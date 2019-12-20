package com.chang.happyshopping.redis;

public class GoodsKey extends BasePrefix {
  public GoodsKey(int expire,String prefix) {
    super(expire,prefix);
  }

  public static GoodsKey getGoodsList = new GoodsKey(60,"gl");
  public static GoodsKey getGoodsDetail = new GoodsKey(60,"gt");
}
