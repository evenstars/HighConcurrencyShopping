package com.chang.happyshopping.controller;

import com.chang.happyshopping.domain.HappyShoppingUser;
import com.chang.happyshopping.rabbitmq.MQSender;
import com.chang.happyshopping.redis.RedisService;
import com.chang.happyshopping.result.Result;
import com.chang.happyshopping.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/sample")
public class UserController {

  @Autowired
  UserService userService;

  @Autowired
  RedisService redisService;

  @Autowired
  MQSender sender;

  @RequestMapping("/info")
  @ResponseBody
  public Result<HappyShoppingUser> info(HappyShoppingUser user){
    return Result.success(user);
  }

//  @RequestMapping("/mq")
//  @ResponseBody
//  public Result<String> mq(){
//    sender.sendSeckillMessage("hello mq");
//    return Result.success("send ok");
//  }
}
