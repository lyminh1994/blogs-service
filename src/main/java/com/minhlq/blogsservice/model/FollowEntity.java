package com.minhlq.blogsservice.model;

import com.minhlq.blogsservice.model.unionkey.FollowKey;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * The user follow model for the application.
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
@Table(name = "follows")
public class FollowEntity implements Serializable {

    @EmbeddedId
    private FollowKey id;
}
