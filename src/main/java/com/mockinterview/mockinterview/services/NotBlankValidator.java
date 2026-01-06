package com.mockinterview.mockinterview.services;

import com.mockinterview.mockinterview.annotation.NotBlank;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.util.StringUtils;

public class NotBlankValidator implements ConstraintValidator<NotBlank,String> {

  @Override
  public void initialize(NotBlank constraintAnnotation) {
    ConstraintValidator.super.initialize(constraintAnnotation);
  }

  @Override
  public boolean isValid(String field, ConstraintValidatorContext constraintValidatorContext) {
    if(field == null || field.isEmpty() || field.equals(" ") || field.equals("null")){
      return false;
    }
    return true;
  }
}
