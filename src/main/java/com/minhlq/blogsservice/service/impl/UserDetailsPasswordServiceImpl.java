package com.minhlq.blogsservice.service.impl;

import com.minhlq.blogsservice.exception.ResourceNotFoundException;
import com.minhlq.blogsservice.model.UserEntity;
import com.minhlq.blogsservice.payload.UserPrincipal;
import com.minhlq.blogsservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsPasswordService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserDetailsPasswordServiceImpl implements UserDetailsPasswordService {

  private final UserRepository userRepository;

  private final PasswordEncoder passwordEncoder;

  @Override
  public UserDetails updatePassword(UserDetails userDetails, String newPassword) {
    UserEntity user =
        userRepository
            .findByUsername(userDetails.getUsername())
            .orElseThrow(ResourceNotFoundException::new);

    user.setPassword(passwordEncoder.encode(newPassword));
    userRepository.saveAndFlush(user);

    return UserPrincipal.buildUserDetails(user);
  }
}
