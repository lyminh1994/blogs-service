package com.minhlq.blogsservice.entity;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
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
@Entity
@Table(name = "articles")
public class Article extends AbstractAuditEntity<Long> implements Serializable {

  private String title;

  @Column(unique = true)
  private String slug;

  private String description;

  private String body;

  @ToString.Exclude
  @ManyToOne(targetEntity = User.class)
  @JoinColumn(name = "user_id", referencedColumnName = "id")
  private User author;

  @ToString.Exclude
  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "article")
  private List<Comment> comments;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof Article) || !super.equals(o)) {
      return false;
    }

    Article article = (Article) o;
    return Objects.equals(getPublicId(), article.getPublicId())
        && Objects.equals(getSlug(), article.getSlug());
  }

  @Override
  protected boolean canEqual(Object other) {
    return other instanceof Article;
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), getPublicId(), getSlug());
  }
}
