package com.minhlq.blogsservice.repository;

import com.minhlq.blogsservice.model.Tag;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface TagRepository extends JpaRepository<Tag, Long>, QuerydslPredicateExecutor<Tag> {

  Optional<Tag> findByName(String name);
}
