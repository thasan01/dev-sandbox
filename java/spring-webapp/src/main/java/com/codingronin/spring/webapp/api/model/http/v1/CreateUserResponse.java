package com.codingronin.spring.webapp.api.model.http.v1;

import java.util.List;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import com.codingronin.spring.webapp.api.model.v1.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CreateUserResponse extends ApiBaseResponse {
  @NotNull
  @NotEmpty
  List<User> users;
}
