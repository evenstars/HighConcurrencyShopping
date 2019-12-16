package com.chang.happyshopping.vo;

import com.chang.happyshopping.domain.Goods;

import java.util.Date;


public class GoodsVo extends Goods {
  private Double miaoshaPrice;

  public Double getMiaoshaPrice() {
    return miaoshaPrice;
  }

  public void setMiaoshaPrice(Double miaoshaPrice) {
    this.miaoshaPrice = miaoshaPrice;
  }

  private Integer stockCount;
  private Date startDate;
  private Date endDate;

  public Date getEndDate() {
    return endDate;
  }

  public void setEndDate(Date endDate) {
    this.endDate = endDate;
  }

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
}
