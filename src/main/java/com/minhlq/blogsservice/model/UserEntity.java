package com.minhlq.blogsservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.minhlq.blogsservice.constant.UserConstants;
import com.minhlq.blogsservice.enums.Gender;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
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
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@Entity
@Audited
@Table(name = "users")
public class UserEntity extends AbstractAuditEntity<Long> implements Serializable {

  @Column(unique = true, nullable = false)
  @NotBlank(message = UserConstants.USERNAME_CANNOT_BLANK)
  @Size(min = 3, max = 50, message = UserConstants.INVALID_USERNAME_SIZE)
  private String username;

  @JsonIgnore
  @ToString.Exclude
  @NotBlank(message = UserConstants.PASSWORD_CANNOT_BLANK)
  private String password;

  @Column(unique = true)
  @Email(message = UserConstants.INVALID_EMAIL)
  private String email;

  private String firstName;

  private String lastName;

  @Column(unique = true)
  private String phone;

  private LocalDate birthday;

  private Gender gender;

  private boolean enabled;

  private String profileImage;

  private String verificationToken;

  @NotAudited private int failedLoginAttempts;

  @NotAudited private LocalDateTime lastSuccessfulLogin;

  @NotAudited
  @ToString.Exclude
  @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
  private Set<UserRoleEntity> userRoles = new HashSet<>();

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof UserEntity) || !super.equals(o)) {
      return false;
    }

    UserEntity user = (UserEntity) o;
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
    return StringUtils.trimToNull(StringUtils.join(StringUtils.SPACE, firstName, lastName));
  }
}
