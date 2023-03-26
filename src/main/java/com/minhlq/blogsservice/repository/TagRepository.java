package com.minhlq.blogsservice.repository;

import com.minhlq.blogsservice.model.TagEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for the Tag.
 *
 * @author Minh Lys
 * @version 1.0
 * @since 1.0
 */
@Repository
public interface TagRepository extends JpaRepository<TagEntity, Long> {

  Optional<TagEntity> findByName(String name);
}
