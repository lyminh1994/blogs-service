package com.minhlq.blogsservice.service.impl;

import com.minhlq.blogsservice.model.RoleEntity;
import com.minhlq.blogsservice.exception.ResourceNotFoundException;
import com.minhlq.blogsservice.repository.RoleRepository;
import com.minhlq.blogsservice.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.Validate;
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
  // @CachePut({CacheConstants.ROLE, CacheConstants.ROLES})
  public RoleEntity save(RoleEntity role) {
    Validate.notNull(role, "The role cannot be null");

    return roleRepository.save(role);
  }

  /**
   * Retrieves the role with the specified name.
   *
   * @param name the name of the role to retrieve
   * @return the role tuple that matches the id given
   */
  @Override
  // @Cacheable(CacheConstants.ROLE)
  public RoleEntity findByName(String name) {
    Validate.notNull(name, "The name cannot be null");

    return roleRepository.findByName(name).orElseThrow(ResourceNotFoundException::new);
  }
}
