package com.codingronin.spring.webapp.api.model.v1;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class EntitlementMembership {

  public enum Action {
    ADD, REMOVE
  }

  public enum Type {
    PERMISSION, ROLE
  }

  @NotNull
  Action action;

  @NotNull
  Type type;

  @NotNull
  @NotEmpty
  String name;
}
