package com.minhlq.blogsservice.entity;

import com.minhlq.blogsservice.entity.base.AbstractEntity;
import javax.persistence.Column;
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
@Table(name = "articles")
public class ArticleEntity extends AbstractEntity {

  @ManyToOne
  @JoinColumn(name = "user_id", referencedColumnName = "id")
  private UserEntity author;

  @Column(name = "slug", unique = true)
  private String slug;

  @Column(name = "title")
  private String title;

  @Column(name = "description")
  private String description;

  @Column(name = "body")
  private String body;
}
