package com.chang.happyshopping.rabbitmq;

import com.chang.happyshopping.domain.HappyShoppingUser;

public class SeckillMessage {
  private HappyShoppingUser user;
  private long goodsId;

  public long getGoodsId() {
    return goodsId;
  }

  public void setGoodsId(long goodsId) {
    this.goodsId = goodsId;
  }

  public HappyShoppingUser getUser() {

    return user;
  }

  public void setUser(HappyShoppingUser user) {
    this.user = user;
  }
}
