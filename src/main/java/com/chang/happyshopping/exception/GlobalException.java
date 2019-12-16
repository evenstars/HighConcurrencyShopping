package com.chang.happyshopping.exception;

import com.chang.happyshopping.result.CodeMsg;

public class GlobalException extends RuntimeException{

  private CodeMsg cm;

  public CodeMsg getCm() {
    return cm;
  }

  public GlobalException(CodeMsg cm){
    super();
    this.cm = cm;

  }

}
