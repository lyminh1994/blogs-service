package com.minhlq.blogsservice.model;

import com.minhlq.blogsservice.model.unionkey.ArticleFavoriteId;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "article_favorites")
public class ArticleFavorite {

  @EmbeddedId private ArticleFavoriteId id;
}
