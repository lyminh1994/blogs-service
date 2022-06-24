package com.minhlq.blogsservice.service.impl;

import com.minhlq.blogsservice.constant.CacheConstants;
import com.minhlq.blogsservice.dto.UpdateUserDto;
import com.minhlq.blogsservice.dto.mapper.UserMapper;
import com.minhlq.blogsservice.dto.request.UpdateUserRequest;
import com.minhlq.blogsservice.dto.response.ProfileResponse;
import com.minhlq.blogsservice.entity.FollowEntity;
import com.minhlq.blogsservice.entity.RoleEntity;
import com.minhlq.blogsservice.entity.UserEntity;
import com.minhlq.blogsservice.entity.unionkey.FollowKey;
import com.minhlq.blogsservice.exception.ResourceNotFoundException;
import com.minhlq.blogsservice.payload.UserPrincipal;
import com.minhlq.blogsservice.repository.FollowRepository;
import com.minhlq.blogsservice.repository.UserRepository;
import com.minhlq.blogsservice.service.RoleService;
import com.minhlq.blogsservice.service.UserService;
import com.minhlq.blogsservice.util.SecurityUtils;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * The UserServiceImpl class provides implementation for the UserService definitions.
 *
 * @author Minh Lys
 * @version 1.0
 * @since 1.0
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;

  private final FollowRepository followRepository;

  private final RoleService roleService;

  @Override
  @Transactional
  @Caching(
      evict = {
        @CacheEvict(value = CacheConstants.USERS, key = "#updateUserDto.targetUser.username")
      })
  public UserPrincipal updateUser(UpdateUserDto updateUserDto) {
    UserEntity newUser =
        userRepository
            .findById(updateUserDto.getTargetUser().getId())
            .map(
                currentUser -> {
                  UpdateUserRequest params = updateUserDto.getParams();
                  currentUser.setEmail(params.getEmail());
                  currentUser.setBio(params.getBio());
                  currentUser.setImage(params.getImage());

                  return userRepository.save(currentUser);
                })
            .orElseThrow(ResourceNotFoundException::new);

    Set<RoleEntity> roles = roleService.findByUserId(newUser.getId());

    return UserPrincipal.buildUserDetails(newUser, roles);
  }

  @Override
  @Cacheable(CacheConstants.USERS)
  public ProfileResponse findByUsername(String username) {
    UserPrincipal currentUser = SecurityUtils.getAuthenticatedUserDetails();
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
    UserPrincipal currentUser = SecurityUtils.getAuthenticatedUserDetails();
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
    UserPrincipal currentUser = SecurityUtils.getAuthenticatedUserDetails();
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
}
