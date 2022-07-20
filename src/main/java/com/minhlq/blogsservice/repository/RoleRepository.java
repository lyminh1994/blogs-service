package com.minhlq.blogsservice.repository;

import com.minhlq.blogsservice.entity.Role;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for the role.
 *
 * @author Minh Lys
 * @version 1.0
 * @since 1.0
 */
public interface RoleRepository extends JpaRepository<Role, Long> {

  Optional<Role> findByName(String name);
}
