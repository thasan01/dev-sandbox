package com.codingronin.spring.webapp.api.model.v1;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "app_role")
public class Role {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  int id;

  @NotNull
  @NotEmpty
  String name;

  @ManyToMany
  @JoinTable(joinColumns = @JoinColumn(name = "role_id"),
      inverseJoinColumns = @JoinColumn(name = "permission_id"))
  List<Permission> permissions;

  @ManyToMany
  @JoinTable(name = "app_user_roles", joinColumns = @JoinColumn(name = "role_id"),
      inverseJoinColumns = @JoinColumn(name = "user_id"))
  List<User> members;
}
