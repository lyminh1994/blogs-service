package com.minhlq.blogsservice;

import java.util.Objects;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

@ExtendWith(MockitoExtension.class)
class BlogsApplicationTest {

  @Mock private ConfigurableApplicationContext context;

  @AfterEach
  void tearDown() {
    if (Objects.nonNull(context)) {
      context.close();
    }
  }

  @Test
  void testClassConstructor() {
    Assertions.assertDoesNotThrow(BlogsApplication::new);
  }

  /** Test the main method with mocked application context. */
  @Test
  void contextLoads() {
    try (MockedStatic<SpringApplication> mockStatic = Mockito.mockStatic(SpringApplication.class)) {
      mockStatic
          .when(() -> context = SpringApplication.run(BlogsApplication.class))
          .thenReturn(context);

      BlogsApplication.main(new String[] {});

      mockStatic.verify(() -> context = SpringApplication.run(BlogsApplication.class));
    }
  }
}
