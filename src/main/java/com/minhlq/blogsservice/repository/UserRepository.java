package com.minhlq.blogsservice.repository;

import com.minhlq.blogsservice.entity.UserEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

/**
 * Repository for the User.
 *
 * @author Minh Lys
 * @version 1.0
 * @since 1.0
 */
@Repository
public interface UserRepository
    extends JpaRepository<UserEntity, Long>, QuerydslPredicateExecutor<UserEntity> {

  Optional<UserEntity> findByUsername(String username);

  Optional<UserEntity> findByEmail(String email);
}
