package com.minhlq.blogsservice.repository;

import com.minhlq.blogsservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {

  Optional<User> findByUsername(String username);

}
