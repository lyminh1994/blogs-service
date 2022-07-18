package com.minhlq.blogsservice.repository;

import com.minhlq.blogsservice.entity.Tag;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Repository for the Tag.
 *
 * @author Minh Lys
 * @version 1.0
 * @since 1.0
 */
@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {

  Optional<Tag> findByName(String name);

  @Query("SELECT T.name from Tag T")
  Page<String> findNames(Pageable pageable);
}
