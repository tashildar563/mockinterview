package com.mockinterview.mockinterview.services;

import com.mockinterview.mockinterview.annotation.IsValidDate;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class DateValidator implements ConstraintValidator<IsValidDate,Long> {

  @Override
  public void initialize(IsValidDate constraintAnnotation) {
    ConstraintValidator.super.initialize(constraintAnnotation);
  }

  @Override
  public boolean isValid(Long date, ConstraintValidatorContext constraintValidatorContext) {
    if(date == null) return false;

    ZoneId zone = ZoneId.systemDefault();
    LocalDate today = LocalDate.now(zone);

    // Start of tomorrow (00:00)
    LocalDateTime tomorrowStart = today.plusDays(1).atStartOfDay();
    long tomorrowStartMillis = tomorrowStart.atZone(zone).toInstant().toEpochMilli();

    return date>=tomorrowStartMillis;
  }
}
