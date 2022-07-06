package com.minhlq.blogsservice.entity;

import com.minhlq.blogsservice.config.jpa.BaseEntity;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * The user role relationship model for the application.
 *
 * @author Minh Lys
 * @version 1.0
 * @since 1.0
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@Entity
@Table(name = "users_roles")
public class UserRoleEntity extends BaseEntity<Long> implements Serializable {

  @ToString.Exclude
  @ManyToOne(fetch = FetchType.LAZY, targetEntity = UserEntity.class)
  private UserEntity user;

  @ToString.Exclude
  @ManyToOne(fetch = FetchType.LAZY, targetEntity = RoleEntity.class, cascade = CascadeType.MERGE)
  private RoleEntity role;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof UserRoleEntity) || !super.equals(o)) {
      return false;
    }

    UserRoleEntity userRole = (UserRoleEntity) o;
    return Objects.equals(getUser(), userRole.getUser())
        && Objects.equals(getRole(), userRole.getRole());
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), getUser(), getRole());
  }

  @Override
  protected boolean canEqual(Object other) {
    return other instanceof UserRoleEntity;
  }
}
