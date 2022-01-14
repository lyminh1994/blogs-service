package com.minhlq.blogsservice.validation;

import com.minhlq.blogsservice.validation.validator.DuplicatedUsernameValidator;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import javax.validation.Constraint;
import javax.validation.Payload;

@Constraint(validatedBy = DuplicatedUsernameValidator.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface DuplicatedUsernameConstraint {

  String message() default "duplicated username";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
