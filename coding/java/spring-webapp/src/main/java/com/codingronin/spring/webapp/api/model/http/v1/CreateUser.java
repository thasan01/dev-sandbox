package com.codingronin.spring.webapp.api.model.http.v1;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor

public class CreateUser {
  @NotNull
  String userName;

  @Email
  String email;

  String password;
}
