package com.minhlq.blogs.service.impl;

import com.minhlq.blogs.dto.UpdateUserDto;
import com.minhlq.blogs.dto.response.ProfileResponse;
import com.minhlq.blogs.handler.exception.ResourceNotFoundException;
import com.minhlq.blogs.mapper.UserMapper;
import com.minhlq.blogs.model.FollowEntity;
import com.minhlq.blogs.model.UserEntity;
import com.minhlq.blogs.model.unionkey.FollowKey;
import com.minhlq.blogs.payload.UserResponse;
import com.minhlq.blogs.repository.FollowRepository;
import com.minhlq.blogs.repository.UserRepository;
import com.minhlq.blogs.service.UserService;
import com.minhlq.blogs.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

  private final FollowRepository followRepository;

  private final MessageSource messageSource;

  @Override
  @Transactional(readOnly = true)
  public UserEntity getCurrentUser() {
    var locale = LocaleContextHolder.getLocale();
    var principal = (Jwt) SecurityUtils.getAuthentication().getPrincipal();
    String username = principal.getSubject();
    return userRepository
        .findByPublicId(username)
        .orElseThrow(
            () ->
                new UsernameNotFoundException(
                    messageSource.getMessage("user.not.found", new String[] {username}, locale)));
  }

  @Override
  public UserResponse updateUserDetails(UpdateUserDto updateUserDto) {
    var updatedUser =
        userRepository
            .findById(updateUserDto.targetUser().getId())
            .map(
                currentUser -> {
                  var updateUser = UserMapper.MAPPER.toUser(currentUser, updateUserDto.params());
                  return userRepository.saveAndFlush(updateUser);
                })
            .orElseThrow(ResourceNotFoundException::new);

    return UserMapper.MAPPER.toUserResponse(updatedUser);
  }

  @Override
  @Transactional(readOnly = true)
  public ProfileResponse findByPublicId(String publicId) {
    return userRepository
        .findByPublicId(publicId)
        .map(
            targetUser -> {
              if (SecurityUtils.isAuthenticated()) {
                var currentUser = getCurrentUser();
                boolean following =
                    currentUser != null
                        && followRepository
                            .findById(new FollowKey(currentUser.getId(), targetUser.getId()))
                            .isPresent();
                return UserMapper.MAPPER.toProfileResponse(targetUser, following);
              }

              return UserMapper.MAPPER.toProfileResponse(targetUser, false);
            })
        .orElseThrow(ResourceNotFoundException::new);
  }

  @Override
  public ProfileResponse followByPublicId(Long userId, String publicId) {
    return userRepository
        .findByPublicId(publicId)
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
  public ProfileResponse unFollowByPublicId(Long userId, String publicId) {
    return userRepository
        .findByPublicId(publicId)
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
    return userRepository.findByPublicId(username).isPresent();
  }

  @Override
  public boolean isEmailExisted(String email) {
    return userRepository.findByEmail(email).isPresent();
  }
}
