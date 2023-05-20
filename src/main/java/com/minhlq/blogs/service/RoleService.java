package com.minhlq.blogs.service;

import com.minhlq.blogs.enums.UserRole;
import com.minhlq.blogs.model.RoleEntity;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import org.springframework.validation.annotation.Validated;

/**
 * Role service to provide implementation for the definitions about a role.
 *
 * @author Minh Lys
 * @version 1.0
 * @since 1.0
 */
@Validated
public interface RoleService {

  /**
   * Create the role with the role instance given.
   *
   * @param role the role
   * @return the persisted role with assigned id
   */
  RoleEntity save(@NotNull RoleEntity role);

  /**
   * Retrieves the role with the specified name.
   *
   * @param role the name of the role to retrieve
   * @return the role tuple that matches the id given
   */
  RoleEntity findByName(@NotNull UserRole role);

  /**
   * Find all role.
   *
   * @return list role
   */
  List<RoleEntity> findAll();
}
