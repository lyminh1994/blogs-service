package com.minhlq.blogsservice.entity;

import com.minhlq.blogsservice.entity.unionkey.ArticleTagKey;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
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
}
