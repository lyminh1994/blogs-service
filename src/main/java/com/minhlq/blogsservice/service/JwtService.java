package com.minhlq.blogsservice.service;

import com.minhlq.blogsservice.model.User;

import javax.servlet.http.HttpServletRequest;

public interface JwtService {

  String createJwt(User user);

  String getUsernameFromJwt(String jwt);

  String getJwtFromRequest(HttpServletRequest request);

}
