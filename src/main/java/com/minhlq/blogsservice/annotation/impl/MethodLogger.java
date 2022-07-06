package com.minhlq.blogsservice.annotation.impl;

import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * Ensures that method calls can be logged with entry-exit logs in console or log file.
 *
 * @author Minh Lys
 * @version 1.0
 * @since 1.0
 */
@Log4j2
@Aspect
@Component
public class MethodLogger {

  /**
   * - visibility modifier is * (public, protected or private) - name is * (any name); - arguments
   * are .. (any arguments); and - is annotated with @Loggable.
   *
   * @param joinPoint the joinPoint
   * @return the log object
   * @throws Throwable if an error occurs
   */
  @Around("execution(* *(..)) && @annotation(com.minhlq.blogsservice.annotation.Loggable)")
  public Object log(ProceedingJoinPoint joinPoint) throws Throwable {
    String method = joinPoint.getSignature().toShortString();
    long start = System.currentTimeMillis();

    log.info("=> Starting - {} args: {}", method, joinPoint.getArgs());
    Object proceeding = joinPoint.proceed();
    log.info(
        "<= {} : {} - Finished, duration: {} ms =>",
        method,
        proceeding,
        System.currentTimeMillis() - start);

    return proceeding;
  }
}
