package com.minhlq.blogsservice.repository;

import com.minhlq.blogsservice.model.UserEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface UserRepository
    extends JpaRepository<UserEntity, Long>, QuerydslPredicateExecutor<UserEntity> {

  Optional<UserEntity> findByUsername(String username);

  Optional<UserEntity> findByEmail(String email);
}
