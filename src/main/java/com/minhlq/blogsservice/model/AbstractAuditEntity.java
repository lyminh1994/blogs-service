package com.minhlq.blogsservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Version;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * BaseEntity class allows an entity to inherit common properties from it.
 *
 * @author Minh Lys
 * @version 1.0
 * @since 1.0
 */
@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class AbstractAuditEntity {

  @Column(unique = true, nullable = false, updatable = false)
  @NotBlank(message = "Public facing id is needed for all entities")
  private String publicId;

  /** Manages the version of Entities to measure the amount of modifications made to this entity. */
  @Version private short version;

  @CreatedDate
  @Column(updatable = false)
  private LocalDateTime createdAt;

  @CreatedBy
  @Column(nullable = false, updatable = false)
  private String createdBy;

  @LastModifiedDate private LocalDateTime updatedAt;

  @LastModifiedBy private String updatedBy;

  /**
   * Evaluate the equality of BaseEntity class.
   *
   * @param o is the other object use in equality test.
   * @return the equality of both objects.
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof AbstractAuditEntity that)) {
      return false;
    }

    if (!that.canEqual(this)) {
      return false;
    }

    return Objects.equals(this.getVersion(), that.getVersion())
        && Objects.equals(this.getPublicId(), that.getPublicId());
  }

  /**
   * This method is meant for allowing to redefine equality on several levels of the class hierarchy
   * while keeping its contract.
   *
   * @param other is the other object use in equality test.
   * @return if the other object can be equal to this object.
   * @see <a href="https://www.artima.com/articles/how-to-write-an-equality-method-in-java">More</a>
   */
  protected boolean canEqual(Object other) {
    return other instanceof AbstractAuditEntity;
  }

  @Override
  public int hashCode() {
    return Objects.hash(getVersion(), getPublicId());
  }

  /**
   * A callback to assign a random UUID to publicId.
   *
   * <p>Assign a public id to the entity. This is used to identify the entity in the system and can
   * be shared publicly over the internet.
   */
  @PrePersist
  private void onCreate() {
    if (Objects.isNull(this.getPublicId())) {
      this.setPublicId(UUID.randomUUID().toString());
    }
  }
}
