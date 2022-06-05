package com.minhlq.blogsservice.entity;

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

/**
 * The comment model for the application.
 *
 * @author Minh Lys
 * @version 1.0
 * @since 1.0
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "comments")
public class CommentEntity extends BaseEntity {

  @Column(name = "body")
  private String body;

  @ManyToOne
  @JoinColumn(name = "article_id", referencedColumnName = "id")
  private ArticleEntity article;

  @ManyToOne
  @JoinColumn(name = "user_id", referencedColumnName = "id")
  private UserEntity user;
}
