package com.minhlq.blogsservice.service.impl;

import com.minhlq.blogsservice.dto.AuthenticationResponse;
import com.minhlq.blogsservice.dto.ProfileResponse;
import com.minhlq.blogsservice.dto.UpdateUserDto;
import com.minhlq.blogsservice.dto.UserPrincipal;
import com.minhlq.blogsservice.dto.request.LoginRequest;
import com.minhlq.blogsservice.dto.request.RegisterRequest;
import com.minhlq.blogsservice.dto.request.UpdateUserRequest;
import com.minhlq.blogsservice.exceptions.ResourceNotFoundException;
import com.minhlq.blogsservice.mapper.UserMapper;
import com.minhlq.blogsservice.model.Follow;
import com.minhlq.blogsservice.model.User;
import com.minhlq.blogsservice.model.unionkey.FollowId;
import com.minhlq.blogsservice.repository.FollowRepository;
import com.minhlq.blogsservice.repository.UserRepository;
import com.minhlq.blogsservice.service.JwtService;
import com.minhlq.blogsservice.service.UserService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;

  private final PasswordEncoder passwordEncoder;

  private final JwtService jwtService;

  private final AuthenticationManager authenticationManager;

  private final FollowRepository followRepository;

  @Override
  public AuthenticationResponse createUser(RegisterRequest registerRequest) {
    User user =
        userRepository.save(
            User.builder()
                .email(registerRequest.getEmail())
                .username(registerRequest.getUsername())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .build());

    UserPrincipal userPrincipal = UserMapper.MAPPER.toUserPrinciple(user);
    String accessToken = jwtService.createJwt(userPrincipal);
    String refreshToken = UUID.randomUUID().toString();

    return new AuthenticationResponse(userPrincipal, accessToken, refreshToken);
  }

  @Override
  public AuthenticationResponse login(LoginRequest loginRequest) {
    UsernamePasswordAuthenticationToken authenticationToken =
        new UsernamePasswordAuthenticationToken(
            loginRequest.getUsername(), loginRequest.getPassword());
    Authentication authentication = authenticationManager.authenticate(authenticationToken);
    SecurityContextHolder.getContext().setAuthentication(authentication);

    UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
    String accessToken = jwtService.createJwt(userPrincipal);
    String refreshToken = UUID.randomUUID().toString();

    return new AuthenticationResponse(userPrincipal, accessToken, refreshToken);
  }

  @Override
  public UserPrincipal updateProfile(UpdateUserDto updateUserDto) {
    User user =
        userRepository
            .findById(updateUserDto.getTargetUser().getId())
            .map(
                current -> {
                  UpdateUserRequest params = updateUserDto.getParams();
                  if (StringUtils.isNotBlank(params.getPassword())) {
                    current.setPassword(passwordEncoder.encode(params.getPassword()));
                  }
                  current.setEmail(params.getEmail());
                  current.setBio(params.getBio());
                  current.setImage(params.getImage());

                  return userRepository.save(current);
                })
            .orElseThrow(ResourceNotFoundException::new);

    return UserMapper.MAPPER.toUserPrinciple(user);
  }

  @Override
  public ProfileResponse findByUsername(String username, UserPrincipal currentUser) {
    return userRepository
        .findByUsername(username)
        .map(
            targetUser -> {
              boolean following =
                  currentUser != null
                      && targetUser.getFollows().stream()
                          .anyMatch(follow -> follow.getId().equals(currentUser.getId()));
              return UserMapper.MAPPER.toProfileResponse(targetUser, following);
            })
        .orElseThrow(ResourceNotFoundException::new);
  }

  @Override
  public ProfileResponse followByUsername(String username, UserPrincipal currentUser) {
    return userRepository
        .findByUsername(username)
        .map(
            targetUser -> {
              FollowId followId = new FollowId(currentUser.getId(), targetUser.getId());
              if (followRepository.findById(followId).isEmpty()) {
                followRepository.save(new Follow(followId));
              }

              return UserMapper.MAPPER.toProfileResponse(targetUser, true);
            })
        .orElseThrow(ResourceNotFoundException::new);
  }

  @Override
  public ProfileResponse unFollowByUsername(String username, UserPrincipal currentUser) {
    return userRepository
        .findByUsername(username)
        .map(
            targetUser -> {
              FollowId followId = new FollowId(currentUser.getId(), targetUser.getId());
              followRepository.findById(followId).ifPresent(followRepository::delete);

              return UserMapper.MAPPER.toProfileResponse(targetUser, false);
            })
        .orElseThrow(ResourceNotFoundException::new);
  }
}
