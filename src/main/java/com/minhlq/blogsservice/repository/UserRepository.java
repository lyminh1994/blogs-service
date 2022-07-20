package com.minhlq.blogsservice.repository;

import com.minhlq.blogsservice.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for the User.
 *
 * @author Minh Lys
 * @version 1.0
 * @since 1.0
 */
public interface UserRepository extends JpaRepository<User, Long> {

  @EntityGraph(
      type = EntityGraphType.FETCH,
      attributePaths = {"userRoles"})
  Optional<User> findByUsername(String username);

  Optional<User> findByEmail(String email);

  Optional<User> findByPhone(String phone);

  Optional<User> findByVerificationTokenAndEnabled(String verificationToken, boolean enabled);
}
