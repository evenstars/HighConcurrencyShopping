package com.chang.happyshopping.service;

import com.chang.happyshopping.dao.HappyShoppingUserDAO;
import com.chang.happyshopping.domain.HappyShoppingUser;
import com.chang.happyshopping.exception.GlobalException;
import com.chang.happyshopping.redis.RedisService;
import com.chang.happyshopping.redis.SeckillUserKey;
import com.chang.happyshopping.result.CodeMsg;
import com.chang.happyshopping.utils.MD5Util;
import com.chang.happyshopping.utils.UUIDUtil;
import com.chang.happyshopping.vo.LoginVo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Service
public class HappyShoppingUserService {

  public static final String COOKIE_NAME_TOKEN = "token";

  @Autowired
  HappyShoppingUserDAO happyShoppingUserDAO;

  @Autowired
  RedisService redisService;

  public HappyShoppingUser getById(long id){
    return happyShoppingUserDAO.getById(id);
  }

  public boolean login(HttpServletResponse response,LoginVo loginVo) {
    if (loginVo==null)
      throw new GlobalException(CodeMsg.SERVER_ERROR);
    String mobile = loginVo.getMobile();
    String formPass = loginVo.getPassword();
    //mobile exist
    HappyShoppingUser user = getById(Long.parseLong(mobile));
    if (user==null)
      throw new GlobalException(CodeMsg.MOBILE_NOT_EXIST);
    //password
    String dbPass = user.getPassword();
    String salt = user.getSalt();
    String curPass = MD5Util.formPassToServerPass(salt,formPass);
    if (!curPass.equals(dbPass))
      throw new GlobalException(CodeMsg.PASSWORD_ERROR);
    //generate cookie
    String token = UUIDUtil.uuid();
    addCookie(token,user,response);
    return true;
  }

  public HappyShoppingUser getByToken(HttpServletResponse response,String token) {
    if (token==null || token.isEmpty())
      return null;
    HappyShoppingUser user = redisService.get(SeckillUserKey.token,token,HappyShoppingUser.class);
    //extend session
    if (user!=null)
      addCookie(token,user,response);
    return user;
  }

  /**
   * Extend expire time in session and cookie
   * @param user todo
   * @param response todo
   */
  private void addCookie(String token,HappyShoppingUser user,HttpServletResponse response){
    redisService.set(SeckillUserKey.token,token,user);
    Cookie cookie = new Cookie(COOKIE_NAME_TOKEN,token);
    cookie.setMaxAge(SeckillUserKey.token.expireSeconds());
    cookie.setPath("/");
    response.addCookie(cookie);
  }
}
