package com.minhlq.blogsservice.model;

import com.minhlq.blogsservice.model.base.AbstractEntity;
import java.util.Collection;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
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
@Table(name = "articles")
public class Article extends AbstractEntity {

  @ManyToOne
  @JoinColumn(name = "user_id", referencedColumnName = "id")
  private User author;

  @Column(name = "slug", unique = true)
  private String slug;

  @Column(name = "title")
  private String title;

  @Column(name = "description")
  private String description;

  @Column(name = "body")
  private String body;

  @ManyToMany
  @JoinTable(
      name = "article_favorites",
      joinColumns = @JoinColumn(name = "article_id", referencedColumnName = "id"),
      inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"))
  private Collection<User> favorites;

  @ManyToMany
  @JoinTable(
      name = "article_tags",
      joinColumns = @JoinColumn(name = "article_id", referencedColumnName = "id"),
      inverseJoinColumns = @JoinColumn(name = "tag_id", referencedColumnName = "id"))
  private Collection<Tag> tags;

  @OneToMany(mappedBy = "article")
  private Collection<Comment> comments;
}
