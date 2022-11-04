package com.minhlq.blogsservice.repository;

import com.minhlq.blogsservice.model.TagEntity;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * Repository for the Tag.
 *
 * @author Minh Lys
 * @version 1.0
 * @since 1.0
 */
public interface TagRepository extends JpaRepository<TagEntity, Long> {

  Optional<TagEntity> findByName(String name);

  @Query("SELECT T.name from TagEntity T")
  Page<String> findNames(Pageable pageable);
}
