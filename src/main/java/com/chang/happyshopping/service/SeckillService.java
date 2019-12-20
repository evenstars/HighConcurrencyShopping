package com.chang.happyshopping.service;

import com.chang.happyshopping.dao.GoodsDAO;
import com.chang.happyshopping.domain.Goods;
import com.chang.happyshopping.domain.HappyShoppingUser;
import com.chang.happyshopping.domain.OrderInfo;
import com.chang.happyshopping.vo.GoodsVo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SeckillService {

  @Autowired
  GoodsService goodsService;

  @Autowired
  OrderService orderService;

  @Transactional
  public OrderInfo kill(HappyShoppingUser user, GoodsVo goods) {
    //decrease stock
    goodsService.reduceStock(goods);
    Goods good = new Goods();
    good.setId(goods.getId());
    goods.setGoodsStock(goods.getStockCount()-1);

    return orderService.createOrder(user,goods);
  }
}
