package com.minhlq.blogsservice.model;

import com.minhlq.blogsservice.model.unionkey.ArticleTagId;
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
@Table(name = "article_tags")
public class ArticleTag {

  @EmbeddedId private ArticleTagId id;
}
