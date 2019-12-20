package com.chang.happyshopping.result;

public class CodeMsg {
  private int code;
  private String msg;

  public static CodeMsg SUCCESS = new CodeMsg(0,"success");

  public static CodeMsg SERVER_ERROR = new CodeMsg(500,"SERVER error");

  public static CodeMsg PASSWORD_EMPTY = new CodeMsg(500200,"password cannot be empty");

  public static CodeMsg MOBILE_EMPTY = new CodeMsg(500522,"mobile cannot be empty");

  public static CodeMsg MOBILE_ERROR = new CodeMsg(500233,"mobile error");

  public static CodeMsg MOBILE_NOT_EXIST = new CodeMsg(500234,"mobile not exist");

  public static CodeMsg PASSWORD_ERROR = new CodeMsg(500235,"password error");

  public static CodeMsg BING_ERROR = new CodeMsg(500235,"parameter check error:%s");

  //sec kill module
  public static CodeMsg STOCK_EMPTY = new CodeMsg(500236,"sec kill goods is over");

  public static CodeMsg REPEAT_PURCHASE = new CodeMsg(500240,"cannot purchase multiple times");


  public CodeMsg fillArgs(Object...args){
    int code = this.code;
    String message = String.format(this.msg,args);
    return new CodeMsg(code,message);
  }

  private CodeMsg(int code,String msg){
    this.code = code;
    this.msg = msg;
  }

  public String getMsg() {
    return msg;
  }

  public void setMsg(String msg) {
    this.msg = msg;
  }

  public int getCode() {

    return code;
  }

  public void setCode(int code) {
    this.code = code;
  }
}
