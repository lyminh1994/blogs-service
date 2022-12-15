package com.minhlq.blogsservice.service.impl;

import com.minhlq.blogsservice.exception.ResourceNotFoundException;
import com.minhlq.blogsservice.model.RoleEntity;
import com.minhlq.blogsservice.repository.RoleRepository;
import com.minhlq.blogsservice.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.Validate;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.Collections;
import java.util.List;

import static com.minhlq.blogsservice.constant.CacheConstants.ROLE;
import static com.minhlq.blogsservice.constant.CacheConstants.ROLES;

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
    @CachePut({ROLE, ROLES})
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
    @Cacheable(value = ROLE, unless = "#result != null")
    public RoleEntity findByName(String name) {
        Validate.notNull(name, "Role name cannot be null");

        return roleRepository
                .findByName(name)
                .orElseThrow(
                        () ->
                                new ResourceNotFoundException(
                                        MessageFormat.format("Role with name {0} do not existed in database", name)));
    }

    @Override
    @Cacheable(value = ROLES, unless = "!#result.isEmpty()")
    public List<RoleEntity> findAll() {
        return Collections.emptyList();
    }
}
