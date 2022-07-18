package com.minhlq.blogsservice.entity;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
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
@ToString(callSuper = true)
@Entity
@Table(name = "comments")
public class Comment extends AbstractAuditEntity<Long> implements Serializable {

  private String body;

  @ToString.Exclude
  @ManyToOne(targetEntity = Article.class, fetch = FetchType.LAZY)
  private Article article;

  @ToString.Exclude
  @ManyToOne(targetEntity = User.class)
  private User user;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof Comment) || !super.equals(o)) {
      return false;
    }

    Comment comment = (Comment) o;
    return Objects.equals(getPublicId(), comment.getPublicId());
  }

  @Override
  protected boolean canEqual(Object other) {
    return other instanceof Comment;
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), getPublicId());
  }
}
