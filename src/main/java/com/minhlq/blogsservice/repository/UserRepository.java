package com.minhlq.blogsservice.repository;

import com.minhlq.blogsservice.model.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface UserRepository extends JpaRepository<User, Long>, QuerydslPredicateExecutor<User> {

  Optional<User> findByUsername(String username);

  Optional<User> findByEmail(String email);
}
