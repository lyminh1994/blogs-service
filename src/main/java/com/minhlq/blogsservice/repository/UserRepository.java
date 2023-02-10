package com.minhlq.blogsservice.repository;

import com.minhlq.blogsservice.model.UserEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository for the User.
 *
 * @author Minh Lys
 * @version 1.0
 * @since 1.0
 */
public interface UserRepository extends JpaRepository<UserEntity, Long> {

  @EntityGraph(
      type = EntityGraphType.FETCH,
      attributePaths = {"userRoles"})
  Optional<UserEntity> findByUsername(String username);

  Optional<UserEntity> findByEmail(String email);

  Optional<UserEntity> findByPhone(String phone);

  Optional<UserEntity> findByVerificationTokenAndEnabled(String verificationToken, boolean enabled);
}
