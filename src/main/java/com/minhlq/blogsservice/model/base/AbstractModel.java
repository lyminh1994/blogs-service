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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;

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
  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "date_created", nullable = false, updatable = false)
  private Date createdAt;

  /**
   * The date the resource was last modified, as GMT.
   */
  @LastModifiedDate
  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "date_modified", nullable = false)
  private Date lastUpdatedAt;

}
