package com.minhlq.blogs.model;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;
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
@Table(name = "roles")
public class RoleEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "roles_seq")
  @SequenceGenerator(name = "roles_seq", allocationSize = 1)
  private Long id;

  private String name;

  private String description;

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "role")
  private Set<UserRoleEntity> userRoles = new HashSet<>();
}
