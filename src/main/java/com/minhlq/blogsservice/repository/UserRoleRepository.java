package com.minhlq.blogsservice.repository;

import com.minhlq.blogsservice.entity.UserRoleEntity;
import com.minhlq.blogsservice.entity.unionkey.UserRoleKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

/**
 * Repository for the user role.
 *
 * @author Minh Lys
 * @version 1.0
 * @since 1.0
 */
@Repository
public interface UserRoleRepository
    extends JpaRepository<UserRoleEntity, UserRoleKey>, QuerydslPredicateExecutor<UserRoleEntity> {}
