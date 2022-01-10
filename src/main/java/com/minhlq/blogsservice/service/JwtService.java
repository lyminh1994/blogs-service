package com.minhlq.blogsservice.service;

import com.minhlq.blogsservice.dto.UserPrincipal;

import javax.servlet.http.HttpServletRequest;

public interface JwtService {

  String createJwt(UserPrincipal user);

  String getUsernameFromJwt(String jwt);

  String getJwtFromRequest(HttpServletRequest request);

}
