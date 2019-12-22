package com.chang.happyshopping.access;

import com.chang.happyshopping.domain.HappyShoppingUser;

public class UserContext {
  private static ThreadLocal<HappyShoppingUser> userHolder = new ThreadLocal<>();

  public static void setUser(HappyShoppingUser user){
    userHolder.set(user);
  }

  public static HappyShoppingUser getUser(){
    return userHolder.get();
  }
}
