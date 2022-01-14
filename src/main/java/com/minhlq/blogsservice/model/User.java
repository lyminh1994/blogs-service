package com.minhlq.blogsservice.model;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class User implements Serializable {

  private static final long serialVersionUID = 8109816171293709396L;

  @Id
  @Column(name = "id", nullable = false)
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "username", unique = true)
  private String username;

  @Column(name = "password")
  private String password;

  @Column(name = "email", unique = true)
  private String email;

  @Column(name = "bio")
  private String bio;

  @Column(name = "image", length = 511)
  private String image;

  @ManyToMany
  @JoinTable(
      name = "follows",
      joinColumns = @JoinColumn(name = "target_id", referencedColumnName = "id"),
      inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"))
  private Collection<User> follows;

  @OneToMany(mappedBy = "author")
  private Collection<Article> articles;

  @ManyToMany(mappedBy = "favorites")
  private Collection<Article> articlesFavorites;

  @OneToMany(mappedBy = "user")
  private Collection<Comment> comments;
}
