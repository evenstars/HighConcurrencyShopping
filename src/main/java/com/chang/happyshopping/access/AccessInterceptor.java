package com.chang.happyshopping.access;

import com.alibaba.fastjson.JSON;
import com.chang.happyshopping.access.AccessLimit;
import com.chang.happyshopping.domain.HappyShoppingUser;
import com.chang.happyshopping.redis.AccessKey;
import com.chang.happyshopping.redis.RedisService;
import com.chang.happyshopping.result.CodeMsg;
import com.chang.happyshopping.result.Result;
import com.chang.happyshopping.service.HappyShoppingUserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.util.WebUtils;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service
public class AccessInterceptor extends HandlerInterceptorAdapter {

  @Autowired
  HappyShoppingUserService userService;

  @Autowired
  RedisService redisService;

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
    if (handler instanceof HandlerMethod) {
      HappyShoppingUser user = getUser(request, response);
      UserContext.setUser(user);

      HandlerMethod hm = (HandlerMethod) handler;
      AccessLimit accessLimit = hm.getMethodAnnotation(AccessLimit.class);
      if (accessLimit == null)
        return true;
      int seconds = accessLimit.seconds();
      int maxCount = accessLimit.maxCount();
      boolean needLogin = accessLimit.needLogin();
      String key = request.getRequestURI();
      if (needLogin) {
        if (user == null) {
          render(response, CodeMsg.SESSION_ERROR);
          return false;
        }
        key += user.getId();
      }
      AccessKey accessKey = AccessKey.withExpire(seconds);
      Integer count = redisService.get(accessKey, key, Integer.class);
      if (count == null) {
        redisService.set(accessKey, key, 1);
      } else if (count < maxCount)
        redisService.increase(AccessKey.access, key);
      else{
        render(response,CodeMsg.ACCESS_LIMIT_MAX);
        return false;
      }
    }
    return true;
  }

  private void render(HttpServletResponse response, CodeMsg codeMsg) throws IOException {
    response.setContentType("application/json;charset=UTF-8");
    OutputStream out = response.getOutputStream();
    String str = JSON.toJSONString(Result.error(codeMsg));
    out.write(str.getBytes("UTF-8"));
    out.flush();
    out.close();
  }

  private HappyShoppingUser getUser(HttpServletRequest request, HttpServletResponse response) {

    String paramToken = request.getParameter(HappyShoppingUserService.COOKIE_NAME_TOKEN);
    String cookieToken = getCookieValue(request, HappyShoppingUserService.COOKIE_NAME_TOKEN);
    if ((cookieToken == null || cookieToken.isEmpty()) && (paramToken == null || paramToken.isEmpty()))
      return null;
    String token = paramToken == null || paramToken.isEmpty() ? cookieToken : paramToken;
    return userService.getByToken(response, token);
  }

  private String getCookieValue(HttpServletRequest request, String name) {
    if (request == null || request.getCookies() == null)
      return null;
    for (Cookie cookie : request.getCookies())
      if (cookie.getName().equals(name))
        return cookie.getValue();
    return null;
  }
}
