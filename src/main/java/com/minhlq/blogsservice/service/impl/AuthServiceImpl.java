package com.minhlq.blogsservice.service.impl;

import com.minhlq.blogsservice.dto.UserPrincipal;
import com.minhlq.blogsservice.dto.UserTokenResponse;
import com.minhlq.blogsservice.dto.request.LoginRequest;
import com.minhlq.blogsservice.dto.request.RegisterRequest;
import com.minhlq.blogsservice.mapper.UserMapper;
import com.minhlq.blogsservice.model.User;
import com.minhlq.blogsservice.repository.UserRepository;
import com.minhlq.blogsservice.service.JwtService;
import com.minhlq.blogsservice.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

  private final UserRepository userRepository;

  private final PasswordEncoder passwordEncoder;

  private final JwtService jwtService;

  private final AuthenticationManager authenticationManager;

  @Override
  public UserTokenResponse createUser(RegisterRequest registerRequest) {
    User user = userRepository.save(User.builder()
            .email(registerRequest.getEmail())
            .username(registerRequest.getUsername())
            .password(passwordEncoder.encode(registerRequest.getPassword()))
            .build());

    UserPrincipal userPrincipal = UserMapper.MAPPER.toUserPrinciple(user);
    String token = jwtService.createJwt(userPrincipal);
    String refreshToken = UUID.randomUUID().toString();

    return new UserTokenResponse(userPrincipal, token, refreshToken);
  }

  @Override
  public UserTokenResponse login(LoginRequest loginRequest) {
    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
            loginRequest.getUsername(), loginRequest.getPassword());
    Authentication authentication = authenticationManager.authenticate(authenticationToken);
    SecurityContextHolder.getContext().setAuthentication(authentication);

    UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
    String token = jwtService.createJwt(userPrincipal);
    String refreshToken = UUID.randomUUID().toString();

    return new UserTokenResponse(userPrincipal, token, refreshToken);
  }

}
