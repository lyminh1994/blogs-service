package com.minhlq.blogsservice.model;

import com.minhlq.blogsservice.model.unionkey.ArticleTagKey;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.io.Serializable;

/**
 * The article tag relation for the application.
 *
 * @author Minh Lys
 * @version 1.0
 * @since 1.0
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "articles_tags")
public class ArticleTagEntity implements Serializable {

  @EmbeddedId private ArticleTagKey id;
}
