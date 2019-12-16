package com.chang.happyshopping.domain;

import java.util.Date;


public class SeckillGoods {
  private Long id;
  private Long goodsId;
  private Integer stockCount;
  private Date startDate;
  private Date endDate;

  public Date getStartDate() {
    return startDate;
  }

  public void setStartDate(Date startDate) {
    this.startDate = startDate;
  }

  public Integer getStockCount() {

    return stockCount;
  }

  public void setStockCount(Integer stockCount) {
    this.stockCount = stockCount;
  }

  public Long getGoodsId() {

    return goodsId;
  }

  public void setGoodsId(Long goodsId) {
    this.goodsId = goodsId;
  }

  public Long getId() {

    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }
}
