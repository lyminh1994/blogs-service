package com.minhlq.blogs.service.impl;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAccessor;
import java.util.Optional;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.stereotype.Service;

/**
 * DateTimeProvider simply creating new clocked LocalDateTime instances for each method call. This
 * will make it easier to create users with specified createdAt and updatedAt dates.
 *
 * @author Minh Lys
 * @version 1.0
 * @since 1.0
 */
@Service
@RequiredArgsConstructor
public class DateTimeProviderImpl implements DateTimeProvider {

  private final Clock clock;

  /**
   * Returns the current time to be used as modification or creation date.
   *
   * @return the current time
   */
  @NonNull
  @Override
  public Optional<TemporalAccessor> getNow() {
    return Optional.of(LocalDateTime.now(clock));
  }
}
