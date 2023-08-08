package com.minhlq.blogs.controller;

import com.minhlq.blogs.dto.UpdateUserDto;
import com.minhlq.blogs.dto.request.UpdatePasswordRequest;
import com.minhlq.blogs.dto.request.UpdateUserRequest;
import com.minhlq.blogs.dto.response.ProfileResponse;
import com.minhlq.blogs.payload.UserResponse;
import com.minhlq.blogs.service.UserService;
import com.minhlq.blogs.util.SecurityUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
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
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
@Tag(name = "User", description = "Blog User Information APIs")
public class UserController {

  private final UserService userService;

  private final UserDetailsPasswordService passwordService;

  /**
   * Updates the user profile with the details provided.
   *
   * @param updateUserRequest the user
   * @return new user details.
   */
  @PutMapping
  @PreAuthorize("isFullyAuthenticated()")
  @Operation(summary = "Update info", description = "Update current user information")
  public UserResponse updateUser(@RequestBody @Valid UpdateUserRequest updateUserRequest) {
    var currentUser = userService.getCurrentUser();
    return userService.updateUserDetails(new UpdateUserDto(currentUser, updateUserRequest));
  }

  @PutMapping("/password")
  @PreAuthorize("isFullyAuthenticated()")
  @Operation(summary = "Update password", description = "Update current user password")
  public void updatePassword(@RequestBody @Valid UpdatePasswordRequest updatePasswordRequest) {
    var currentUser = userService.getCurrentUser();
    passwordService.updatePassword(
        SecurityUtils.buildUserDetails(currentUser), updatePasswordRequest.newPassword());
  }

  /**
   * Get user profile by publicId.
   *
   * @param publicId the publicId
   * @return user profile.
   */
  @GetMapping("/{publicId}")
  @SecurityRequirements
  @Operation(summary = "Public profile", description = "Get public user information by publicId")
  public ProfileResponse getProfile(@PathVariable String publicId) {
    return userService.findByPublicId(publicId);
  }

  /**
   * Following user by publicId.
   *
   * @param publicId the publicId following
   * @return user profile.
   */
  @PutMapping(path = "/{publicId}/following")
  @Operation(summary = "Following", description = "Following user by publicId")
  public ProfileResponse following(@PathVariable String publicId) {
    var currentUser = userService.getCurrentUser();
    return userService.followByPublicId(currentUser.getId(), publicId);
  }

  /**
   * Unfollowing user by publicId.
   *
   * @param publicId the publicId un-following
   * @return user profile.
   */
  @DeleteMapping(path = "/{publicId}/following")
  @Operation(summary = "Unfollowing", description = "Unfollowing user by publicId")
  public ProfileResponse unFollowing(@PathVariable String publicId) {
    var currentUser = userService.getCurrentUser();
    return userService.unFollowByPublicId(currentUser.getId(), publicId);
  }
}
