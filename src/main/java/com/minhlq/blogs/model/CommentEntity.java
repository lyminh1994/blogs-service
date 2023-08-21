package com.minhlq.blogs.model;

import jakarta.persistence.*;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

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
@Entity
@Table(name = "comments")
public class CommentEntity extends AuditEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "comments_seq")
  @SequenceGenerator(name = "comments_seq", allocationSize = 1)
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
