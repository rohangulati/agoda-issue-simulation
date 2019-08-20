package com.rohangulati.agoda.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class WeekValidator implements ConstraintValidator<Week, String> {
  @Override
  public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
    try {
      com.rohangulati.agoda.data.Week result = com.rohangulati.agoda.data.Week.of(s);
      return result != null;
    } catch (Exception e) {
    }
    return false;
  }
}
