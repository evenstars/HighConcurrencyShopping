package com.chang.happyshopping.config;

import com.chang.happyshopping.access.AccessInterceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {

  @Autowired
  UserArgumentResolver resolver;

  @Autowired
  AccessInterceptor accessInterceptor;

  @Override
  public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
    argumentResolvers.add(resolver);
  }

  @Override
  public void addInterceptors(InterceptorRegistry registry){
    registry.addInterceptor(accessInterceptor);
  }

}
