package com.chang.happyshopping.utils;

import org.apache.commons.codec.digest.DigestUtils;

public class MD5Util {

  private static final String salt = "1a2b3c4d";

  private static String md5(String src){
    return DigestUtils.md5Hex(src);
  }

  public static String inputPassToFormPass(String inputPass){
    return getMixedPass(salt,inputPass);
  }

  public static String formPassToServerPass(String salt,String inputPass){
    return getMixedPass(salt,inputPass);
  }

  private static String getMixedPass(String salt,String src){
    String str = salt.charAt(0)+salt.charAt(2)+src+salt.charAt(5)+salt.charAt(4);
    return md5(str);
  }
}
