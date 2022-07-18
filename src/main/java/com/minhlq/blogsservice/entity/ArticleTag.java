package com.minhlq.blogsservice.entity;

import com.minhlq.blogsservice.entity.unionkey.ArticleTagKey;
import java.io.Serializable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
public class ArticleTag implements Serializable {

  @EmbeddedId private ArticleTagKey id;
}
