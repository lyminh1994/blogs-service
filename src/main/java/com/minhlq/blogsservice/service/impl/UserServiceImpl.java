package com.minhlq.blogsservice.service.impl;

import com.minhlq.blogsservice.dto.UpdateUserDto;
import com.minhlq.blogsservice.dto.mapper.UserMapper;
import com.minhlq.blogsservice.dto.request.UpdateUserRequest;
import com.minhlq.blogsservice.dto.response.ProfileResponse;
import com.minhlq.blogsservice.exception.ResourceNotFoundException;
import com.minhlq.blogsservice.model.FollowEntity;
import com.minhlq.blogsservice.model.UserEntity;
import com.minhlq.blogsservice.model.unionkey.FollowKey;
import com.minhlq.blogsservice.payload.UserPrincipal;
import com.minhlq.blogsservice.repository.FollowRepository;
import com.minhlq.blogsservice.repository.UserRepository;
import com.minhlq.blogsservice.service.UserService;
import com.minhlq.blogsservice.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
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
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;

  private final FollowRepository followRepository;

  @Override
  public UserPrincipal updateUserDetails(UpdateUserDto updateUserDto) {
    UserEntity updatedUser =
        userRepository
            .findById(updateUserDto.targetUser().id())
            .map(
                currentUser -> {
                  UpdateUserRequest params = updateUserDto.params();
                  currentUser.setEmail(params.email());
                  currentUser.setFirstName(params.firstName());
                  currentUser.setLastName(params.lastName());
                  currentUser.setPhone(params.phone());
                  currentUser.setBirthday(params.birthday());
                  currentUser.setGender(params.gender());
                  currentUser.setProfileImage(params.profileImage());

                  return userRepository.saveAndFlush(currentUser);
                })
            .orElseThrow(ResourceNotFoundException::new);

    return UserPrincipal.buildUserDetails(updatedUser);
  }

  @Override
  @Transactional(readOnly = true)
  public ProfileResponse findByUsername(String username) {
    UserPrincipal currentUser = SecurityUtils.getAuthenticatedUserDetails();
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
  public ProfileResponse followByUsername(String username) {
    UserPrincipal currentUser = SecurityUtils.getAuthenticatedUserDetails();
    return userRepository
        .findByUsername(username)
        .map(
            targetUser -> {
              FollowKey followId = new FollowKey(currentUser.id(), targetUser.getId());
              if (!followRepository.existsById(followId)) {
                followRepository.save(new FollowEntity(followId));
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
              FollowKey followId = new FollowKey(currentUser.id(), targetUser.getId());
              followRepository.findById(followId).ifPresent(followRepository::delete);

              return UserMapper.MAPPER.toProfileResponse(targetUser, false);
            })
        .orElseThrow(ResourceNotFoundException::new);
  }
}
