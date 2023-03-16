package com.minhlq.blogsservice.service;

import com.minhlq.blogsservice.enums.UserRole;
import com.minhlq.blogsservice.model.RoleEntity;
import java.util.List;

/**
 * Role service to provide implementation for the definitions about a role.
 *
 * @author Minh Lys
 * @version 1.0
 * @since 1.0
 */
public interface RoleService {

  /**
   * Create the role with the role instance given.
   *
   * @param role the role
   * @return the persisted role with assigned id
   */
  RoleEntity save(final RoleEntity role);

  /**
   * Retrieves the role with the specified name.
   *
   * @param role the name of the role to retrieve
   * @return the role tuple that matches the id given
   */
  RoleEntity findByName(final UserRole role);

  /**
   * Find all role.
   *
   * @return list role
   */
  List<RoleEntity> findAll();
}
