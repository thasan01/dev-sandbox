package com.codingronin.spring.webapp.scim.dto.v2;

import java.util.List;
import com.bettercloud.scim2.common.BaseScimResource;

public interface ScimObjectMapper<F, T extends BaseScimResource> {
  F fromScim(T t);

  List<F> fromScim(List<T> t);

  T toScim(F t);

  List<T> toScim(List<F> t);
}
