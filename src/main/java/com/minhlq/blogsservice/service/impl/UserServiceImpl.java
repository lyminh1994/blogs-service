package com.minhlq.blogsservice.service.impl;

import com.minhlq.blogsservice.dto.UpdateUserDto;
import com.minhlq.blogsservice.dto.mapper.UserMapper;
import com.minhlq.blogsservice.dto.request.UpdatePasswordRequest;
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
import org.springframework.security.crypto.password.PasswordEncoder;
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

  private final PasswordEncoder passwordEncoder;

  @Override
  public UserPrincipal updateUser(UpdateUserDto updateUserDto) {
    UserEntity updatedUser =
        userRepository
            .findById(updateUserDto.getTargetUser().getId())
            .map(
                currentUser -> {
                  UpdateUserRequest params = updateUserDto.getParams();
                  currentUser.setEmail(params.getEmail());
                  currentUser.setFirstName(params.getFirstName());
                  currentUser.setLastName(params.getLastName());
                  currentUser.setPhone(params.getPhone());
                  currentUser.setBirthday(params.getBirthday());
                  currentUser.setGender(params.getGender());
                  currentUser.setProfileImage(params.getProfileImage());

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
              FollowKey followId = new FollowKey(currentUser.getId(), targetUser.getId());
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
              FollowKey followId = new FollowKey(currentUser.getId(), targetUser.getId());
              followRepository.findById(followId).ifPresent(followRepository::delete);

              return UserMapper.MAPPER.toProfileResponse(targetUser, false);
            })
        .orElseThrow(ResourceNotFoundException::new);
  }

  @Override
  public void updatePassword(
      UserPrincipal currentUser, UpdatePasswordRequest updatePasswordRequest) {
    userRepository
        .findById(currentUser.getId())
        .ifPresent(
            user -> {
              String password = passwordEncoder.encode(updatePasswordRequest.getNewPassword());
              user.setPassword(password);
              userRepository.saveAndFlush(user);
            });
  }
}
