package com.minhlq.blogsservice.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;
import java.util.Objects;

/**
 * The article model for the application.
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
@Table(name = "articles")
public class ArticleEntity extends AbstractAuditEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String title;

  @Column(unique = true)
  private String slug;

  private String description;

  private String body;

  @ToString.Exclude
  @ManyToOne(targetEntity = UserEntity.class)
  @JoinColumn(name = "user_id", referencedColumnName = "id")
  private UserEntity author;

  @ToString.Exclude
  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "article")
  private List<CommentEntity> comments;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof ArticleEntity article) || !super.equals(o)) {
      return false;
    }

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
