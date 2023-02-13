package com.minhlq.blogsservice.model;

import com.minhlq.blogsservice.model.unionkey.ArticleTagKey;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The article tag relation for the application.
 *
 * @author Minh Lys
 * @version 1.0
 * @since 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "articles_tags")
public class ArticleTagEntity {

  @EmbeddedId private ArticleTagKey id;
}
