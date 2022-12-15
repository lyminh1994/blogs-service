package com.minhlq.blogsservice.model;

import com.minhlq.blogsservice.config.jpa.AssignedSequenceStyleGenerator;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.Version;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * BaseEntity class allows an entity to inherit common properties from it.
 *
 * @author Minh Lys
 * @version 1.0
 * @since 1.0
 */
@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class AbstractAuditEntity<T extends Serializable> {

    private static final String SEQUENCE_NAME = "seq_id";

    private static final String SEQUENCE_INITIAL_VALUE = "1";

    private static final String STRATEGY =
            "com.minhlq.blogsservice.config.jpa.AssignedSequenceStyleGenerator";

    private static final String SEQUENCE_GENERATOR_NAME = "seq_generator";

    /**
     * Sequence Style Generator to auto generate ID based on the choices in the parameters.
     *
     * @see AssignedSequenceStyleGenerator
     */
    @GenericGenerator(
            name = SEQUENCE_GENERATOR_NAME,
            strategy = STRATEGY,
            parameters = {
                    @Parameter(name = "sequence_name", value = SEQUENCE_NAME),
                    @Parameter(name = "initial_value", value = SEQUENCE_INITIAL_VALUE),
                    @Parameter(name = "increment_size", value = SEQUENCE_INITIAL_VALUE)
            })
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQUENCE_GENERATOR_NAME)
    private T id;

    @Column(unique = true, nullable = false)
    @NotBlank(message = "Public facing id is needed for all entities")
    private String publicId;

    /**
     * Manages the version of Entities to measure the amount of modifications made to this entity.
     */
    @Version
    private short version;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @CreatedBy
    @Column(nullable = false, updatable = false)
    private String createdBy;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @LastModifiedBy
    private String updatedBy;

    /**
     * Evaluate the equality of BaseEntity class.
     *
     * @param o is the other object use in equality test.
     * @return the equality of both objects.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof AbstractAuditEntity<?>)) {
            return false;
        }

        AbstractAuditEntity<?> that = (AbstractAuditEntity<?>) o;
        if (!that.canEqual(this)) {
            return false;
        }

        return Objects.equals(this.getVersion(), that.getVersion())
                && Objects.equals(this.getPublicId(), that.getPublicId());
    }

    /**
     * This method is meant for allowing to redefine equality on several levels of the class hierarchy
     * while keeping its contract.
     *
     * @param other is the other object use in equality test.
     * @return if the other object can be equal to this object.
     * @see <a href="https://www.artima.com/articles/how-to-write-an-equality-method-in-java">More</a>
     */
    protected boolean canEqual(Object other) {
        return other instanceof AbstractAuditEntity;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getVersion(), getPublicId());
    }

    /**
     * A callback to assign a random UUID to publicId.
     *
     * <p>Assign a public id to the entity. This is used to identify the entity in the system and can
     * be shared publicly over the internet.
     */
    @PrePersist
    private void onCreate() {
        if (Objects.isNull(this.getPublicId())) {
            this.setPublicId(UUID.randomUUID().toString());
        }
    }
}
