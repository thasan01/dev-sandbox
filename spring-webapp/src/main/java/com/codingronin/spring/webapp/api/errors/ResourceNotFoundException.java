package com.codingronin.spring.webapp.api.errors;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResourceNotFoundException extends RuntimeException {

  private static final long serialVersionUID = 1L;
  final String responseId;
  final String resourceName;

  public ResourceNotFoundException(String responseId, String resourceName) {
    super(String.format("Unable to find %s", resourceName));
    this.responseId = responseId;
    this.resourceName = resourceName;
  }
}
