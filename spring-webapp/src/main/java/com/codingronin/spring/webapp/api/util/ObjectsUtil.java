package com.codingronin.spring.webapp.api.util;

public class ObjectsUtil {

  private ObjectsUtil() {}

  public static <T> String nullSafeToString(T object) {
    return (object == null) ? null : object.toString();
  }

}
