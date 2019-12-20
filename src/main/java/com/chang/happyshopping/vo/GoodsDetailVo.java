package com.chang.happyshopping.vo;

import com.chang.happyshopping.domain.HappyShoppingUser;



public class GoodsDetailVo {
  private int seckillStatus;
  private int remainSeconds;
  private GoodsVo goods;
  private HappyShoppingUser user;

  public HappyShoppingUser getUser() {
    return user;
  }

  public void setUser(HappyShoppingUser user) {
    this.user = user;
  }

  public GoodsVo getGoods() {

    return goods;
  }

  public void setGoods(GoodsVo goods) {
    this.goods = goods;
  }

  public int getRemainSeconds() {

    return remainSeconds;
  }

  public void setRemainSeconds(int remainSeconds) {
    this.remainSeconds = remainSeconds;
  }

  public int getSeckillStatus() {

    return seckillStatus;
  }

  public void setSeckillStatus(int seckillStatus) {
    this.seckillStatus = seckillStatus;
  }
}
