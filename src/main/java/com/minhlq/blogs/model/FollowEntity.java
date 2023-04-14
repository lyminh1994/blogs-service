package com.minhlq.blogs.model;

import com.minhlq.blogs.model.unionkey.FollowKey;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The user follow model for the application.
 *
 * @author Minh Lys
 * @version 1.0
 * @since 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "follows")
public class FollowEntity {

  @EmbeddedId private FollowKey id;
}
