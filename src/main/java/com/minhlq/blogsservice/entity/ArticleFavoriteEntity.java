package com.minhlq.blogsservice.entity;

import com.minhlq.blogsservice.entity.unionkey.ArticleFavoriteKey;
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
public class ArticleFavoriteEntity {

  @EmbeddedId private ArticleFavoriteKey id;
}
