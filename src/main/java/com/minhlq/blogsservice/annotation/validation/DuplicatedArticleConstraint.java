package com.minhlq.blogsservice.annotation.validation;

import com.minhlq.blogsservice.annotation.validation.validator.DuplicatedArticleValidator;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

@Documented
@Constraint(validatedBy = DuplicatedArticleValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
public @interface DuplicatedArticleConstraint {

  String message() default "Article name already existed";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
