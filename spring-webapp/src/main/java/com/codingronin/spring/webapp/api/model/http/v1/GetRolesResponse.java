package com.codingronin.spring.webapp.api.model.http.v1;

import java.util.List;
import com.codingronin.spring.webapp.api.model.v1.Role;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class GetRolesResponse extends ApiBaseResponse {
  List<Role> roles;
}
