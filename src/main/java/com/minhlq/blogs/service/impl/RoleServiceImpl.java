package com.minhlq.blogs.service.impl;

import com.minhlq.blogs.enums.UserRole;
import com.minhlq.blogs.handler.exception.ResourceNotFoundException;
import com.minhlq.blogs.model.RoleEntity;
import com.minhlq.blogs.repository.RoleRepository;
import com.minhlq.blogs.service.RoleService;
import java.text.MessageFormat;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * The RoleServiceImpl class is an implementation for the RoleService Interface.
 *
 * @author Minh Lys
 * @version 1.0
 * @since 1.0
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RoleServiceImpl implements RoleService {

  private final RoleRepository roleRepository;

  /**
   * Create the roleEntity with the roleEntity instance given.
   *
   * @param role the roleEntity
   * @return the persisted roleEntity with assigned id
   */
  @Override
  @Transactional
  public RoleEntity save(RoleEntity role) {
    return roleRepository.save(role);
  }

  /**
   * Retrieves the role with the specified name.
   *
   * @param role the name of the role to retrieve
   * @return the role tuple that matches the id given
   */
  @Override
  public RoleEntity findByName(UserRole role) {
    return roleRepository
        .findByName(role.name())
        .orElseThrow(
            () ->
                new ResourceNotFoundException(
                    MessageFormat.format(
                        "Role with name {0} do not existed in database", role.name())));
  }
}
