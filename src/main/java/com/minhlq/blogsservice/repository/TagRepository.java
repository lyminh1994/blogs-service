package com.minhlq.blogsservice.repository;

import com.minhlq.blogsservice.model.TagEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository for the Tag.
 *
 * @author Minh Lys
 * @version 1.0
 * @since 1.0
 */
public interface TagRepository extends JpaRepository<TagEntity, Long> {

  Optional<TagEntity> findByName(String name);
}
