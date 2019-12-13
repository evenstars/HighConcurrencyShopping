package com.chang.happyshopping.controller;

import com.chang.happyshopping.domain.User;
import com.chang.happyshopping.redis.RedisService;
import com.chang.happyshopping.redis.UserKey;
import com.chang.happyshopping.result.CodeMsg;
import com.chang.happyshopping.result.Result;
import com.chang.happyshopping.service.UserService;
import com.sun.org.apache.xpath.internal.operations.Bool;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/demo")
public class SampleController {

  @Autowired
  UserService userService;

  @Autowired
  RedisService redisService;

  @RequestMapping("/hello")
  @ResponseBody
  public Result<String> hello(){
    return Result.success("hello chang");
  }

  @RequestMapping("/error")
  @ResponseBody
  public Result<String> error(){
    return Result.error(CodeMsg.SERVER_ERROR);
  }

  @RequestMapping("/thymeleaf")
  public String thymeleaf(Model model){
    model.addAttribute("name","chang");
    return "hello";
  }

  @RequestMapping("/db/get")
  @ResponseBody
  public Result<User> dbGet(){
    User user = userService.getById(1);
    return Result.success(user);
  }

  @RequestMapping("/db/tx")
  @ResponseBody
  public Result<Boolean> dbTx(){
    boolean res=userService.tx();
    return Result.success(res);
  }

  @RequestMapping("/redis/get")
  @ResponseBody
  public Result<User> redisGet(){
    User user = redisService.get(UserKey.getById,"1",User.class);
    return Result.success(user);
  }

  @RequestMapping("/redis/set")
  @ResponseBody
  public Result<Boolean> redisSet(){
    User user = new User();
    user.setId(1);
    user.setName("1111");
    redisService.set(UserKey.getById,""+1,user);
    return Result.success(true);
  }
}
