package com.minhlq.blogsservice.entity;

import com.minhlq.blogsservice.config.jpa.BaseEntity;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * The article model for the application.
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
@Table(name = "articles")
public class ArticleEntity extends BaseEntity<Long> implements Serializable {

  @ToString.Exclude
  @ManyToOne(targetEntity = UserEntity.class)
  private UserEntity author;

  @Column(unique = true)
  private String slug;

  private String title;

  private String description;

  private String body;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof ArticleEntity) || !super.equals(o)) {
      return false;
    }

    ArticleEntity article = (ArticleEntity) o;
    return Objects.equals(getPublicId(), article.getPublicId())
        && Objects.equals(getSlug(), article.getSlug());
  }

  @Override
  protected boolean canEqual(Object other) {
    return other instanceof ArticleEntity;
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), getPublicId(), getSlug());
  }
}
