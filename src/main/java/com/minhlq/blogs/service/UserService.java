package com.minhlq.blogs.service;

import com.minhlq.blogs.dto.UpdateUserDto;
import com.minhlq.blogs.dto.response.ProfileResponse;
import com.minhlq.blogs.handler.exception.ResourceNotFoundException;
import com.minhlq.blogs.payload.SignUpRequest;
import com.minhlq.blogs.payload.UserPrincipal;
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
   * Create the user with the register request instance given.
   *
   * @param signUpBody the register information
   */
  void createUser(SignUpRequest signUpBody);

  /**
   * Update the user with the user instance given and the update type for record.
   *
   * @param updateUserDto The user with updated information
   * @return the updated user
   * @throws ResourceNotFoundException in case the given entity is {@literal null}
   */
  UserPrincipal updateUserDetails(@Valid UpdateUserDto updateUserDto);

  /**
   * Returns a user profile for the given username or throw exception if a user could not be found.
   *
   * @param currentUser user details
   * @param username The username associated to the user to find
   * @return a user for the given username or null if a user could not be found
   * @throws ResourceNotFoundException in case the given entity is {@literal null}
   */
  ProfileResponse findByUsername(UserPrincipal currentUser, String username);

  /**
   * Add follow relation and returns a user profile for the given username or throw exception if a
   * user could not be found.
   *
   * @param userId user id
   * @param username The username associated to the user to find
   * @return a user for the given username or null if a user could not be found
   * @throws ResourceNotFoundException in case the given entity is {@literal null}
   */
  ProfileResponse followByUsername(Long userId, String username);

  /**
   * Remove follow relation and returns a user profile for the given username or throw exception if
   * a user could not be found.
   *
   * @param userId user id
   * @param username The username associated to the user to find
   * @return a user for the given username or null if a user could not be found
   * @throws ResourceNotFoundException in case the given entity is {@literal null}
   */
  ProfileResponse unFollowByUsername(Long userId, String username);

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
