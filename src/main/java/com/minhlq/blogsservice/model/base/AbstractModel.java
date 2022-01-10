package com.minhlq.blogsservice.model.base;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.time.Instant;

import static javax.persistence.GenerationType.IDENTITY;

@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class AbstractModel implements Serializable {

  /**
   * Unique identifier for the resource.
   */
  @Id
  @GeneratedValue(strategy = IDENTITY)
  private Long id;

  /**
   * The date the resource was created, as GMT.
   */
  @CreatedDate
  @Column(name = "created_at", nullable = false, updatable = false)
  private Instant createdAt;

  /**
   * The date the resource was last modified, as GMT.
   */
  @LastModifiedDate
  @Column(name = "updated_at", nullable = false)
  private Instant updatedAt;

}
