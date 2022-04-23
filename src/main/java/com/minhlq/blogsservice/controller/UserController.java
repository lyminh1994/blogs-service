package com.minhlq.blogsservice.controller;

import com.minhlq.blogsservice.dto.UpdateUserDto;
import com.minhlq.blogsservice.dto.UserPrincipal;
import com.minhlq.blogsservice.dto.request.UpdateUserRequest;
import com.minhlq.blogsservice.dto.response.ProfileResponse;
import com.minhlq.blogsservice.service.UserService;
import com.minhlq.blogsservice.utils.SecurityUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@Tag(name = "Current User", description = "Login user information APIs")
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;

  @SecurityRequirement(name = "app_auth")
  @Operation(summary = "Current user", description = "Get current user")
  @GetMapping
  public UserPrincipal getCurrentUser() {
    return SecurityUtils.getCurrentUser();
  }

  @SecurityRequirement(name = "app_auth")
  @Operation(summary = "Update info", description = "Update current user information")
  @PutMapping
  public UserPrincipal updateProfile(@Valid @RequestBody UpdateUserRequest updateUserRequest) {
    UserPrincipal currentUser = SecurityUtils.getCurrentUser();
    return userService.updateProfile(new UpdateUserDto(currentUser, updateUserRequest));
  }

  @Operation(summary = "Get profile", description = "Get profile by username")
  @GetMapping("/{username}")
  public ProfileResponse getProfile(@PathVariable("username") String username) {
    return userService.findByUsername(username);
  }

  @SecurityRequirement(name = "app_auth")
  @Operation(summary = "Following", description = "Following user by username")
  @PostMapping(path = "/following/{target}")
  public ProfileResponse following(@PathVariable("target") String username) {
    return userService.followByUsername(username);
  }

  @SecurityRequirement(name = "app_auth")
  @Operation(summary = "Unfollowing", description = "Unfollowing user by username")
  @DeleteMapping(path = "/following/{target}")
  public ProfileResponse unFollowing(@PathVariable("target") String username) {
    return userService.unFollowByUsername(username);
  }
}
