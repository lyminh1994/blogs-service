package com.minhlq.blogsservice.repository;

import com.minhlq.blogsservice.model.FollowEntity;
import com.minhlq.blogsservice.model.unionkey.FollowKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Set;

/**
 * Repository for the User following.
 *
 * @author Minh Lys
 * @version 1.0
 * @since 1.0
 */
public interface FollowRepository extends JpaRepository<FollowEntity, FollowKey> {

    @Query("select F.id.followId from FollowEntity F where F.id.userId = :userId")
    Set<Long> findByUserId(Long userId);
}
