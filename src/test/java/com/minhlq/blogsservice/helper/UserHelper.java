package com.minhlq.blogsservice.helper;

import com.minhlq.blogsservice.constant.ErrorConstants;
import com.minhlq.blogsservice.constant.UserConstants;
import com.minhlq.blogsservice.entity.User;
import com.minhlq.blogsservice.entity.UserRole;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import net.datafaker.Faker;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.validator.routines.EmailValidator;

/**
 * User utility class that holds methods used across application.
 *
 * @author Minh Lys
 * @version 1.0
 * @since 1.0
 */
public final class UserHelper {

  /** The Constant FAKER. */
  private static final Faker FAKER = Faker.instance();
  /** Minimum password length for the password generation. */
  private static final int PASSWORD_MIN_LENGTH = 4;
  /** Maximum password length for the password generation. */
  private static final int PASSWORD_MAX_LENGTH = 15;

  private static final String PIC_SUM_PHOTOS_150_RANDOM = "https://picsum.photos/150/150/?random";

  private UserHelper() {
    throw new AssertionError(ErrorConstants.NOT_INSTANTIABLE);
  }

  /**
   * Create a user.
   *
   * @return a user
   */
  public static User createUser() {
    return createUser(FAKER.name().username());
  }

  /**
   * Create a test user with flexibility.
   *
   * @param enabled if the user should be enabled or disabled
   * @return the user
   */
  public static User createUser(final boolean enabled) {
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
  public static User createUser(String username) {
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
  public static User createUser(String username, String password, String email) {
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
  public static User createUser(
      String username, String password, String email, boolean enabled) {
    User user = new User();
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
  public static void enableUser(final User userDto) {
    Validate.notNull(userDto, UserConstants.USER_DTO_MUST_NOT_BE_NULL);
    userDto.setEnabled(true);
    userDto.setFailedLoginAttempts(0);
  }

  /**
   * Verifies input string is an email.
   *
   * @param email email.
   * @return true if pattern matches valid3 email, otherwise false.
   */
  public static boolean isEmail(String email) {
    return EmailValidator.getInstance().isValid(email);
  }

  /**
   * Retrieves the roles from the userRoles.
   *
   * @param userRoles the userRoles
   * @return set of the roles as strings
   */
  public static List<String> getRoles(Set<UserRole> userRoles) {
    List<String> roles = new ArrayList<>();

    for (UserRole userRole : userRoles) {
      if (Objects.nonNull(userRole.getRole())) {
        roles.add(userRole.getRole().getName());
      }
    }

    return roles;
  }

  /**
   * Returns the user profile or random image if not found.
   *
   * @param user the user
   * @return profile image
   */
  public static String getUserProfileImage(User user) {
    if (StringUtils.isBlank(user.getProfileImage())) {
      return PIC_SUM_PHOTOS_150_RANDOM;
    }

    return user.getProfileImage();
  }
}
