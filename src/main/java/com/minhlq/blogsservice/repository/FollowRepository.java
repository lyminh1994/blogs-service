package com.minhlq.blogsservice.repository;

import com.minhlq.blogsservice.entity.FollowEntity;
import com.minhlq.blogsservice.entity.unionkey.FollowKey;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Repository for the User following.
 *
 * @author Minh Lys
 * @version 1.0
 * @since 1.0
 */
@Repository
public interface FollowRepository extends JpaRepository<FollowEntity, FollowKey> {

  @Query("select F.id.followId from FollowEntity F where F.id.userId = :userId")
  Set<Long> findByUserId(Long userId);
}
