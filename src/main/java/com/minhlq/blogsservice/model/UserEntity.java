package com.minhlq.blogsservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.minhlq.blogsservice.enums.Gender;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import lombok.Data;
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
@Data
@Entity
@Audited
@Table(name = "users")
public class UserEntity extends AuditEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
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

  private Gender gender = Gender.FEMALE;

  private boolean enabled;

  private String profileImage;

  @NotAudited private String verificationToken;

  @NotAudited private int failedLoginAttempts = 0;

  @NotAudited private LocalDateTime lastSuccessfulLogin = LocalDateTime.now();

  @NotAudited
  @ToString.Exclude
  @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
  private Set<UserRoleEntity> userRoles = new HashSet<>();

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
    UserRoleEntity userRole = new UserRoleEntity(this, role);
    userRoles.add(new UserRoleEntity(this, role));
    userRole.setUser(this);
  }

  /**
   * Remove Role from this User.
   *
   * @param role the role
   */
  public void removeRole(final RoleEntity role) {
    UserRoleEntity userRole = new UserRoleEntity(this, role);
    userRoles.remove(userRole);
    userRole.setUser(null);
  }

  /**
   * Formulates the full name of the user.
   *
   * @return the full name of the user
   */
  public String getName() {
    return StringUtils.trimToEmpty(StringUtils.join(firstName, StringUtils.SPACE, lastName));
  }
}
