package com.minhlq.blogsservice.controller;

import com.minhlq.blogsservice.dto.UpdateUserDto;
import com.minhlq.blogsservice.dto.UserPrincipal;
import com.minhlq.blogsservice.dto.request.UpdateUserRequest;
import com.minhlq.blogsservice.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@SecurityRequirement(name = "app_auth")
@Tag(name = "Current User", description = "Login user information APIs")
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;

  @Operation(summary = "Current user", description = "Get current login user")
  @GetMapping
  public UserPrincipal getCurrentUser(@AuthenticationPrincipal UserPrincipal currentUser) {
    return currentUser;
  }

  @Operation(summary = "Update info", description = "Update current user information")
  @PutMapping
  public UserPrincipal updateProfile(
      @Valid @RequestBody UpdateUserRequest updateUserRequest,
      @AuthenticationPrincipal UserPrincipal currentUser) {
    return userService.updateProfile(new UpdateUserDto(currentUser, updateUserRequest));
  }
}
