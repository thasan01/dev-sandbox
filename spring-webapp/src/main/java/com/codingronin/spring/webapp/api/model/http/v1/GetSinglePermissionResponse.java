package com.codingronin.spring.webapp.api.model.http.v1;

import com.codingronin.spring.webapp.api.model.v1.Permission;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class GetSinglePermissionResponse extends ApiBaseResponse {
  Permission permission;
}
