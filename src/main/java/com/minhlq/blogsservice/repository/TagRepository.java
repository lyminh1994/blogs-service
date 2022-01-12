package com.minhlq.blogsservice.repository;

import com.minhlq.blogsservice.model.Tag;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag, String> {

  Optional<Tag> findByName(String name);
}
