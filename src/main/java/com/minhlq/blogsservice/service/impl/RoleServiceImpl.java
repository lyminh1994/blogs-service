package com.minhlq.blogsservice.service.impl;

import com.minhlq.blogsservice.constant.CacheConstants;
import com.minhlq.blogsservice.enums.UserRole;
import com.minhlq.blogsservice.exception.ResourceNotFoundException;
import com.minhlq.blogsservice.model.RoleEntity;
import com.minhlq.blogsservice.repository.RoleRepository;
import com.minhlq.blogsservice.service.RoleService;
import java.text.MessageFormat;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
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
  @CachePut({CacheConstants.ROLE, CacheConstants.ROLES})
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
  @Cacheable(value = CacheConstants.ROLE, unless = "#result != null")
  public RoleEntity findByName(UserRole role) {
    return roleRepository
        .findByName(role.name())
        .orElseGet(
            () -> {
              if (UserRole.ROLE_USER.match(role)) {
                var roleEntity = new RoleEntity();
                roleEntity.setName(UserRole.ROLE_USER.name());
                roleEntity.setDescription(UserRole.ROLE_USER.getDescription());
                return roleRepository.save(roleEntity);
              }

              throw new ResourceNotFoundException(
                  MessageFormat.format(
                      "Role with name {0} do not existed in database", role.name()));
            });
  }

  @Override
  @Cacheable(value = CacheConstants.ROLES, unless = "!#result.isEmpty()")
  public List<RoleEntity> findAll() {
    return roleRepository.findAll();
  }
}
