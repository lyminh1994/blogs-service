package com.minhlq.blogsservice.validation;

import com.minhlq.blogsservice.validation.validator.UpdateUserValidator;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import javax.validation.Constraint;
import javax.validation.Payload;

@Constraint(validatedBy = UpdateUserValidator.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface UpdateUserConstraint {

  String message() default "invalid update user params";

  Class[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
