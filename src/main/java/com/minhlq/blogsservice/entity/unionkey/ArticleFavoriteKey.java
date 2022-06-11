package com.minhlq.blogsservice.entity.unionkey;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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

  private static final long serialVersionUID = -8855550399306606235L;

  @Column(name = "article_id", nullable = false)
  private Long articleId;

  @Column(name = "user_id", nullable = false)
  private Long userId;
}
