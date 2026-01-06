package com.mockinterview.mockinterview.annotation;

import com.mockinterview.mockinterview.services.NotBlankValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = NotBlankValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface NotBlank {
  String message() default "Field contains invalid characters";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

}
