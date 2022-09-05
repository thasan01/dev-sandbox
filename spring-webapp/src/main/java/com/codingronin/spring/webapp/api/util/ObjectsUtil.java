package com.codingronin.spring.webapp.api.util;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class ObjectsUtil {

  private ObjectsUtil() {}

  public static <T> String nullSafeToString(T object) {
    return (object == null) ? null : object.toString();
  }

  public static <T> List<T> toList(Iterable<T> iter) {
    return StreamSupport.stream(iter.spliterator(), false).collect(Collectors.toList());
  }


}
