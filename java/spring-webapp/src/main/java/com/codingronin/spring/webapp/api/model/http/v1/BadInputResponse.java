package com.codingronin.spring.webapp.api.model.http.v1;

import java.util.List;
import org.springframework.validation.FieldError;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor

public class BadInputResponse extends ApiBaseResponse {
  List<FieldError> errors;
}
