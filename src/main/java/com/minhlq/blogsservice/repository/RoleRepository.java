package com.minhlq.blogsservice.repository;

import com.minhlq.blogsservice.model.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository for the role.
 *
 * @author Minh Lys
 * @version 1.0
 * @since 1.0
 */
public interface RoleRepository extends JpaRepository<RoleEntity, Long> {

  Optional<RoleEntity> findByName(String name);
}
