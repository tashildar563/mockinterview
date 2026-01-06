package com.mockinterview.mockinterview.annotation;

import com.mockinterview.mockinterview.services.DateValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = DateValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface IsValidDate {
  String message() default "Field contains invalid characters";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

}
