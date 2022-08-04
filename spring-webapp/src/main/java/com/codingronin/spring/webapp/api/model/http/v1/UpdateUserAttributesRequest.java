package com.codingronin.spring.webapp.api.model.http.v1;

import java.util.Map;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UpdateUserAttributesRequest extends ApiBaseRequest {
  @NotEmpty
  @NotNull
  Map<String, UserAttributes> users;
}
