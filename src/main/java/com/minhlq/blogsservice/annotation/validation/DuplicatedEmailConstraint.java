package com.minhlq.blogsservice.annotation.validation;

import com.minhlq.blogsservice.annotation.validation.validator.DuplicatedEmailValidator;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import javax.validation.Constraint;
import javax.validation.Payload;

@Constraint(validatedBy = DuplicatedEmailValidator.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface DuplicatedEmailConstraint {

  String message() default "Email has existed";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
