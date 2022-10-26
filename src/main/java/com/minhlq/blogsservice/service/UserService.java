package com.minhlq.blogsservice.service;

import com.minhlq.blogsservice.dto.UpdateUserDto;
import com.minhlq.blogsservice.dto.response.ProfileResponse;
import com.minhlq.blogsservice.exception.ResourceNotFoundException;
import com.minhlq.blogsservice.payload.UserPrincipal;
import javax.validation.Valid;
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
   * Update the user with the user instance given and the update type for record.
   *
   * @param updateUserDto The user with updated information
   * @return the updated user
   * @throws ResourceNotFoundException in case the given entity is {@literal null}
   */
  UserPrincipal updateUser(@Valid UpdateUserDto updateUserDto);

  /**
   * Returns a user profile for the given username or throw exception if a user could not be found.
   *
   * @param username The username associated to the user to find
   * @return a user for the given username or null if a user could not be found
   * @throws ResourceNotFoundException in case the given entity is {@literal null}
   */
  ProfileResponse findByUsername(String username);

  /**
   * Add follow relation and returns a user profile for the given username or throw exception if a
   * user could not be found.
   *
   * @param username The username associated to the user to find
   * @return a user for the given username or null if a user could not be found
   * @throws ResourceNotFoundException in case the given entity is {@literal null}
   */
  ProfileResponse followByUsername(String username);

  /**
   * Remove follow relation and returns a user profile for the given username or throw exception if
   * a user could not be found.
   *
   * @param username The username associated to the user to find
   * @return a user for the given username or null if a user could not be found
   * @throws ResourceNotFoundException in case the given entity is {@literal null}
   */
  ProfileResponse unFollowByUsername(String username);
}
