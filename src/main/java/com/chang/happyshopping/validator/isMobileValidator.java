package com.chang.happyshopping.validator;

import com.chang.happyshopping.utils.ValidatorUtil;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class isMobileValidator implements ConstraintValidator<isMobile,String> {

  private boolean required = false;

  public void initialize(isMobile constraintAnnotation){
    required = constraintAnnotation.required();
  }

  @Override
  public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
    if (required){
      return ValidatorUtil.isMobile(s);
    }
    else{
      if (s==null || s.isEmpty())
        return true;
      else
        return ValidatorUtil.isMobile(s);
    }
  }
}
