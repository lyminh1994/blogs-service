package com.minhlq.blogs.helper;

import com.redis.testcontainers.RedisContainer;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.lang.NonNull;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.lifecycle.Startables;
import org.testcontainers.utility.DockerImageName;

class TestcontainersInitializer
    implements ApplicationContextInitializer<ConfigurableApplicationContext> {

  static PostgreSQLContainer<?> postgres =
      new PostgreSQLContainer<>(DockerImageName.parse("postgres:latest"));

  static RedisContainer redis = new RedisContainer(DockerImageName.parse("redis:latest"));

  static {
    Startables.deepStart(postgres, redis).join();
  }

  @Override
  public void initialize(@NonNull ConfigurableApplicationContext applicationContext) {
    TestPropertyValues.of(
            "spring.datasource.url=" + postgres.getJdbcUrl(),
            "spring.datasource.username=" + postgres.getUsername(),
            "spring.datasource.password=" + postgres.getPassword(),
            "spring.redis.host=" + redis.getHost(),
            "spring.redis.port=" + redis.getMappedPort(6379))
        .applyTo(applicationContext.getEnvironment());
  }
}
