package com.minhlq.blogsservice.repository;

import com.minhlq.blogsservice.entity.Follow;
import com.minhlq.blogsservice.entity.unionkey.FollowKey;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * Repository for the User following.
 *
 * @author Minh Lys
 * @version 1.0
 * @since 1.0
 */
public interface FollowRepository extends JpaRepository<Follow, FollowKey> {

  @Query("select F.id.followId from Follow F where F.id.userId = :userId")
  Set<Long> findByUserId(Long userId);
}
