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
public class FollowKey implements Serializable {

  private static final long serialVersionUID = 6245344112897279223L;

  @Column(name = "user_id", nullable = false)
  private Long userId;

  @Column(name = "follow_id", nullable = false)
  private Long followId;
}
