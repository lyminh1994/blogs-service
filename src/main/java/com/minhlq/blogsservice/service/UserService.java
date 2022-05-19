package com.minhlq.blogsservice.service;

import com.minhlq.blogsservice.dto.UpdateUserDto;
import com.minhlq.blogsservice.dto.response.ProfileResponse;
import com.minhlq.blogsservice.payload.UserPrincipal;
import javax.validation.Valid;
import org.springframework.validation.annotation.Validated;

@Validated
public interface UserService {

  UserPrincipal updateUser(@Valid UpdateUserDto updateUserDto);

  ProfileResponse findByUsername(String username);

  ProfileResponse followByUsername(String username);

  ProfileResponse unFollowByUsername(String username);
}
