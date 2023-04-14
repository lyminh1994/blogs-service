package com.minhlq.blogs.controller;

import com.minhlq.blogs.constant.AppConstants;
import com.minhlq.blogs.dto.UpdateUserDto;
import com.minhlq.blogs.dto.request.UpdatePasswordRequest;
import com.minhlq.blogs.dto.request.UpdateUserRequest;
import com.minhlq.blogs.dto.response.ProfileResponse;
import com.minhlq.blogs.payload.UserPrincipal;
import com.minhlq.blogs.payload.UserResponse;
import com.minhlq.blogs.service.UserService;
import com.minhlq.blogs.util.SecurityUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetailsPasswordService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * This controller handles all requests relating to user.
 *
 * @author Minh Lys
 * @version 1.0
 * @since 1.0
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(AppConstants.USER)
@Tag(name = "User", description = "Blog User Information APIs")
public class UserController {

  private final UserService userService;

  private final UserDetailsPasswordService passwordService;

  @GetMapping
  @PreAuthorize("isFullyAuthenticated()")
  @Operation(summary = "Current user", description = "Get current user")
  public UserResponse getCurrentUser(@AuthenticationPrincipal UserPrincipal currentUser) {
    return UserResponse.getUserResponse(currentUser);
  }

  /**
   * Updates the user profile with the details provided.
   *
   * @param updateUserRequest the user
   * @return new user details.
   */
  @PutMapping
  @PreAuthorize("isFullyAuthenticated()")
  @Operation(summary = "Update info", description = "Update current user information")
  public UserResponse updateUser(
      @AuthenticationPrincipal UserPrincipal currentUser,
      @Valid @RequestBody UpdateUserRequest updateUserRequest) {
    var userDetails =
        userService.updateUserDetails(new UpdateUserDto(currentUser, updateUserRequest));

    // Authenticate user with the updated profile.
    SecurityUtils.authenticateUser(userDetails);

    return UserResponse.getUserResponse(userDetails);
  }

  @PutMapping(AppConstants.PASSWORD)
  @PreAuthorize("isFullyAuthenticated()")
  @Operation(summary = "Update password", description = "Update current user password")
  public void updatePassword(
      @AuthenticationPrincipal UserPrincipal currentUser,
      @Valid @RequestBody UpdatePasswordRequest updatePasswordRequest) {
    passwordService.updatePassword(currentUser, updatePasswordRequest.newPassword());
  }

  /**
   * Get user profile by username.
   *
   * @param username the username
   * @return user profile.
   */
  @GetMapping(AppConstants.USERNAME)
  @SecurityRequirements
  @Operation(summary = "Public profile", description = "Get public user information by username")
  public ProfileResponse getProfile(
      @AuthenticationPrincipal UserPrincipal currentUser, @PathVariable String username) {
    return userService.findByUsername(currentUser, username);
  }

  /**
   * Following user by username.
   *
   * @param username the username following
   * @return user profile.
   */
  @PutMapping(path = AppConstants.FOLLOWING)
  @Operation(summary = "Following", description = "Following user by username")
  public ProfileResponse following(
      @AuthenticationPrincipal UserPrincipal currentUser, @PathVariable String username) {
    return userService.followByUsername(currentUser.id(), username);
  }

  /**
   * Unfollowing user by username.
   *
   * @param username the username un-following
   * @return user profile.
   */
  @DeleteMapping(path = AppConstants.FOLLOWING)
  @Operation(summary = "Unfollowing", description = "Unfollowing user by username")
  public ProfileResponse unFollowing(
      @AuthenticationPrincipal UserPrincipal currentUser, @PathVariable String username) {
    return userService.unFollowByUsername(currentUser.id(), username);
  }
}
