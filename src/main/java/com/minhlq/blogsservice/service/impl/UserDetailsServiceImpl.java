package com.minhlq.blogsservice.service.impl;

import com.minhlq.blogsservice.mapper.UserMapper;
import com.minhlq.blogsservice.model.User;
import com.minhlq.blogsservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

  private final UserRepository userRepository;

  @Override
  @Transactional(readOnly = true)
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User information not found: " + username));

    return UserMapper.MAPPER.toUserPrinciple(user);
  }

}
