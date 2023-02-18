package com.minhlq.blogsservice.dto.mapper;

import com.minhlq.blogsservice.dto.request.UpdateUserRequest;
import com.minhlq.blogsservice.dto.response.ProfileResponse;
import com.minhlq.blogsservice.model.UserEntity;
import com.minhlq.blogsservice.payload.UserPrincipal;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.mapstruct.NullValueMappingStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

/**
 * The UserMapper class outlines the supported conversions between User entity and other data
 * transfer objects.
 *
 * @author Minh Lys
 * @version 1.0
 * @since 1.0
 */
@Mapper(
    nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT,
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {

  /** The mapper instance. */
  UserMapper MAPPER = Mappers.getMapper(UserMapper.class);

  /**
   * Convert and populate a userDetails to user entity object.
   *
   * @param userDetails the user details
   * @return the User
   */
  @Mappings(
      value = {
        @Mapping(target = "createdBy", ignore = true),
        @Mapping(target = "createdAt", ignore = true),
        @Mapping(target = "updatedBy", ignore = true),
        @Mapping(target = "updatedAt", ignore = true),
        @Mapping(target = "userRoles", ignore = true),
        @Mapping(target = "verificationToken", ignore = true),
        @Mapping(target = "version", ignore = true)
      })
  UserEntity toUser(UserPrincipal userDetails);

  /**
   * Convert and populate a user to userProfile object.
   *
   * @param user the user
   * @param following is following user
   * @return the profile
   */
  ProfileResponse toProfileResponse(UserEntity user, boolean following);

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
        @Mapping(target = "version", ignore = true)
      })
  UserEntity toUser(@MappingTarget UserEntity user, UpdateUserRequest userRequest);
}
