package com.minhlq.blogsservice.annotation.impl;

import com.minhlq.blogsservice.annotation.Loggable;
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
  @Around("execution(* *(..)) && @annotation(loggable)")
  public Object log(final ProceedingJoinPoint joinPoint, final Loggable loggable) throws Throwable {

    var method = joinPoint.toShortString();
    var start = System.currentTimeMillis();

    switchStartingLogger(loggable.level(), method, joinPoint.getArgs());
    Object response = joinPoint.proceed();
    switchFinishingLogger(loggable.level(), method, response, start);

    return response;
  }

  private void switchStartingLogger(final String level, final String method, final Object args) {
    final String format = "=> Starting -  {} args: {}";

    switch (level) {
      case "warn":
        log.warn(format, method, args);
        break;
      case "error":
        log.error(format, method, args);
        break;
      case "debug":
        log.debug(format, method, args);
        break;
      case "trace":
        log.trace(format, method, args);
        break;
      default:
        log.info(format, method, args);
    }
  }

  private void switchFinishingLogger(String level, String method, Object response, long start) {
    final String format = "<= {} : {} - Finished, duration: {} ms";

    switch (level) {
      case "warn":
        log.warn(format, method, response, System.currentTimeMillis() - start);
        break;
      case "error":
        log.error(format, method, response, System.currentTimeMillis() - start);
        break;
      case "debug":
        log.debug(format, method, response, System.currentTimeMillis() - start);
        break;
      case "trace":
        log.trace(format, method, response, System.currentTimeMillis() - start);
        break;
      default:
        log.info(format, method, response, System.currentTimeMillis() - start);
    }
  }
}
