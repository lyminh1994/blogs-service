package com.minhlq.blogs.service.impl;

import com.minhlq.blogs.handler.exception.ResourceNotFoundException;
import com.minhlq.blogs.payload.UserPrincipal;
import com.minhlq.blogs.repository.UserRepository;
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
  public UserPrincipal updatePassword(UserDetails userDetails, String newPassword) {
    var user =
        userRepository
            .findByUsername(userDetails.getUsername())
            .orElseThrow(ResourceNotFoundException::new);

    user.setPassword(passwordEncoder.encode(newPassword));
    userRepository.saveAndFlush(user);

    return UserPrincipal.buildUserDetails(user);
  }
}
