package com.minhlq.blogs.model;

import com.minhlq.blogs.model.unionkey.ArticleFavoriteKey;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The article favorite relation for the application.
 *
 * @author Minh Lys
 * @version 1.0
 * @since 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "articles_favorites")
public class ArticleFavoriteEntity {

  @EmbeddedId private ArticleFavoriteKey id;
}
