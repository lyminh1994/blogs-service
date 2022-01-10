package com.minhlq.blogsservice.repository;

import com.minhlq.blogsservice.model.Follow;
import com.minhlq.blogsservice.model.unionkey.FollowKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;

public interface FollowRepository extends JpaRepository<Follow, FollowKey> {

  @Query(value = "select count(1) from follows f where f.user_id = ? and f.follow_id = ?", nativeQuery = true)
  boolean isUserFollowing(Long userId, Long followId);

  @Query(value = "select f.follow_id from follows f where f.user_id = ? and f.user_id in (?)", nativeQuery = true)
  Set<Long> followingAuthors(Long userId, List<Long> ids);

  @Query(value = "select f.follow_id from follows f where f.user_id = ?", nativeQuery = true)
  List<Long> followedUsers(Long userId);

}
