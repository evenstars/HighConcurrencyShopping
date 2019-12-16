package com.chang.happyshopping.config;

import com.chang.happyshopping.domain.HappyShoppingUser;
import com.chang.happyshopping.service.HappyShoppingUserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service
public class UserArgumentResolver implements HandlerMethodArgumentResolver {

  @Autowired
  HappyShoppingUserService userService;

  @Override
  public boolean supportsParameter(MethodParameter methodParameter) {
    Class<?> clazz = methodParameter.getParameterType();
    return clazz ==HappyShoppingUser.class;
  }

  @Override
  public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
    HttpServletRequest request = nativeWebRequest.getNativeRequest(HttpServletRequest.class);
    HttpServletResponse response = nativeWebRequest.getNativeResponse(HttpServletResponse.class);
    String paramToken = request.getParameter(HappyShoppingUserService.COOKIE_NAME_TOKEN);
    String cookieToken = getCookieValue(request,HappyShoppingUserService.COOKIE_NAME_TOKEN);
    if ((cookieToken==null || cookieToken.isEmpty()) && (paramToken ==null || paramToken.isEmpty()))
      return null;
    String token = paramToken ==null || paramToken.isEmpty()?cookieToken:paramToken;
    return userService.getByToken(response,token);
  }

  private String getCookieValue(HttpServletRequest request, String name) {
    for (Cookie cookie:request.getCookies())
      if (cookie.getName().equals(name))
        return cookie.getValue();
    return null;
  }
}
