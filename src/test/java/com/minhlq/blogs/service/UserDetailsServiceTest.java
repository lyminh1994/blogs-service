package com.minhlq.blogs.service;

import com.minhlq.blogs.repository.UserRepository;
import com.minhlq.blogs.service.impl.UserDetailsServiceImpl;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

@Disabled("not ready yet")
@ExtendWith(MockitoExtension.class)
class UserDetailsServiceTest {

  @Mock MessageSource messageSource;

  @Mock UserRepository userRepository;

  @InjectMocks UserDetailsServiceImpl userDetailsService;

  @Test
  void should_load_user_by_username_success() {}

  @Test
  void should_load_user_by_username_throw_username_not_found_exception() {}
}
