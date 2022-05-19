package com.minhlq.blogsservice.dto.mapper;

import com.minhlq.blogsservice.dto.response.ProfileResponse;
import com.minhlq.blogsservice.entity.UserEntity;
import com.minhlq.blogsservice.payload.UserPrincipal;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

/**
 * The UserMapper class outlines the supported conversions between User entity and other data
 * transfer objects.
 *
 * @author Minh Lys
 * @version 1.0
 * @since 1.0
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

  /** The mapper instance. */
  UserMapper MAPPER = Mappers.getMapper(UserMapper.class);

  /**
   * Convert and populate a userDetails to user entity object.
   *
   * @param userDetails the user details
   * @return the User
   */
  UserEntity toUser(UserPrincipal userDetails);

  /**
   * Convert and populate a user to userProfile object.
   *
   * @param user the user
   * @param following is following user
   * @return the profile
   */
  ProfileResponse toProfileResponse(UserEntity user, boolean following);
}
