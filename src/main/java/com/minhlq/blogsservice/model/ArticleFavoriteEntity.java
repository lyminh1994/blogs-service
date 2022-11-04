package com.minhlq.blogsservice.model;

import com.minhlq.blogsservice.model.unionkey.ArticleFavoriteKey;
import java.io.Serializable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * The article favorite relation for the application.
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
@Table(name = "articles_favorites")
public class ArticleFavoriteEntity implements Serializable {

  @EmbeddedId private ArticleFavoriteKey id;
}
