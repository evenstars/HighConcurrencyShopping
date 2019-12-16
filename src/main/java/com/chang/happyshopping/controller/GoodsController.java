package com.chang.happyshopping.controller;

import com.chang.happyshopping.domain.HappyShoppingUser;
import com.chang.happyshopping.redis.RedisService;
import com.chang.happyshopping.redis.SeckillUserKey;
import com.chang.happyshopping.service.GoodsService;
import com.chang.happyshopping.service.HappyShoppingUserService;
import com.chang.happyshopping.vo.GoodsVo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/goods")
public class GoodsController {

  @Autowired
  HappyShoppingUserService userService;

  @Autowired
  RedisService redisService;

  @Autowired
  GoodsService goodsService;

  @RequestMapping("/to_list")
  public String toGoodsList(Model model,HappyShoppingUser user){
    System.out.println(user);
    List<GoodsVo> goods = goodsService.listGoodsVo();
    model.addAttribute("goodsList",goods);
    return "goods_list";
  }

  @RequestMapping("/to_detail/{goodsId}")
  public String toDetail(Model model, HappyShoppingUser user,
                         @PathVariable("goodsId")long goodsId){
    model.addAttribute("user",user);
    GoodsVo goods=goodsService.getGoodsVoByGoodsId(goodsId);
    model.addAttribute("goods",goods);

    long startAt = goods.getStartDate().getTime();
    long endAt = goods.getEndDate().getTime();
    long now = System.currentTimeMillis();
    System.out.println(startAt);
    System.out.println(endAt);
    System.out.println(now);
    int seckillStatus;
    int remainSeconds = 0;
    if (now < startAt){
      seckillStatus = 0;
      remainSeconds = (int)(startAt-now)/1000;
    }
    else if (now > endAt){
      seckillStatus=2;
    }
    else{
      seckillStatus = 1;
    }
    System.out.println(seckillStatus);
    model.addAttribute("seckillStatus",seckillStatus);
    model.addAttribute("remainSeconds",remainSeconds);
    return "goods_detail";
  }
}
