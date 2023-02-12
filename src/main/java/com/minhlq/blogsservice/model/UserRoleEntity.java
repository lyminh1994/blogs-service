package com.minhlq.blogsservice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.util.Objects;

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
public class UserRoleEntity extends AbstractAuditEntity implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  private Long id;

  @ToString.Exclude
  @ManyToOne(targetEntity = UserEntity.class, fetch = FetchType.LAZY)
  private UserEntity user;

  @ToString.Exclude
  @ManyToOne(
      targetEntity = RoleEntity.class,
      cascade = {CascadeType.MERGE},
      fetch = FetchType.LAZY)
  private RoleEntity role;

  public UserRoleEntity(UserEntity user, RoleEntity role) {
    this.user = user;
    this.role = role;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof UserRoleEntity userRole) || !super.equals(o)) {
      return false;
    }

    return Objects.equals(getUser(), userRole.getUser())
        && Objects.equals(getRole(), userRole.getRole());
  }

  @Override
  protected boolean canEqual(Object other) {
    return other instanceof UserRoleEntity;
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), getUser(), getRole());
  }
}
