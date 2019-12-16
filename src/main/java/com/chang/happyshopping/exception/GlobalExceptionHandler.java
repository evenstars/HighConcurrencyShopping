package com.chang.happyshopping.exception;

import com.chang.happyshopping.result.CodeMsg;
import com.chang.happyshopping.result.Result;

import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;


import java.util.List;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {

  @ExceptionHandler(value = Exception.class)
  public Result<String> exceptionHandler(HttpServletRequest request,Exception e){
    if (e instanceof GlobalException){
      GlobalException ex = (GlobalException)e;
      return Result.error(ex.getCm());
    }
    else if (e instanceof BindException){
      BindException ex = (BindException)e;
      List<ObjectError> errors = ex.getAllErrors();
      String msg = errors.get(0).getDefaultMessage();
      return Result.error(CodeMsg.BING_ERROR.fillArgs(msg));
    }
    else{
      e.printStackTrace();
      return Result.error(CodeMsg.SERVER_ERROR);
    }
  }
}
