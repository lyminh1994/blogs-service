package com.minhlq.blogs.model;

import jakarta.persistence.*;
import java.util.Objects;
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
@Entity
@Table(name = "users_roles")
public class UserRoleEntity extends AuditEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_roles_seq")
  @SequenceGenerator(name = "users_roles_seq", allocationSize = 1)
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
