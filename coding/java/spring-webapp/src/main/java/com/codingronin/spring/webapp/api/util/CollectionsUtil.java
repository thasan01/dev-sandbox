package com.codingronin.spring.webapp.api.util;

import java.util.Collection;
import java.util.Collections;

public class CollectionsUtil {

  private CollectionsUtil() {}

  public static <T> Iterable<T> nullSafe(Iterable<T> iterable) {
    return iterable == null ? Collections.<T>emptyList() : iterable;
  }

  public static <T> Collection<T> nullSafe(Collection<T> collection) {
    return collection == null ? Collections.<T>emptyList() : collection;
  }


}
