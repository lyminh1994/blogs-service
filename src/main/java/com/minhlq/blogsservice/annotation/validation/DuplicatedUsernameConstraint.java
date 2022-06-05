package com.minhlq.blogsservice.annotation.validation;

import com.minhlq.blogsservice.annotation.validation.validator.DuplicatedUsernameValidator;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import javax.validation.Constraint;
import javax.validation.Payload;

@Constraint(validatedBy = DuplicatedUsernameValidator.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface DuplicatedUsernameConstraint {

  String message() default "Username has existed";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
