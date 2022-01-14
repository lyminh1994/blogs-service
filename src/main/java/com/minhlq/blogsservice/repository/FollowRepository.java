package com.minhlq.blogsservice.repository;

import com.minhlq.blogsservice.model.Follow;
import com.minhlq.blogsservice.model.unionkey.FollowId;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface FollowRepository
    extends JpaRepository<Follow, FollowId>, QuerydslPredicateExecutor<Follow> {

  @Query("select F.id.targetId from Follow F where F.id.userId = :userId")
  Set<Long> findFollowedUsers(Long userId);
}
