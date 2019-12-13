package com.chang.happyshopping.result;

public class Result<T> {
  private int code;
  private String msg;
  private T data;

  private Result(T data){
    this.code = 0;
    this.msg = "success";
    this.data = data;
  }

  private Result(CodeMsg cm){
    if (cm==null)
      return;
    this.code = cm.getCode();
    this.msg = cm.getMsg();
  }

  public static <T> Result<T> success(T data){
    return new Result<>(data);
  }

  public static <T> Result<T> error(CodeMsg cm){
    return new Result<>(cm);
  }

  public T getData() {
    return data;
  }

  public String getMsg() {

    return msg;
  }

  public int getCode() {

    return code;
  }

}
