package com.chang.happyshopping.vo;

import com.chang.happyshopping.validator.isMobile;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

public class LoginVo {

  @NotNull
  @Length(min = 6)
  private String password;

  @NotNull
  @isMobile
  private String mobile;

  @Override
  public String toString() {
    return "LoginVo{" +
            "password='" + password + '\'' +
            ", mobile='" + mobile + '\'' +
            '}';
  }

  public String getMobile() {
    return mobile;
  }

  public void setMobile(String mobile) {
    this.mobile = mobile;
  }

  public String getPassword() {

    return password;
  }

  public void setPassword(String passWord) {
    this.password = passWord;
  }
}
