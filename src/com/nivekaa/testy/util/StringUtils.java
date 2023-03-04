package com.nivekaa.testy.util;

public class StringUtils {
  private StringUtils() {}

  public static boolean isNotBlank(String str) {
    return str != null && !str.isEmpty();
  }

  public static boolean isBlank(String str) {
    return !isNotBlank(str);
  }

  public static String getSetterMethodByFieldName(String fieldName) {
    if (isBlank(fieldName)) {
      throw new NullPointerException("The field name must not be null");
    }
    return "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
  }
}
