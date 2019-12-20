package com.chang.happyshopping.controller;

import com.chang.happyshopping.domain.HappyShoppingUser;
import com.chang.happyshopping.domain.OrderInfo;
import com.chang.happyshopping.domain.SeckillOrder;
import com.chang.happyshopping.redis.RedisService;
import com.chang.happyshopping.result.CodeMsg;
import com.chang.happyshopping.result.Result;
import com.chang.happyshopping.service.GoodsService;
import com.chang.happyshopping.service.HappyShoppingUserService;
import com.chang.happyshopping.service.OrderService;
import com.chang.happyshopping.service.SeckillService;
import com.chang.happyshopping.vo.GoodsVo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/seckill")
public class SeckillController {

  @Autowired
  HappyShoppingUserService userService;

  @Autowired
  RedisService redisService;

  @Autowired
  GoodsService goodsService;

  @Autowired
  OrderService orderService;

  @Autowired
  SeckillService seckillService;

  @RequestMapping("/do_seckill")
  @ResponseBody
  public String doSeckill(Model model, HappyShoppingUser user,
                          @RequestParam("goodsId")long goodsId){
    if (user==null)
      return "login";
    //determine stock
    GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
    int stock = goods.getStockCount();
    if (stock<=0){
      model.addAttribute("errmsg",CodeMsg.STOCK_EMPTY.getMsg());
      return "miaosha_fail";
    }
    //is successful
    SeckillOrder order = orderService.getSeckillOrderByUserIdGoodsId(user.getId(),goods.getId());
    if (order!=null){
      model.addAttribute("errmsg",CodeMsg.REPEAT_PURCHASE);
      return "miaosha_fail";
    }

    //1.decrease stock 2. make order 3. write order in a transaction
    OrderInfo orderInfo = seckillService.kill(user,goods);
    model.addAttribute("orderInfo",orderInfo);
    model.addAttribute("goods",goods);
    return "order_detail";
  }

  // get: every time result is equali : post:influence the data of server
  @RequestMapping(value = "/do_seckill2",method = RequestMethod.POST)
  @ResponseBody
  public Result<OrderInfo> doSeckill( HappyShoppingUser user,
                                     @RequestParam("goodsId")long goodsId){
    if (user==null)
      return Result.error(CodeMsg.SERVER_ERROR);
    //determine stock
    GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
    int stock = goods.getStockCount();
    if (stock<=0){
      return Result.error(CodeMsg.STOCK_EMPTY);
    }
    //is successful
    SeckillOrder order = orderService.getSeckillOrderByUserIdGoodsId(user.getId(),goods.getId());
    if (order!=null){
      return Result.error(CodeMsg.REPEAT_PURCHASE);
    }

    //1.decrease stock 2. make order 3. write order in a transaction
    OrderInfo orderInfo = seckillService.kill(user,goods);

    return Result.success(orderInfo);
  }
}
