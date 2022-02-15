package com.minhlq.blogsservice.mapper;

import com.minhlq.blogsservice.dto.UserPrincipal;
import com.minhlq.blogsservice.dto.response.ProfileResponse;
import com.minhlq.blogsservice.model.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {

  UserMapper MAPPER = Mappers.getMapper(UserMapper.class);

  UserEntity toUser(UserPrincipal userPrinciple);

  UserPrincipal toUserPrinciple(UserEntity user);

  ProfileResponse toProfileResponse(UserEntity user, boolean following);
}
