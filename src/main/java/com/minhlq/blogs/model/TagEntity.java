package com.minhlq.blogs.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * The tag model for the application.
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
@Table(name = "tags")
public class TagEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tags_seq")
  @SequenceGenerator(name = "tags_seq", allocationSize = 1)
  private Long id;

  @Column(nullable = false)
  private String name;
}
