package com.minhlq.blogs.service.impl;

import com.minhlq.blogs.constant.CacheConstants;
import com.minhlq.blogs.payload.UserPrincipal;
import com.minhlq.blogs.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Primary;
import org.springframework.context.i18n.LocaleContextHolder;
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

  private final MessageSource messageSource;

  private final UserRepository userRepository;

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
  @Cacheable(value = CacheConstants.USER_DETAILS, unless = "#result != null")
  public UserPrincipal loadUserByUsername(final String username) {
    var locale = LocaleContextHolder.getLocale();
    if (StringUtils.isBlank(username)) {
      throw new UsernameNotFoundException(
          messageSource.getMessage("user.username.cannot.blank", null, locale));
    }

    var user =
        userRepository
            .findByUsername(username)
            .orElseThrow(
                () ->
                    new UsernameNotFoundException(
                        messageSource.getMessage(
                            "user.not.found", new String[] {username}, locale)));

    return UserPrincipal.buildUserDetails(user);
  }
}
