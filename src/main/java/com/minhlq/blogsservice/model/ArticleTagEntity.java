package com.minhlq.blogsservice.model;

import com.minhlq.blogsservice.model.unionkey.ArticleTagKey;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "article_tags")
public class ArticleTagEntity {

  @EmbeddedId private ArticleTagKey id;

  @ManyToOne
  @JoinColumn(name = "article_id", referencedColumnName = "id")
  private ArticleEntity article;

  @ManyToOne
  @JoinColumn(name = "tag_id", referencedColumnName = "id")
  private TagEntity tag;
}
