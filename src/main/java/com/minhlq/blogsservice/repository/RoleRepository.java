package com.minhlq.blogsservice.repository;

import com.minhlq.blogsservice.entity.RoleEntity;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

/**
 * Repository for the role.
 *
 * @author Minh Lys
 * @version 1.0
 * @since 1.0
 */
@Repository
public interface RoleRepository
    extends JpaRepository<RoleEntity, Long>, QuerydslPredicateExecutor<RoleEntity> {

  /**
   * Gets role associated with required name.
   *
   * @param name name of role.
   * @return Role found.
   */
  Optional<RoleEntity> findByName(String name);

  /**
   * Get role by user id.
   *
   * @param userId user id
   * @return set roles
   */
  @Query(
      "select distinct r from RoleEntity r inner join UserRoleEntity ur where ur.id.userId = :userId")
  Set<RoleEntity> findByUserId(Long userId);
}
