package com.minhlq.blogsservice.service.impl;

import com.minhlq.blogsservice.dto.ProfileResponse;
import com.minhlq.blogsservice.dto.UserPrincipal;
import com.minhlq.blogsservice.exception.ResourceNotFoundException;
import com.minhlq.blogsservice.mapper.UserMapper;
import com.minhlq.blogsservice.model.Follow;
import com.minhlq.blogsservice.model.unionkey.FollowId;
import com.minhlq.blogsservice.repository.FollowRepository;
import com.minhlq.blogsservice.repository.UserRepository;
import com.minhlq.blogsservice.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

  private final UserRepository userRepository;

  private final FollowRepository followRepository;

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
