package com.minhlq.blogsservice.model;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Objects;

/**
 * The comment model for the application.
 *
 * @author Minh Lys
 * @version 1.0
 * @since 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "comments")
public class CommentEntity extends AbstractAuditEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    if (!(o instanceof CommentEntity comment) || !super.equals(o)) {
      return false;
    }

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
