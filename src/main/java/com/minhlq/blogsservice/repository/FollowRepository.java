package com.minhlq.blogsservice.repository;

import com.minhlq.blogsservice.model.FollowEntity;
import com.minhlq.blogsservice.model.unionkey.FollowKey;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface FollowRepository
    extends JpaRepository<FollowEntity, FollowKey>, QuerydslPredicateExecutor<FollowEntity> {

  @Query("select F.id.followId from FollowEntity F where F.id.userId = :userId")
  Set<Long> findFollowedUsers(Long userId);
}
