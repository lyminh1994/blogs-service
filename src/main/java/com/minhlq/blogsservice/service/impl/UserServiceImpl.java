package com.minhlq.blogsservice.service.impl;

import com.minhlq.blogsservice.dto.UpdateUserDto;
import com.minhlq.blogsservice.dto.UserPrincipal;
import com.minhlq.blogsservice.dto.request.UpdateUserRequest;
import com.minhlq.blogsservice.exception.ResourceNotFoundException;
import com.minhlq.blogsservice.mapper.UserMapper;
import com.minhlq.blogsservice.model.User;
import com.minhlq.blogsservice.repository.UserRepository;
import com.minhlq.blogsservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;

  private final PasswordEncoder passwordEncoder;

  @Override
  public UserPrincipal updateProfile(UpdateUserDto updateUserDto) {
    User user = userRepository.findById(updateUserDto.getTargetUser().getId()).map(current -> {
      UpdateUserRequest params = updateUserDto.getParams();
      if (StringUtils.isNotBlank(params.getPassword())) {
        current.setPassword(passwordEncoder.encode(params.getPassword()));
      }
      current.setEmail(params.getEmail());
      current.setBio(params.getBio());
      current.setImage(params.getImage());

      return userRepository.save(current);
    }).orElseThrow(ResourceNotFoundException::new);

    return UserMapper.MAPPER.toUserPrinciple(user);
  }

}
