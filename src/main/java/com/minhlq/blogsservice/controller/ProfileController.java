package com.minhlq.blogsservice.controller;

import com.minhlq.blogsservice.dto.ProfileResponse;
import com.minhlq.blogsservice.dto.UserPrincipal;
import com.minhlq.blogsservice.service.ProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/profiles/{username}")
@Tag(name = "User profile", description = "User profile APIs")
@RequiredArgsConstructor
public class ProfileController {

  private final ProfileService profileService;

  @Operation(summary = "Get profile", description = "Get profile by username")
  @GetMapping
  public ProfileResponse getProfile(
      @PathVariable("username") String username,
      @AuthenticationPrincipal UserPrincipal currentUser) {
    return profileService.findByUsername(username, currentUser);
  }

  @SecurityRequirement(name = "app_auth")
  @Operation(summary = "Follow", description = "Following user by username")
  @PostMapping(path = "/follow")
  public ProfileResponse follow(
      @PathVariable("username") String username,
      @AuthenticationPrincipal UserPrincipal currentUser) {
    return profileService.followByUsername(username, currentUser);
  }

  @SecurityRequirement(name = "app_auth")
  @Operation(summary = "UnFollow", description = "UnFollowing user by username")
  @DeleteMapping(path = "/follow")
  public ProfileResponse unFollow(
      @PathVariable("username") String username,
      @AuthenticationPrincipal UserPrincipal currentUser) {
    return profileService.unFollowByUsername(username, currentUser);
  }
}
