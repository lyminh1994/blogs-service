package com.minhlq.blogs.model;

import jakarta.persistence.*;
import lombok.Data;

/**
 * The user role model for the application.
 *
 * @author Minh Lys
 * @version 1.0
 * @since 1.0
 */
@Data
@Entity
@Table(name = "roles")
public class RoleEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "roles_seq")
  @SequenceGenerator(name = "roles_seq", allocationSize = 1)
  private Long id;

  private String name;

  private String description;
}
