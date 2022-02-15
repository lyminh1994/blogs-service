package com.minhlq.blogsservice.repository;

import com.minhlq.blogsservice.model.TagEntity;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface TagRepository
    extends JpaRepository<TagEntity, Long>, QuerydslPredicateExecutor<TagEntity> {

  Optional<TagEntity> findByName(String name);

  @Query("SELECT t.name from TagEntity t")
  Page<String> findNames(Pageable pageable);
}
