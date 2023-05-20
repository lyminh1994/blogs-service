package com.minhlq.blogs.service.impl;

import com.minhlq.blogs.constant.AppConstants;
import com.minhlq.blogs.constant.CacheConstants;
import com.minhlq.blogs.constant.UserConstants;
import com.minhlq.blogs.dto.UpdateUserDto;
import com.minhlq.blogs.dto.response.ProfileResponse;
import com.minhlq.blogs.enums.UserRole;
import com.minhlq.blogs.handler.exception.ResourceNotFoundException;
import com.minhlq.blogs.mapper.UserMapper;
import com.minhlq.blogs.model.FollowEntity;
import com.minhlq.blogs.model.UserEntity;
import com.minhlq.blogs.model.unionkey.FollowKey;
import com.minhlq.blogs.payload.SignUpRequest;
import com.minhlq.blogs.payload.UserPrincipal;
import com.minhlq.blogs.repository.FollowRepository;
import com.minhlq.blogs.repository.UserRepository;
import com.minhlq.blogs.service.EncryptionService;
import com.minhlq.blogs.service.JwtService;
import com.minhlq.blogs.service.RoleService;
import com.minhlq.blogs.service.UserService;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CachePut;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

/**
 * The UserServiceImpl class provides implementation for the UserService definitions.
 *
 * @author Minh Lys
 * @version 1.0
 * @since 1.0
 */
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;

  private final RoleService roleService;

  private final JwtService jwtService;

  private final PasswordEncoder passwordEncoder;

  private final EncryptionService encryptionService;

  private final FollowRepository followRepository;

  @Override
  public void createUser(SignUpRequest signUpBody) {
    var role = roleService.findByName(UserRole.ROLE_USER);
    var ttl = Duration.ofDays(UserConstants.DAYS_TO_ALLOW_ACCOUNT_ACTIVATION);

    var verificationToken =
        jwtService.createJwt(
            signUpBody.username(), Date.from(Instant.now().plusSeconds(ttl.toSeconds())));

    var encodedVerifyToken = encryptionService.encode(verificationToken);
    var uri =
        ServletUriComponentsBuilder.fromCurrentContextPath()
            .path(AppConstants.VERIFY)
            .buildAndExpand(encodedVerifyToken)
            .toUri();
    log.debug("{}", uri);

    var user = new UserEntity();
    user.setUsername(signUpBody.username());
    user.setPassword(passwordEncoder.encode(signUpBody.password()));
    user.setEmail(signUpBody.email());
    user.setVerificationToken(encodedVerifyToken);
    user.addRole(role);

    userRepository.save(user);
  }

  @Override
  @CachePut(value = CacheConstants.USER_DETAILS, unless = "#result != null")
  public UserPrincipal updateUserDetails(UpdateUserDto updateUserDto) {
    var updatedUser =
        userRepository
            .findById(updateUserDto.targetUser().id())
            .map(
                currentUser -> {
                  var updateUser = UserMapper.MAPPER.toUser(currentUser, updateUserDto.params());
                  return userRepository.saveAndFlush(updateUser);
                })
            .orElseThrow(ResourceNotFoundException::new);

    return UserPrincipal.buildUserDetails(updatedUser);
  }

  @Override
  @Transactional(readOnly = true)
  public ProfileResponse findByUsername(UserPrincipal currentUser, String username) {
    return userRepository
        .findByUsername(username)
        .map(
            targetUser -> {
              boolean following =
                  currentUser != null
                      && followRepository
                          .findById(new FollowKey(currentUser.id(), targetUser.getId()))
                          .isPresent();

              return UserMapper.MAPPER.toProfileResponse(targetUser, following);
            })
        .orElseThrow(ResourceNotFoundException::new);
  }

  @Override
  public ProfileResponse followByUsername(Long userId, String username) {
    return userRepository
        .findByUsername(username)
        .map(
            targetUser -> {
              var followId = new FollowKey(userId, targetUser.getId());
              if (!followRepository.existsById(followId)) {
                followRepository.save(new FollowEntity(followId));
              }

              return UserMapper.MAPPER.toProfileResponse(targetUser, true);
            })
        .orElseThrow(ResourceNotFoundException::new);
  }

  @Override
  public ProfileResponse unFollowByUsername(Long userId, String username) {
    return userRepository
        .findByUsername(username)
        .map(
            targetUser -> {
              var followId = new FollowKey(userId, targetUser.getId());
              followRepository.findById(followId).ifPresent(followRepository::delete);

              return UserMapper.MAPPER.toProfileResponse(targetUser, false);
            })
        .orElseThrow(ResourceNotFoundException::new);
  }

  @Override
  public boolean isUsernameExisted(String username) {
    return userRepository.findByUsername(username).isPresent();
  }

  @Override
  public boolean isEmailExisted(String email) {
    return userRepository.findByEmail(email).isPresent();
  }
}