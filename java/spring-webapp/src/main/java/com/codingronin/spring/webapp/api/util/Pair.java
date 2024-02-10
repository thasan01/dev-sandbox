package com.codingronin.spring.webapp.api.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Pair<T, Q> {
  T first;
  Q second;
}
