package com.minhlq.blogsservice.helper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.minhlq.blogsservice.model.UserEntity;
import com.minhlq.blogsservice.payload.UserPrincipal;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import net.datafaker.Faker;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public class TestHelper {

  public static final String ANONYMOUS_USER = "anonymousUser";
  public static final String ANONYMOUS_ROLE = "ROLE_ANONYMOUS";
  public static final String ROLE_USER = "ROLE_USER";
  public static final String ROLE_ADMIN = "ROLE_ADMIN";
  public static final Faker FAKER = Faker.instance();
  public static final ObjectMapper objectMapper = new ObjectMapper();
  public static final String[] IGNORED_FIELDS = {
    "id", "createdAt", "createdBy", "updatedAt", "updatedBy"
  };
  public static final String[] BASE_EQUALS_AND_HASH_CODE_FIELDS = {"version", "publicId"};
  public static final String[] USER_EQUALS_FIELDS = {"publicId", "username", "email"};

  public static Collection<String> getBaseEqualsAndHashCodeFields() {
    return List.of(BASE_EQUALS_AND_HASH_CODE_FIELDS);
  }

  public static Collection<String> getIgnoredFields() {
    return List.of(IGNORED_FIELDS);
  }

  public static String[] getEntityEqualsFields(String... fields) {
    return ArrayUtils.addAll(getBaseEqualsAndHashCodeFields().toArray(new String[0]), fields);
  }

  public static Collection<String> getUserEqualsFields() {
    String[] userEquals = ArrayUtils.addAll(BASE_EQUALS_AND_HASH_CODE_FIELDS, USER_EQUALS_FIELDS);
    return List.of(userEquals);
  }

  /**
   * Sets the authentication object for unit testing purposes.
   *
   * @param username the user to authenticate
   * @param role the role to be assigned
   */
  public static void setAuthentication(final String username, final String role) {
    List<SimpleGrantedAuthority> authorities =
        Collections.singletonList(new SimpleGrantedAuthority(role));
    Authentication auth;
    if (username.equals(ANONYMOUS_USER)) {
      UserDetails user =
          org.springframework.security.core.userdetails.User.builder().username(username).password(username).authorities(authorities).build();

      auth = new AnonymousAuthenticationToken(username, user, authorities);
    } else {
      UserEntity user = UserHelper.createUser(username);
      UserPrincipal principal = UserPrincipal.buildUserDetails(user);

      auth = new UsernamePasswordAuthenticationToken(principal, null, authorities);
    }
    SecurityContextHolder.getContext().setAuthentication(auth);
  }

  /**
   * Converts an object to JSON string.
   *
   * @param object the object
   * @param <T> the type of object passed
   * @return the json string
   */
  public static <T> String toJson(T object) throws IOException {
    return objectMapper.writeValueAsString(object);
  }

  /**
   * Parse a JSON string to an object.
   *
   * @param content the content to be parsed
   * @param classType the class to be returned
   * @param <T> the class type
   * @return the parsed object
   */
  public static <T> T parse(String content, Class<T> classType) throws IOException {
    return objectMapper.readValue(content, classType);
  }
}
