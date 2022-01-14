package com.minhlq.blogsservice.mapper;

import com.minhlq.blogsservice.dto.ProfileResponse;
import com.minhlq.blogsservice.dto.UserPrincipal;
import com.minhlq.blogsservice.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {

  UserMapper MAPPER = Mappers.getMapper(UserMapper.class);

  User toUser(UserPrincipal userPrinciple);

  UserPrincipal toUserPrinciple(User user);

  ProfileResponse toProfileResponse(User user, boolean following);
}
