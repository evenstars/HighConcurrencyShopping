package com.chang.happyshopping.service;

import com.chang.happyshopping.dao.GoodsDAO;
import com.chang.happyshopping.dao.OrderDao;
import com.chang.happyshopping.domain.HappyShoppingUser;
import com.chang.happyshopping.domain.OrderInfo;
import com.chang.happyshopping.domain.SeckillOrder;
import com.chang.happyshopping.redis.OrderKey;
import com.chang.happyshopping.redis.RedisService;
import com.chang.happyshopping.vo.GoodsVo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class OrderService {

  @Autowired
  OrderDao orderDao;

  @Autowired
  RedisService redisService;

  public SeckillOrder getSeckillOrderByUserIdGoodsId(Long userId, Long goodsId) {
//    return orderDao.getSeckillOrderByUserIdGoodsId(userId,goodsId);
    SeckillOrder order = redisService.get(OrderKey.getSeckillOrderByUidGid,userId+"_"+goodsId,SeckillOrder.class);
    return order;
  }

  @Transactional
  public OrderInfo createOrder(HappyShoppingUser user, GoodsVo goods) {
    OrderInfo orderInfo = new OrderInfo();
    orderInfo.setCreateDate(new Date());
    orderInfo.setDeliveryAddrId(0L);
    orderInfo.setGoodsCount(1);
    orderInfo.setGoodsId(goods.getId());
    orderInfo.setGoodsName(goods.getGoodsName());
    orderInfo.setGoodsPrice(goods.getMiaoshaPrice());
    orderInfo.setOrderChannel(1);
    orderInfo.setStatus(0);
    orderInfo.setUserId(user.getId());
    orderDao.insert(orderInfo);

    SeckillOrder seckillOrder = new SeckillOrder();
    seckillOrder.setGoodsId(goods.getId());
    seckillOrder.setOrderId(orderInfo.getId());
    seckillOrder.setUserId(user.getId());
    orderDao.insertSeckillOrder(seckillOrder);

    redisService.set(OrderKey.getSeckillOrderByUidGid,user.getId()+"_"+goods.getId(),seckillOrder);
    return orderInfo;
  }

  public OrderInfo getOrderById(long orderId) {
    return orderDao.getOrderById(orderId);
  }
}
