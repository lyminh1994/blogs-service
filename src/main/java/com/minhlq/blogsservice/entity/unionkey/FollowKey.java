package com.minhlq.blogsservice.entity.unionkey;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * This class hold union key for user following relation.
 *
 * @author Minh Lys
 * @version 1.0
 * @since 1.0
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class FollowKey implements Serializable {

  @Column(nullable = false)
  private Long userId;

  @Column(nullable = false)
  private Long followId;
}
