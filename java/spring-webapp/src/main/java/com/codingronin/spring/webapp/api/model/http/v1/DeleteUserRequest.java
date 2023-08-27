package com.codingronin.spring.webapp.api.model.http.v1;

import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DeleteUserRequest extends ApiBaseResponse {
  List<String> userNames;
}
