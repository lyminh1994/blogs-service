package com.minhlq.blogsservice.controller;

import com.minhlq.blogsservice.annotation.Loggable;
import com.minhlq.blogsservice.dto.UpdateUserDto;
import com.minhlq.blogsservice.dto.request.UpdatePasswordRequest;
import com.minhlq.blogsservice.dto.request.UpdateUserRequest;
import com.minhlq.blogsservice.dto.response.ProfileResponse;
import com.minhlq.blogsservice.payload.UserPrincipal;
import com.minhlq.blogsservice.payload.UserResponse;
import com.minhlq.blogsservice.service.UserService;
import com.minhlq.blogsservice.util.SecurityUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetailsPasswordService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

/**
 * This controller handles all requests relating to user.
 *
 * @author Minh Lys
 * @version 1.0
 * @since 1.0
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
@Tag(name = "User", description = "Blog User Information APIs")
public class UserController {

  private final UserService userService;

  private final UserDetailsPasswordService passwordService;

  @GetMapping
  @PreAuthorize("isFullyAuthenticated()")
  @Operation(summary = "Current user", description = "Get current user")
  public UserResponse getCurrentUser() {
    UserPrincipal currentUser = SecurityUtils.getAuthenticatedUserDetails();
    return UserResponse.getUserResponse(currentUser);
  }

  /**
   * Updates the user profile with the details provided.
   *
   * @param updateUserRequest the user
   * @return new user details.
   */
  @Loggable
  @PutMapping
  @PreAuthorize("isFullyAuthenticated()")
  @Operation(summary = "Update info", description = "Update current user information")
  public UserResponse updateUser(@Valid @RequestBody UpdateUserRequest updateUserRequest) {
    UserPrincipal currentUser = SecurityUtils.getAuthenticatedUserDetails();

    UpdateUserDto updateUser = new UpdateUserDto(currentUser, updateUserRequest);
    UserPrincipal userDetails = userService.updateUser(updateUser);

    // Authenticate user with the updated profile.
    SecurityUtils.authenticateUser(userDetails);

    return UserResponse.getUserResponse(userDetails);
  }

  @PutMapping("/password")
  @PreAuthorize("isFullyAuthenticated()")
  @Operation(summary = "Update password", description = "Update current user password")
  public void updatePassword(@Valid @RequestBody UpdatePasswordRequest updatePasswordRequest) {
    UserPrincipal currentUser = SecurityUtils.getAuthenticatedUserDetails();
    passwordService.updatePassword(currentUser, updatePasswordRequest.newPassword());
  }

  /**
   * Get user profile by username.
   *
   * @param username the username
   * @return user profile.
   */
  @GetMapping("/{username}")
  @SecurityRequirements
  @Operation(summary = "Public profile", description = "Get public user information by username")
  public ProfileResponse getProfile(@PathVariable("username") String username) {
    return userService.findByUsername(username);
  }

  /**
   * Following user by username.
   *
   * @param username the username following
   * @return user profile.
   */
  @PutMapping(path = "/{username}/following")
  @Operation(summary = "Following", description = "Following user by username")
  public ProfileResponse following(@PathVariable("username") String username) {
    return userService.followByUsername(username);
  }

  /**
   * Unfollowing user by username.
   *
   * @param username the username un-following
   * @return user profile.
   */
  @DeleteMapping(path = "/{username}/following")
  @Operation(summary = "Unfollowing", description = "Unfollowing user by username")
  public ProfileResponse unFollowing(@PathVariable("username") String username) {
    return userService.unFollowByUsername(username);
  }
}
