package com.minhlq.blogsservice.service.impl;

import com.minhlq.blogsservice.dto.UpdateUserDto;
import com.minhlq.blogsservice.dto.UserPrincipal;
import com.minhlq.blogsservice.dto.request.LoginRequest;
import com.minhlq.blogsservice.dto.request.RegisterRequest;
import com.minhlq.blogsservice.dto.request.UpdateUserRequest;
import com.minhlq.blogsservice.dto.response.AuthenticationResponse;
import com.minhlq.blogsservice.dto.response.ProfileResponse;
import com.minhlq.blogsservice.entity.FollowEntity;
import com.minhlq.blogsservice.entity.UserEntity;
import com.minhlq.blogsservice.entity.unionkey.FollowKey;
import com.minhlq.blogsservice.exception.ResourceNotFoundException;
import com.minhlq.blogsservice.mapper.UserMapper;
import com.minhlq.blogsservice.repository.FollowRepository;
import com.minhlq.blogsservice.repository.UserRepository;
import com.minhlq.blogsservice.service.JwtService;
import com.minhlq.blogsservice.service.UserService;
import com.minhlq.blogsservice.utils.SecurityUtils;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Log4j2
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
    UserEntity user =
        userRepository.save(
            UserEntity.builder()
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
    UserEntity newUser =
        userRepository
            .findById(updateUserDto.getTargetUser().getId())
            .map(
                oldUser -> {
                  UpdateUserRequest params = updateUserDto.getParams();
                  if (StringUtils.isNotBlank(params.getPassword())) {
                    oldUser.setPassword(passwordEncoder.encode(params.getPassword()));
                  }
                  oldUser.setEmail(params.getEmail());
                  oldUser.setBio(params.getBio());
                  oldUser.setImage(params.getImage());

                  return userRepository.save(oldUser);
                })
            .orElseThrow(ResourceNotFoundException::new);

    return UserMapper.MAPPER.toUserPrinciple(newUser);
  }

  @Override
  public ProfileResponse findByUsername(String username) {
    UserPrincipal currentUser = SecurityUtils.getCurrentUser();
    return userRepository
        .findByUsername(username)
        .map(
            targetUser -> {
              boolean following =
                  currentUser != null
                      && followRepository
                          .findById(new FollowKey(currentUser.getId(), targetUser.getId()))
                          .isPresent();

              return UserMapper.MAPPER.toProfileResponse(targetUser, following);
            })
        .orElseThrow(ResourceNotFoundException::new);
  }

  @Override
  public ProfileResponse followByUsername(String username) {
    UserPrincipal currentUser = SecurityUtils.getCurrentUser();
    return userRepository
        .findByUsername(username)
        .map(
            targetUser -> {
              FollowKey followKey = new FollowKey(currentUser.getId(), targetUser.getId());
              if (!followRepository.existsById(followKey)) {
                followRepository.save(new FollowEntity(followKey));
              }

              return UserMapper.MAPPER.toProfileResponse(targetUser, true);
            })
        .orElseThrow(ResourceNotFoundException::new);
  }

  @Override
  public ProfileResponse unFollowByUsername(String username) {
    UserPrincipal currentUser = SecurityUtils.getCurrentUser();
    return userRepository
        .findByUsername(username)
        .map(
            targetUser -> {
              FollowKey followKey = new FollowKey(currentUser.getId(), targetUser.getId());
              followRepository.findById(followKey).ifPresent(followRepository::delete);

              return UserMapper.MAPPER.toProfileResponse(targetUser, false);
            })
        .orElseThrow(ResourceNotFoundException::new);
  }

  @Override
  @CachePut(value = "user", key = "#user.id")
  public UserEntity saveOrUpdate(UserEntity user) {
    return user;
  }

  @Override
  @Cacheable(value = "user", key = "#id")
  public UserEntity get(Long id) {
    return userRepository.findById(id).orElse(null);
  }

  @Override
  @CacheEvict(value = "user", key = "#id")
  public void delete(Long id) {
    log.info("Delete catch user: {}", id);
  }
}
