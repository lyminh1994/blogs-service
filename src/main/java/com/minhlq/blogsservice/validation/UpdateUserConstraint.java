package com.minhlq.blogsservice.validation;

import com.minhlq.blogsservice.validation.validator.UpdateUserValidator;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import javax.validation.Constraint;

@Constraint(validatedBy = UpdateUserValidator.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface UpdateUserConstraint {

  String message() default "invalid update param";

  Class[] groups() default {};

  Class[] payload() default {};
}
