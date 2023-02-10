package com.minhlq.blogsservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Objects;

/**
 * The comment model for the application.
 *
 * @author Minh Lys
 * @version 1.0
 * @since 1.0
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@Entity
@Table(name = "comments")
public class CommentEntity extends AbstractAuditEntity implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  private Long id;

  private String body;

  @ToString.Exclude
  @ManyToOne(targetEntity = ArticleEntity.class, fetch = FetchType.LAZY)
  private ArticleEntity article;

  @ToString.Exclude
  @ManyToOne(targetEntity = UserEntity.class)
  private UserEntity user;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof CommentEntity) || !super.equals(o)) {
      return false;
    }

    CommentEntity comment = (CommentEntity) o;
    return Objects.equals(getPublicId(), comment.getPublicId());
  }

  @Override
  protected boolean canEqual(Object other) {
    return other instanceof CommentEntity;
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), getPublicId());
  }
}
