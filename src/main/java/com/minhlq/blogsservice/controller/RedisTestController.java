package com.minhlq.blogsservice.controller;

import com.minhlq.blogsservice.dto.UserPrincipal;
import com.minhlq.blogsservice.entity.UserEntity;
import com.minhlq.blogsservice.mapper.UserMapper;
import com.minhlq.blogsservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/redis")
@RequiredArgsConstructor
public class RedisTestController {
  private final UserService userService;

  @GetMapping
  public UserEntity getUserFromRedis(@RequestParam("user-id") Long id) {
    return userService.get(id);
  }

  @PostMapping
  public UserEntity addUserToRedis(@RequestBody UserPrincipal userPrincipal) {
    UserEntity user = UserMapper.MAPPER.toUser(userPrincipal);
    return userService.saveOrUpdate(user);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteUserInRedis(@PathVariable Long id) {
    userService.delete(id);
  }
}
