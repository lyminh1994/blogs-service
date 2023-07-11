package com.minhlq.blogs.repository;

import com.minhlq.blogs.model.UserEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for the User.
 *
 * @author Minh Lys
 * @version 1.0
 * @since 1.0
 */
@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

  @EntityGraph(
      type = EntityGraphType.FETCH,
      attributePaths = {"userRoles"})
  Optional<UserEntity> findByUsername(String username);

  Optional<UserEntity> findByPublicId(String publicId);

  Optional<UserEntity> findByEmail(String email);

  Optional<UserEntity> findByPhone(String phone);

  Optional<UserEntity> findByVerificationTokenAndEnabled(String verificationToken, boolean enabled);
}
