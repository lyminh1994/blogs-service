package com.minhlq.blogs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * The class has the main method to get the application started.
 *
 * @author Minh Lys
 * @version 1.0
 * @since 1.0
 */
@EnableFeignClients
@SpringBootApplication
public class BlogsApplication {

  /**
   * The application's entry point.
   *
   * @param args an array of command-line arguments for the application
   */
  public static void main(String[] args) {
    SpringApplication.run(BlogsApplication.class, args);
  }
}
