package com.minhlq.blogsservice;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.mockStatic;

@ExtendWith(MockitoExtension.class)
class BlogsApplicationTest {

  @Mock ConfigurableApplicationContext context;

  @AfterEach
  void tearDown() {
    if (Objects.nonNull(context)) {
      context.close();
    }
  }

  @Test
  void testClassConstructor() {
    assertDoesNotThrow(BlogsApplication::new);
  }

  /** Test the main method with mocked application context. */
  @Test
  void contextLoads() {
    try (MockedStatic<SpringApplication> mockStatic = mockStatic(SpringApplication.class)) {
      mockStatic
          .when(() -> context = SpringApplication.run(BlogsApplication.class))
          .thenReturn(context);

      BlogsApplication.main(new String[] {});

      mockStatic.verify(() -> context = SpringApplication.run(BlogsApplication.class));
    }
  }
}
