package com.minhlq.blogsservice.service.impl;

import com.minhlq.blogsservice.dto.ProfileResponse;
import com.minhlq.blogsservice.dto.UserPrincipal;
import com.minhlq.blogsservice.exception.ResourceNotFoundException;
import com.minhlq.blogsservice.mapper.UserMapper;
import com.minhlq.blogsservice.model.Follow;
import com.minhlq.blogsservice.model.unionkey.FollowKey;
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
    return userRepository.findByUsername(username).map(targetUser -> {
      boolean following = currentUser != null && followRepository.findById(new FollowKey(currentUser.getId(), targetUser.getId())).isPresent();
      return UserMapper.MAPPER.toProfileResponse(targetUser, following);
    }).orElseThrow(ResourceNotFoundException::new);
  }

  @Override
  public ProfileResponse follow(String username, UserPrincipal currentUser) {
    return userRepository.findByUsername(username).map(targetUser -> {
      FollowKey followKey = new FollowKey(currentUser.getId(), targetUser.getId());
      if (followRepository.findById(followKey).isEmpty()) {
        followRepository.save(new Follow(followKey));
      }

      return UserMapper.MAPPER.toProfileResponse(targetUser, true);
    }).orElseThrow(ResourceNotFoundException::new);
  }

  @Override
  public ProfileResponse unFollow(String username, UserPrincipal currentUser) {
    return userRepository.findByUsername(username).map(targetUser -> {
      FollowKey followKey = new FollowKey(currentUser.getId(), targetUser.getId());
      followRepository.findById(followKey).ifPresent(followRepository::delete);

      return UserMapper.MAPPER.toProfileResponse(targetUser, false);
    }).orElseThrow(ResourceNotFoundException::new);
  }

}
