package com.codingronin.spring.webapp.api.model.v1;

import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "app_user")
public class User {

  public enum Status {
    ACTIVE, INACTIVE
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  int id;

  @Column(unique = true)
  String userName;

  @Column(unique = true)
  String email;

  Status status;

  @OneToMany(cascade = {CascadeType.ALL}) // Cascade.ALL is used to save child objects as when
                                          // saving the User object.
  // JoinTable annotation allows you to define the names of the two derived fields
  @JoinTable(joinColumns = @JoinColumn(name = "user_id"),
      inverseJoinColumns = @JoinColumn(name = "auth_profile_id"))
  List<AuthProfile> authProfiles;

  @ManyToMany(cascade = {CascadeType.ALL})
  @JoinTable(name = "app_user_roles", joinColumns = @JoinColumn(name = "user_id"),
      inverseJoinColumns = @JoinColumn(name = "role_id"))
  List<Role> roles;

  @ManyToMany(cascade = {CascadeType.ALL})
  @JoinTable(name = "app_user_permissions", joinColumns = @JoinColumn(name = "user_id"),
      inverseJoinColumns = @JoinColumn(name = "permission_id"))
  List<Permission> directPermissions;
}
