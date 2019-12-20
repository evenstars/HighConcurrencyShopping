package com.chang.happyshopping.controller;

import com.chang.happyshopping.domain.HappyShoppingUser;
import com.chang.happyshopping.result.Result;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/user")
public class UserController {

  @RequestMapping("/info")
  @ResponseBody
  public Result<HappyShoppingUser> info(HappyShoppingUser user){
    return Result.success(user);
  }
}
