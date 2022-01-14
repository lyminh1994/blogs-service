package com.minhlq.blogsservice.model;

import com.minhlq.blogsservice.model.base.AbstractEntity;
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
@Table(name = "comments")
public class Comment extends AbstractEntity {

  @Column(name = "body")
  private String body;

  @ManyToOne
  @JoinColumn(name = "article_id", referencedColumnName = "id")
  private Article article;

  @ManyToOne
  @JoinColumn(name = "user_id", referencedColumnName = "id")
  private User user;
}
