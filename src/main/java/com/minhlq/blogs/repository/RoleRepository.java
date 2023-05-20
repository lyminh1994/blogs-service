package com.minhlq.blogs.repository;

import com.minhlq.blogs.model.RoleEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for the role.
 *
 * @author Minh Lys
 * @version 1.0
 * @since 1.0
 */
@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, Long> {

  Optional<RoleEntity> findByName(String name);
}
