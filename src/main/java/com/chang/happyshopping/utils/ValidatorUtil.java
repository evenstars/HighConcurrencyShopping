package com.chang.happyshopping.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidatorUtil {

  private static final Pattern mobile_pattern = Pattern.compile("1\\d{10}");
  public static boolean isMobile(String src){
    if (src==null || src.isEmpty())
      return false;
    System.out.println(src);
    Matcher m = mobile_pattern.matcher(src);
    return m.matches();
  }
}
