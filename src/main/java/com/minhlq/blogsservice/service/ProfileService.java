package com.minhlq.blogsservice.service;

import com.minhlq.blogsservice.dto.ProfileResponse;
import com.minhlq.blogsservice.dto.UserPrincipal;

public interface ProfileService {

  ProfileResponse findByUsername(String username, UserPrincipal currentUser);

  ProfileResponse follow(String username, UserPrincipal currentUser);

  ProfileResponse unFollow(String username, UserPrincipal currentUser);

}
