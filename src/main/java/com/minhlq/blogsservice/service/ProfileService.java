package com.minhlq.blogsservice.service;

import com.minhlq.blogsservice.dto.ProfileResponse;
import com.minhlq.blogsservice.dto.UserPrincipal;

public interface ProfileService {

  ProfileResponse findByUsername(String username, UserPrincipal currentUser);

  ProfileResponse followByUsername(String username, UserPrincipal currentUser);

  ProfileResponse unFollowByUsername(String username, UserPrincipal currentUser);
}
