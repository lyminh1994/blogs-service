package com.minhlq.blogsservice.entity;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * The user role model for the application.
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
@Cacheable
@Table(name = "roles")
public class RoleEntity implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  private Integer id;

  private String name;

  private String description;

  /**
   * Evaluate the equality of Role class.
   *
   * @param other is the object to use in equality test.
   * @return the equality of both objects.
   */
  @Override
  public boolean equals(Object other) {
    if (this == other) {
      return true;
    }
    if (!(other instanceof RoleEntity)) {
      return false;
    }
    RoleEntity that = (RoleEntity) other;
    return Objects.equals(name, that.name);
  }

  /**
   * Hashcode of Role base on name.
   *
   * @return the hash value.
   */
  @Override
  public int hashCode() {
    return Objects.hash(name);
  }
}
