package com.minhlq.blogsservice.entity;

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
public class UserRole extends AbstractAuditEntity<Long> implements Serializable {

  @ToString.Exclude
  @ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
  private User user;

  @ToString.Exclude
  @ManyToOne(
      targetEntity = Role.class,
      cascade = {CascadeType.MERGE},
      fetch = FetchType.LAZY)
  private Role role;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof UserRole) || !super.equals(o)) {
      return false;
    }

    UserRole userRole = (UserRole) o;
    return Objects.equals(getUser(), userRole.getUser())
        && Objects.equals(getRole(), userRole.getRole());
  }

  @Override
  protected boolean canEqual(Object other) {
    return other instanceof UserRole;
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), getUser(), getRole());
  }
}
