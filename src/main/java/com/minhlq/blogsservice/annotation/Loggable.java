package com.minhlq.blogsservice.annotation;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * A custom annotation as a pointcut, thus wherever this annotation appears on a method, logging
 * aspect will run around that method execution and log the entry-exit and time taken by that
 * method.
 *
 * @author Minh Lys
 * @version 1.0
 * @since 1.0
 */
@Documented
@Target({METHOD})
@Retention(RUNTIME)
public @interface Loggable {}
