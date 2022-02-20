package com.minhlq.blogsservice.entity.unionkey;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
