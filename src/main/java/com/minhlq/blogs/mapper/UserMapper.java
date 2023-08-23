package com.minhlq.blogs.mapper;

import com.minhlq.blogs.dto.request.UpdateUserRequest;
import com.minhlq.blogs.dto.response.ProfileResponse;
import com.minhlq.blogs.model.UserEntity;
import com.minhlq.blogs.payload.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.mapstruct.NullValueMappingStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;

/**
 * The UserMapper class outlines the supported conversions between User entity and other data
 * transfer objects.
 *
 * @author Minh Lys
 * @version 1.0
 * @since 1.0
 */
@Mapper(
    componentModel = "spring",
    nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT,
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {

  /**
   * Convert and populate a user to userProfile object.
   *
   * @param user the user
   * @param following is following user
   * @return the profile
   */
  ProfileResponse toProfileResponse(UserEntity user, boolean following);

  UserResponse toUserResponse(UserEntity user);

  @Mappings(
      value = {
        @Mapping(target = "enabled", ignore = true),
        @Mapping(target = "failedLoginAttempts", ignore = true),
        @Mapping(target = "id", ignore = true),
        @Mapping(target = "publicId", ignore = true),
        @Mapping(target = "username", ignore = true),
        @Mapping(target = "password", ignore = true),
        @Mapping(target = "lastSuccessfulLogin", ignore = true),
        @Mapping(target = "createdBy", ignore = true),
        @Mapping(target = "createdAt", ignore = true),
        @Mapping(target = "updatedBy", ignore = true),
        @Mapping(target = "updatedAt", ignore = true),
        @Mapping(target = "userRoles", ignore = true),
        @Mapping(target = "verificationToken", ignore = true),
        @Mapping(target = "version", ignore = true),
        @Mapping(target = "articles", ignore = true),
        @Mapping(target = "comments", ignore = true),
        @Mapping(target = "expiredVerificationToken", ignore = true)
      })
  UserEntity toUser(@MappingTarget UserEntity user, UpdateUserRequest userRequest);
}
