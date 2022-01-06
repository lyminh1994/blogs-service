package com.minhlq.blogsservice.mapper;

import com.minhlq.blogsservice.model.User;
import com.minhlq.blogsservice.model.UserPrinciple;
import org.mapstruct.factory.Mappers;

public interface UserMapper {

  UserMapper MAPPER = Mappers.getMapper(UserMapper.class);

  User toUser(UserPrinciple userPrinciple);

  UserPrinciple toUserPrinciple(User user);

}
