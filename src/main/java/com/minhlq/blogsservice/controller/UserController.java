package com.minhlq.blogsservice.controller;

import com.minhlq.blogsservice.annotation.Loggable;
import com.minhlq.blogsservice.dto.UpdateUserDTO;
import com.minhlq.blogsservice.dto.request.UpdateUserRequest;
import com.minhlq.blogsservice.dto.response.ProfileResponse;
import com.minhlq.blogsservice.payload.UserPrincipal;
import com.minhlq.blogsservice.payload.response.UserResponse;
import com.minhlq.blogsservice.service.UserService;
import com.minhlq.blogsservice.util.SecurityUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
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
@RequestMapping("/user")
@Tag(name = "User", description = "User Information APIs")
public class UserController {

  private final UserService userService;

  /**
   * Updates the user profile with the details provided.
   *
   * @param updateUserRequest the user
   * @return new user details.
   */
  @Loggable
  @PutMapping
  @PreAuthorize("isFullyAuthenticated()")
  @Operation(summary = "Update user info", description = "Update current user information")
  public UserResponse updateUser(@RequestBody @Valid UpdateUserRequest updateUserRequest) {
    UserPrincipal currentUser = SecurityUtils.getAuthenticatedUserDetails();

    UpdateUserDTO updateUser = new UpdateUserDTO(currentUser, updateUserRequest);
    UserPrincipal userDetails = userService.updateUser(updateUser);

    // Authenticate user with the updated profile.
    SecurityUtils.authenticateUser(userDetails);

    return UserResponse.getUserResponse(userDetails);
  }

  /**
   * Get user profile by username.
   *
   * @param username the username
   * @return user profile.
   */
  @SecurityRequirements
  @GetMapping("/{username}")
  @Operation(summary = "Get profile", description = "Get user profile by username")
  public ProfileResponse getProfile(@PathVariable("username") String username) {
    return userService.findByUsername(username);
  }

  /**
   * Following user by username.
   *
   * @param username the username following
   * @return user profile.
   */
  @PutMapping(path = "/{target-username}/following")
  @Operation(summary = "Following", description = "Following user by username")
  public ProfileResponse following(@PathVariable("target-username") String username) {
    return userService.followByUsername(username);
  }

  /**
   * Unfollowing user by username.
   *
   * @param username the username un-following
   * @return user profile.
   */
  @DeleteMapping(path = "/{target-username}/following")
  @Operation(summary = "Unfollowing", description = "Unfollowing user by username")
  public ProfileResponse unFollowing(@PathVariable("target-username") String username) {
    return userService.unFollowByUsername(username);
  }
}
