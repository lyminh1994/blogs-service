package com.minhlq.blogs.model.unionkey;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This class hold union key for user following relation.
 *
 * @author Minh Lys
 * @version 1.0
 * @since 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class FollowKey implements Serializable {

  @Column(nullable = false)
  private Long userId;

  @Column(nullable = false)
  private Long followId;
}
