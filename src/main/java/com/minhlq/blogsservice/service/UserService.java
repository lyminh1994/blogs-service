package com.minhlq.blogsservice.service;

import com.minhlq.blogsservice.dto.UpdateUserDto;
import com.minhlq.blogsservice.dto.UserPrincipal;
import javax.validation.Valid;
import org.springframework.validation.annotation.Validated;

@Validated
public interface UserService {

  UserPrincipal updateProfile(@Valid UpdateUserDto updateUserDto);
}
