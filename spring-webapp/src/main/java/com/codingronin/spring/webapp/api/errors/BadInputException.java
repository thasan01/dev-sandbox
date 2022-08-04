package com.codingronin.spring.webapp.api.errors;

import java.util.List;
import org.springframework.validation.FieldError;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class BadInputException extends RuntimeException {
  private static final long serialVersionUID = 1L;
  private final String responseId;
  private final List<FieldError> fieldErrors;
}
