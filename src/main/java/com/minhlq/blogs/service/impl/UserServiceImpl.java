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
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final FollowRepository followRepository;
  private final MessageSource messageSource;
  private final UserMapper userMapper;

  @Override
  public UserEntity getCurrentUser() {
    if (SecurityUtils.isAuthenticated()) {
      var locale = LocaleContextHolder.getLocale();
      var username = (Jwt) SecurityUtils.getAuthentication().getPrincipal();
      return userRepository
          .findByUsername(username.getSubject())
          .orElseThrow(
              () ->
                  new UsernameNotFoundException(
                      messageSource.getMessage(
                          "user.not.found", new String[] {username.getSubject()}, locale)));
    }

    return null;
  }

  @Override
  @Transactional
  public UserResponse updateUserDetails(UpdateUserDto updateUserDto) {
    var updatedUser =
        userRepository
            .findById(updateUserDto.targetUser().getId())
            .map(
                currentUser -> {
                  var updateUser = userMapper.toUser(currentUser, updateUserDto.params());
                  return userRepository.saveAndFlush(updateUser);
                })
            .orElseThrow(ResourceNotFoundException::new);

    return userMapper.toUserResponse(updatedUser);
  }

  @Override
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
                return userMapper.toProfileResponse(targetUser, following);
              }

              return userMapper.toProfileResponse(targetUser, false);
            })
        .orElseThrow(ResourceNotFoundException::new);
  }

  @Override
  @Transactional
  public ProfileResponse followByPublicId(Long userId, String publicId) {
    return userRepository
        .findByPublicId(publicId)
        .map(
            targetUser -> {
              var followId = new FollowKey(userId, targetUser.getId());
              if (!followRepository.existsById(followId)) {
                followRepository.saveAndFlush(new FollowEntity(followId));
              }

              return userMapper.toProfileResponse(targetUser, true);
            })
        .orElseThrow(ResourceNotFoundException::new);
  }

  @Override
  @Transactional
  public ProfileResponse unFollowByPublicId(Long userId, String publicId) {
    return userRepository
        .findByPublicId(publicId)
        .map(
            targetUser -> {
              var followId = new FollowKey(userId, targetUser.getId());
              followRepository.findById(followId).ifPresent(followRepository::delete);

              return userMapper.toProfileResponse(targetUser, false);
            })
        .orElseThrow(ResourceNotFoundException::new);
  }
}
