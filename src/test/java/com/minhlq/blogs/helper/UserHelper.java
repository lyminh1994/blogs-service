package com.minhlq.blogs.helper;

import com.minhlq.blogs.constant.TestConstants;
import com.minhlq.blogs.enums.Gender;
import com.minhlq.blogs.model.UserEntity;
import com.minhlq.blogs.model.UserRoleEntity;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.experimental.UtilityClass;
import net.datafaker.Faker;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

/**
 * User utility class that holds methods used across application.
 *
 * @author Minh Lys
 * @version 1.0
 * @since 1.0
 */
@UtilityClass
public class UserHelper {

  /** The Constant FAKER. */
  private final Faker FAKER = new Faker();
  /** Minimum password length for the password generation. */
  private final int PASSWORD_MIN_LENGTH = 4;
  /** Maximum password length for the password generation. */
  private final int PASSWORD_MAX_LENGTH = 15;

  private final String PIC_SUM_PHOTOS_150_RANDOM = "https://picsum.photos/150/150/?random";

  /**
   * Create a user.
   *
   * @return a user
   */
  public UserEntity createUser() {
    return createUser(FAKER.name().username());
  }

  /**
   * Create a test user with flexibility.
   *
   * @param enabled if the user should be enabled or disabled
   * @return the user
   */
  public UserEntity createUser(final boolean enabled) {
    return createUser(
        FAKER.name().username(),
        FAKER.internet().password(PASSWORD_MIN_LENGTH, PASSWORD_MAX_LENGTH),
        FAKER.internet().emailAddress(),
        enabled);
  }

  /**
   * Create a user with some flexibility.
   *
   * @param username username used to create user.
   * @return a user
   */
  public UserEntity createUser(String username) {
    return createUser(
        username,
        FAKER.internet().password(PASSWORD_MIN_LENGTH, PASSWORD_MAX_LENGTH),
        FAKER.internet().emailAddress());
  }

  /**
   * Create a user with some flexibility.
   *
   * @param username username used to create user
   * @param password password used to create user.
   * @param email email used to create user.
   * @return a user
   */
  public UserEntity createUser(String username, String password, String email) {
    return createUser(username, password, email, false);
  }

  /**
   * Create user with some flexibility.
   *
   * @param username username used to create user.
   * @param password password used to create user.
   * @param email email used to create user.
   * @param enabled boolean value used to evaluate if user enabled.
   * @return a user
   */
  public UserEntity createUser(String username, String password, String email, boolean enabled) {
    UserEntity user = new UserEntity();
    user.setId(FAKER.random().nextLong());
    user.setBirthday(FAKER.date().birthday().toLocalDateTime().toLocalDate());
    user.setGender(Gender.MALE);
    user.setProfileImage(FAKER.avatar().image());
    user.setPublicId(FAKER.internet().uuid());
    user.setUsername(username);
    user.setPassword(password);
    user.setEmail(email);
    user.setPhone(FAKER.phoneNumber().cellPhone());
    user.setFirstName(FAKER.name().firstName());
    user.setLastName(FAKER.name().lastName());

    if (enabled) {
      user.setEnabled(true);
    }
    return user;
  }

  /**
   * Enables and unlocks a user.
   *
   * @param userDto the userDto
   */
  public void enableUser(final UserEntity userDto) {
    Validate.notNull(userDto, TestConstants.USER_DTO_MUST_NOT_BE_NULL);
    userDto.setEnabled(true);
    userDto.setFailedLoginAttempts(0);
  }

  /**
   * Retrieves the roles from the userRoles.
   *
   * @param userRoles the userRoles
   * @return set of the roles as strings
   */
  public Set<String> getRoles(Set<UserRoleEntity> userRoles) {
    return userRoles.stream()
        .filter(role -> role.getRole() != null)
        .map(role -> role.getRole().getName())
        .collect(Collectors.toSet());
  }

  /**
   * Returns the user profile or random image if not found.
   *
   * @param user the user
   * @return profile image
   */
  public String getUserProfileImage(UserEntity user) {
    if (StringUtils.isBlank(user.getProfileImage())) {
      return PIC_SUM_PHOTOS_150_RANDOM;
    }

    return user.getProfileImage();
  }
}
