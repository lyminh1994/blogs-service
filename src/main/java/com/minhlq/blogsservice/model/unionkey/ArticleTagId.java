package com.minhlq.blogsservice.model.unionkey;

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
public class ArticleTagId implements Serializable {

  private static final long serialVersionUID = 3549434090139835218L;

  @Column(name = "article_id", nullable = false)
  private Long articleId;

  @Column(name = "tag_id", nullable = false)
  private Long tagId;
}
