package com.minhlq.blogs.service;

import com.minhlq.blogs.dto.UpdateUserDto;
import com.minhlq.blogs.dto.response.ProfileResponse;
import com.minhlq.blogs.handler.exception.ResourceNotFoundException;
import com.minhlq.blogs.model.UserEntity;
import com.minhlq.blogs.payload.UserResponse;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;

/**
 * This UserService interface is the contract for the user service operations.
 *
 * @author Minh Lys
 * @version 1.0
 * @since 1.0
 */
@Validated
public interface UserService {

  /**
   * Retrieves the currently logged-in user's information.
   *
   * @return The currently logged-in user. Throw exception if not found user.
   */
  UserEntity getCurrentUser();

  /**
   * Update the user with the user instance given and the update type for record.
   *
   * @param updateUserDto The user with updated information
   * @return the updated user
   * @throws ResourceNotFoundException in case the given entity is {@literal null}
   */
  UserResponse updateUserDetails(@Valid UpdateUserDto updateUserDto);

  /**
   * Returns a user profile for the given publicId or throw exception if a user could not be found.
   *
   * @param publicId The publicId associated to the user to find
   * @return a user for the given publicId or null if a user could not be found
   * @throws ResourceNotFoundException in case the given entity is {@literal null}
   */
  ProfileResponse findByPublicId(String publicId);

  /**
   * Add follow relation and returns a user profile for the given publicId or throw exception if a
   * user could not be found.
   *
   * @param userId user id
   * @param publicId The publicId associated to the user to find
   * @return a user for the given publicId or null if a user could not be found
   * @throws ResourceNotFoundException in case the given entity is {@literal null}
   */
  ProfileResponse followByPublicId(Long userId, String publicId);

  /**
   * Remove follow relation and returns a user profile for the given publicId or throw exception if
   * a user could not be found.
   *
   * @param userId user id
   * @param publicId The publicId associated to the user to find
   * @return a user for the given publicId or null if a user could not be found
   * @throws ResourceNotFoundException in case the given entity is {@literal null}
   */
  ProfileResponse unFollowByPublicId(Long userId, String publicId);

  /**
   * Check username existed in database
   *
   * @param username - The username
   * @return is username existed
   */
  boolean isUsernameExisted(String username);

  /**
   * Check email exited in database
   *
   * @param email - The email
   * @return is email exited
   */
  boolean isEmailExisted(String email);
}
