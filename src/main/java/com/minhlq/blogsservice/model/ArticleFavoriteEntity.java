package com.minhlq.blogsservice.model;

import com.minhlq.blogsservice.model.unionkey.ArticleFavoriteKey;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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

  @ManyToOne
  @JoinColumn(name = "article_id", referencedColumnName = "id")
  private ArticleEntity article;

  @ManyToOne
  @JoinColumn(name = "user_id", referencedColumnName = "id")
  private UserEntity user;

  public ArticleFavoriteEntity(ArticleFavoriteKey id) {
    this.id = id;
  }
}
