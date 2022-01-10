package com.minhlq.blogsservice.service;

import com.minhlq.blogsservice.dto.UpdateUserDto;
import com.minhlq.blogsservice.dto.UserPrincipal;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;

@Validated
public interface UserService {

  UserPrincipal updateProfile(@Valid UpdateUserDto updateUserDto);

}
