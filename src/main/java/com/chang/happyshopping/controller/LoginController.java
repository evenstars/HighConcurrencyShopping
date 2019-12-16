package com.chang.happyshopping.controller;

import com.chang.happyshopping.domain.User;
import com.chang.happyshopping.redis.RedisService;
import com.chang.happyshopping.redis.UserKey;
import com.chang.happyshopping.result.CodeMsg;
import com.chang.happyshopping.result.Result;
import com.chang.happyshopping.service.HappyShoppingUserService;
import com.chang.happyshopping.service.UserService;
import com.chang.happyshopping.utils.ValidatorUtil;
import com.chang.happyshopping.vo.LoginVo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Controller
@RequestMapping("/login")
public class LoginController {

  private static Logger log = LoggerFactory.getLogger(LoginController.class);

  @Autowired
  UserService userService;

  @Autowired
  RedisService redisService;

  @Autowired
  HappyShoppingUserService happyShoppingUserService;

  @RequestMapping("/to_login")
  public String toLogin(){
    return "login";
  }

  @RequestMapping("/do_login")
  @ResponseBody
  public Result<Boolean> doLogin(HttpServletResponse response, @Valid LoginVo loginVo){
    log.info(loginVo.toString());
    //login
    happyShoppingUserService.login(response,loginVo);
    return Result.success(true);
  }

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
