package com.minhlq.blogs.model.unionkey;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * This class hold union key for article favorite relation.
 *
 * @author Minh Lys
 * @version 1.0
 * @since 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class ArticleFavoriteKey implements Serializable {

  @Column(nullable = false)
  private Long articleId;

  @Column(nullable = false)
  private Long userId;
}
