package com.minhlq.blogsservice.helper;

import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.minhlq.blogsservice.config.security.SecurityConfig;
import com.minhlq.blogsservice.entity.RoleEntity;
import com.minhlq.blogsservice.entity.UserEntity;
import com.minhlq.blogsservice.payload.UserPrincipal;
import com.minhlq.blogsservice.repository.UserRepository;
import com.minhlq.blogsservice.service.JwtService;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.userdetails.UserDetailsService;

@Import(SecurityConfig.class)
public abstract class BaseControllerTest {
  @MockBean UserRepository userRepository;
  @MockBean JwtService jwtService;
  @MockBean UserDetailsService userDetailsService;

  ObjectMapper mapper;
  UserEntity user;
  Set<RoleEntity> roles;
  UserPrincipal userDetails;

  @BeforeEach
  void setUp() {
    mapper = new ObjectMapper();
    String username = "jonathan";
    String password = "123";
    String email = "jonathan@gmail.com";
    String bio = "bio";
    String image = "image";
    user =
        UserEntity.builder()
            .id(1L)
            .username(username)
            .password(password)
            .email(email)
            .bio(bio)
            .image(image)
            .build();
    roles.add(new RoleEntity("USER", "Role user"));
    userDetails = UserPrincipal.buildUserDetails(user, roles);

    when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
    when(userRepository.findById(1L)).thenReturn(Optional.of(user));

    when(jwtService.getUsernameFromJwt("token")).thenReturn(username);
    when(userDetailsService.loadUserByUsername(username)).thenReturn(userDetails);
  }
}
