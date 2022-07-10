package com.minhlq.blogsservice.service.impl;

import com.minhlq.blogsservice.constant.UserConstants;
import com.minhlq.blogsservice.entity.UserEntity;
import com.minhlq.blogsservice.payload.UserPrincipal;
import com.minhlq.blogsservice.repository.UserRepository;
import com.minhlq.blogsservice.service.RoleService;
import java.text.MessageFormat;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * The implementation of service used to query user details during login.
 *
 * @author Minh Lys
 * @version 1.0
 * @since 1.0
 */
@Primary
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserDetailsServiceImpl implements UserDetailsService {

  private final UserRepository userRepository;

  private final RoleService roleService;

  /**
   * Locates the user based on the usernameOrEmail. In the actual implementation, the search may be
   * case-sensitive, or case-insensitive depending on how the implementation instance is configured.
   * In this case, the <code>UserDetails</code> object that comes back may have a usernameOrEmail
   * that is of a different case than what was actually requested..
   *
   * @param username the usernameOrEmail identifying the user whose data is required.
   * @return a fully populated user record (never <code>null</code>)
   * @throws UsernameNotFoundException if the user could not be found or the user has no
   *     GrantedAuthority
   */
  @Override
  // @Cacheable(value = CacheConstants.USER_DETAILS, key = "{ #root.methodName, #username }")
  public UserDetails loadUserByUsername(final String username) {
    if (StringUtils.isBlank(username)) {
      throw new UsernameNotFoundException(UserConstants.BLANK_USERNAME);
    }

    UserEntity user =
        userRepository
            .findByUsername(username)
            .orElseThrow(
                () ->
                    new UsernameNotFoundException(
                        MessageFormat.format(UserConstants.USER_NOT_FOUND, username)));

    return UserPrincipal.buildUserDetails(user);
  }
}
