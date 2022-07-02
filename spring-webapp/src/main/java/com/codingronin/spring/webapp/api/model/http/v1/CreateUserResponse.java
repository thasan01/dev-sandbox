package com.codingronin.spring.webapp.api.model.http.v1;

import java.util.List;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CreateUserResponse extends ApiBaseResponse {
  @NotNull
  @NotEmpty
  List<CreateUser> users;
}
