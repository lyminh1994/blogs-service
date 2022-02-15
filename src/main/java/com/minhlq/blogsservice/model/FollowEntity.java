package com.minhlq.blogsservice.model;

import com.minhlq.blogsservice.model.unionkey.FollowKey;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "follows")
public class FollowEntity {

  @EmbeddedId private FollowKey id;

  @ManyToOne
  @JoinColumn(name = "user_id", referencedColumnName = "id")
  private UserEntity user;

  @ManyToOne
  @JoinColumn(name = "follow_id", referencedColumnName = "id")
  private UserEntity follow;

  public FollowEntity(FollowKey id) {
    this.id = id;
  }
}
