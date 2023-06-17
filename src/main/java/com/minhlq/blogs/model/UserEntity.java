package com.minhlq.blogs.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.minhlq.blogs.enums.Gender;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

/**
 * The user model for the application.
 *
 * @author Minh Lys
 * @version 1.0
 * @since 1.0
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Audited
@Table(name = "users")
public class UserEntity extends AuditEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_seq")
  @SequenceGenerator(name = "users_seq", allocationSize = 1)
  private Long id;

  @Column(unique = true, nullable = false)
  @NotBlank(message = "{user.username.cannot.blank}")
  @Size(min = 3, max = 50, message = "{user.invalid.username.size}")
  private String username;

  @JsonIgnore
  @ToString.Exclude
  @NotBlank(message = "{user.password.cannot.blank}")
  private String password;

  @Column(unique = true)
  @Email(message = "{user.invalid.email}")
  private String email;

  private String firstName;

  private String lastName;

  @Column(unique = true)
  private String phone;

  private LocalDate birthday;

  private Gender gender;

  private boolean enabled;

  private String profileImage;

  @NotAudited private String verificationToken;
  
  @NotAudited private LocalDateTime expiredVerificationToken;

  @NotAudited @Builder.Default private int failedLoginAttempts = 0;

  @NotAudited @Builder.Default private LocalDateTime lastSuccessfulLogin = LocalDateTime.now();

  @NotAudited
  @ToString.Exclude
  @Builder.Default
  @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
  private Set<UserRoleEntity> userRoles = new HashSet<>();

  @NotAudited
  @ToString.Exclude
  @OneToMany(cascade = CascadeType.ALL, mappedBy = "author")
  private List<ArticleEntity> articles;

  @NotAudited
  @ToString.Exclude
  @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
  private List<CommentEntity> comments;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof UserEntity user) || !super.equals(o)) {
      return false;
    }

    return Objects.equals(getPublicId(), user.getPublicId())
        && Objects.equals(getUsername(), user.getUsername())
        && Objects.equals(getEmail(), user.getEmail());
  }

  @Override
  protected boolean canEqual(Object other) {
    return other instanceof UserEntity;
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), getPublicId(), getUsername(), getEmail());
  }

  /**
   * Add Role to this User.
   *
   * @param role the role
   */
  public void addRole(final RoleEntity role) {
    userRoles.add(new UserRoleEntity(this, role));
  }

  /**
   * Remove Role from this User.
   *
   * @param role the role
   */
  public void removeRole(final RoleEntity role) {
    UserRoleEntity userRole = new UserRoleEntity(this, role);
    userRoles.remove(userRole);
  }

  /**
   * Formulates the full name of the user.
   *
   * @return the full name of the user
   */
  public String getFullName() {
    return StringUtils.trimToEmpty(StringUtils.join(firstName, StringUtils.SPACE, lastName));
  }
}
